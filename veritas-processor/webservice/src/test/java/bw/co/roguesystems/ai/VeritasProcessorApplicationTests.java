package bw.co.roguesystems.ai;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class VeritasProcessorApplicationTests {

    private static final String PROFILE = """
            {"name": "National ID", "description": "Omang",
             "steps": [
               {"type": "TEXT_EXTRACTION", "providers": ["PDF_TEXT", "TESSERACT"]},
               {"type": "FIELD_EXTRACTION", "providers": ["OLLAMA"], "model": "gemma4",
                "userPrompt": "Fields {{fields}} from {{text}}", "options": {"temperature": "0.1"}}
             ]}""";

    @Autowired
    private MockMvc mvc;

    private static RequestPostProcessor role(String role) {
        return jwt().jwt(token -> token.claim("preferred_username", "admin"))
                .authorities(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Test
    void createsTheDefaultProfileOnStartup() throws Exception {
        mvc.perform(get("/profiles/default").with(role("profiles-view")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.version").value(1))
                .andExpect(jsonPath("$.steps.length()").value(4))
                .andExpect(jsonPath("$.steps[0].providers[1]").value("TESSERACT"));
    }

    @Test
    void savingAProfileAddsAVersionOnlyWhenItChanges() throws Exception {
        mvc.perform(put("/profiles/national-id").with(role("profiles-admin"))
                .contentType(MediaType.APPLICATION_JSON).content(PROFILE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.version").value(1))
                .andExpect(jsonPath("$.createdBy").value("admin"));
        mvc.perform(put("/profiles/national-id").with(role("profiles-admin"))
                .contentType(MediaType.APPLICATION_JSON).content(PROFILE))
                .andExpect(jsonPath("$.version").value(1));
        mvc.perform(put("/profiles/national-id").with(role("profiles-admin"))
                .contentType(MediaType.APPLICATION_JSON).content(PROFILE.replace("Omang", "Omang card")))
                .andExpect(jsonPath("$.version").value(2));

        mvc.perform(get("/profiles/national-id/versions").with(role("profiles-view")))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[1].steps[1].options.temperature").value("0.1"));
        mvc.perform(get("/profiles").with(role("profiles-view")))
                .andExpect(jsonPath("$[?(@.code == 'national-id')].version").value(2));
    }

    @Test
    void invalidProfilesAreBadRequests() throws Exception {
        mvc.perform(put("/profiles/bad").with(role("profiles-admin"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Bad\", \"steps\": [{\"type\": \"TEXT_CLEANUP\", \"providers\": [\"TESSERACT\"]}]}"))
                .andExpect(status().isBadRequest());
        mvc.perform(get("/profiles/missing").with(role("profiles-view"))).andExpect(status().isNotFound());
    }

    @Test
    void accessFollowsTheClientRoles() throws Exception {
        mvc.perform(get("/profiles")).andExpect(status().isUnauthorized());
        mvc.perform(get("/profiles").with(role("jobs-submit"))).andExpect(status().isForbidden());
        mvc.perform(put("/profiles/x").with(role("profiles-view")).contentType(MediaType.APPLICATION_JSON)
                .content(PROFILE)).andExpect(status().isForbidden());
        mvc.perform(get("/jobs/x").with(role("profiles-admin"))).andExpect(status().isForbidden());
        mvc.perform(get("/actuator/health")).andExpect(status().isOk());
    }

    @Test
    void runsAJobAndKeepsItsHistory() throws Exception {
        String job = """
                {"jobId": "job-123", "correlationId": "doc-9", "profileCode": "default",
                 "documentType": "National ID", "fields": [{"name": "idNumber", "mandatory": true}],
                 "fromStep": "FIELD_EXTRACTION", "text": "ID 123", "attributes": {"documentId": "doc-9"}}""";

        mvc.perform(post("/jobs?wait=true").with(role("jobs-submit"))
                .contentType(MediaType.APPLICATION_JSON).content(job))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("FAILED"))
                .andExpect(jsonPath("$.profileVersion").value(1))
                .andExpect(jsonPath("$.steps[0].status").value("SKIPPED"))
                .andExpect(jsonPath("$.steps[2].status").value("FAILED"))
                .andExpect(jsonPath("$.error").value(org.hamcrest.Matchers.startsWith("FIELD_EXTRACTION failed: OLLAMA")))
                .andExpect(jsonPath("$.attributes.documentId").value("doc-9"));

        mvc.perform(get("/jobs/job-123").with(role("jobs-submit")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.correlationId").value("doc-9"))
                .andExpect(jsonPath("$.text").value("ID 123"))
                .andExpect(jsonPath("$.steps.length()").value(3));
    }

    private static RequestPostProcessor user(String name) {
        return jwt().jwt(token -> token.claim("preferred_username", name))
                .authorities(new SimpleGrantedAuthority("ROLE_jobs-submit"));
    }

    @Test
    void aJobSubmittedThroughTheApiIsItsSubmitters() throws Exception {
        String job = """
                {"jobId": "job-owned", "profileCode": "default", "fromStep": "FIELD_EXTRACTION", "text": "ID 123"}""";

        mvc.perform(post("/jobs?wait=true").with(user("thabo")).contentType(MediaType.APPLICATION_JSON).content(job))
                .andExpect(status().isOk());

        mvc.perform(get("/jobs/job-owned").with(user("thabo"))).andExpect(status().isOk());
        mvc.perform(get("/jobs/job-owned").with(user("neo"))).andExpect(status().isNotFound());
        mvc.perform(post("/jobs?wait=true").with(user("neo")).contentType(MediaType.APPLICATION_JSON).content(job))
                .andExpect(status().isBadRequest());
        mvc.perform(post("/jobs").with(user("neo")).contentType(MediaType.APPLICATION_JSON).content(job))
                .andExpect(status().isBadRequest());
    }

    @Test
    void optionsAProviderCannotReadAreBadRequests() throws Exception {
        mvc.perform(put("/profiles/bad-options").with(role("profiles-admin")).contentType(MediaType.APPLICATION_JSON)
                .content(PROFILE.replace("\"0.1\"", "\"warm\"")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void savingASchemaAddsAVersionOnlyWhenItsFieldsChange() throws Exception {
        String schema = """
                {"name": "National ID", "fields": [{"name": "idNumber", "type": "STRING", "mandatory": true},
                 {"name": "dateOfBirth", "type": "DATE", "format": "dd/MM/yyyy"}]}""";
        mvc.perform(put("/schemas/NID").with(role("profiles-admin")).contentType(MediaType.APPLICATION_JSON)
                .content(schema)).andExpect(status().isOk()).andExpect(jsonPath("$.version").value(1));
        mvc.perform(put("/schemas/NID").with(role("profiles-admin")).contentType(MediaType.APPLICATION_JSON)
                .content(schema)).andExpect(status().isOk()).andExpect(jsonPath("$.version").value(1));
        mvc.perform(put("/schemas/NID").with(role("profiles-admin")).contentType(MediaType.APPLICATION_JSON)
                .content(schema.replace("dateOfBirth", "birthDate")))
                .andExpect(status().isOk()).andExpect(jsonPath("$.version").value(2));

        mvc.perform(get("/schemas/NID").with(role("profiles-view"))).andExpect(status().isOk())
                .andExpect(jsonPath("$.fields[1].name").value("birthDate"))
                .andExpect(jsonPath("$.fields[0].mandatory").value(true));
        mvc.perform(get("/schemas/NID/versions/1").with(role("profiles-view")))
                .andExpect(jsonPath("$.fields[1].name").value("dateOfBirth"));
        mvc.perform(get("/schemas").with(role("profiles-view")))
                .andExpect(jsonPath("$[?(@.code == 'NID')].version").value(2));
    }

    @Test
    void invalidSchemasAreBadRequestsAndOnlyAdminsSaveThem() throws Exception {
        mvc.perform(put("/schemas/DUP").with(role("profiles-admin")).contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Dup\", \"fields\": [{\"name\": \"a\"}, {\"name\": \"A\"}]}"))
                .andExpect(status().isBadRequest());
        mvc.perform(put("/schemas/X").with(role("profiles-view")).contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"X\"}")).andExpect(status().isForbidden());
        mvc.perform(get("/schemas/missing").with(role("profiles-view"))).andExpect(status().isNotFound());
    }
}
