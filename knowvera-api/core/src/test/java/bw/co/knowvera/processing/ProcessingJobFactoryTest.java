package bw.co.knowvera.processing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import bw.co.knowvera.document.DocumentDTO;
import bw.co.knowvera.document.type.DocumentTypeDTO;
import bw.co.knowvera.document.type.DocumentTypeService;
import bw.co.knowvera.document.type.field.ExpectedFieldDTO;
import bw.co.knowvera.document.type.field.ExpectedFieldType;
import bw.co.knowvera.processing.job.ProcessingJobRequest;
import bw.co.knowvera.processing.profile.StepType;
import bw.co.knowvera.processing.schema.DocumentTypeSpec;
import bw.co.knowvera.processing.schema.FieldSpec;
import bw.co.knowvera.processing.schema.FieldType;

@ExtendWith(MockitoExtension.class)
class VeritasJobFactoryTest {

    @Mock
    private DocumentTypeService documentTypeService;

    private static ExpectedFieldDTO field(String name, String format, Boolean mandatory) {
        ExpectedFieldDTO field = new ExpectedFieldDTO();
        field.setField(name);
        field.setFormat(format);
        field.setMandatory(mandatory);
        return field;
    }

    private static ExpectedFieldDTO field(String name, ExpectedFieldType type, String label, Boolean many) {
        ExpectedFieldDTO field = field(name, " ", null);
        field.setFieldType(type);
        field.setFieldLabel(label);
        field.setMany(many);
        return field;
    }

    private static DocumentTypeDTO type(String name, String profile, ExpectedFieldDTO... fields) {
        DocumentTypeDTO type = new DocumentTypeDTO();
        type.setId(name + "-id");
        type.setCode(name.toUpperCase());
        type.setName(name);
        type.setProcessingProfileCode(profile);
        type.setExpectedFields(List.of(fields));
        return type;
    }

    private static DocumentDTO document() {
        DocumentDTO document = new DocumentDTO();
        document.setId("doc-1");
        document.setDocumentTypeId("omang-id");
        document.setUrl("KYC_RECORD/1/omang.pdf");
        document.setFileName("omang.pdf");
        document.setMetadata(Map.of("contentType", "application/pdf"));
        document.setFileContent("stored text");
        return document;
    }

    @Test
    void buildsAJobFromTheDocumentAndItsType() {
        DocumentTypeDTO omang = type("Omang", "national-id", field("idNumber", null, true),
                field("dateOfBirth", "YYYY-MM-DD", null));
        when(documentTypeService.findById("omang-id")).thenReturn(omang);
        when(documentTypeService.getAll()).thenReturn(List.of(omang, type("Payslip", null, field("employer", null, false))));

        ProcessingJobRequest job = new ProcessingJobFactory(documentTypeService, "default").create(document(), null);

        assertEquals("national-id", job.profileCode());
        assertEquals("doc-1", job.correlationId());
        assertEquals("Omang", job.documentType());
        assertEquals("KYC_RECORD/1/omang.pdf", job.document().objectKey());
        assertEquals("application/pdf", job.document().contentType());
        assertNull(job.fields(), "the fields to extract are the document type's");
        assertEquals("Omang-id", job.documentTypeId());
        assertEquals(List.of(new DocumentTypeSpec("Omang", null, "Omang-id"),
                new DocumentTypeSpec("Payslip", null, "Payslip-id")),
                job.candidateTypes());
        assertNull(job.fromStep());
        assertNull(job.text(), "a job from the file does not carry the stored text");
        assertEquals(Map.of(), job.attributes());
    }

    @Test
    void usesTheDefaultProfileAndTheStoredTextForAnalysis() {
        DocumentTypeDTO omang = type("Omang", " ");
        when(documentTypeService.findById("omang-id")).thenReturn(omang);
        when(documentTypeService.getAll()).thenReturn(List.of(omang));

        ProcessingJobRequest job = new ProcessingJobFactory(documentTypeService, "default")
                .create(document(), StepType.FIELD_EXTRACTION);

        assertEquals("default", job.profileCode());
        assertEquals(StepType.FIELD_EXTRACTION, job.fromStep());
        assertEquals("stored text", job.text());
        assertTrue(job.jobId().length() <= 64);
    }


    @Test
    void sendsTheFieldsTypedAsTheProcessorExpectsThem() {
        assertEquals(new FieldSpec("salaries", "Monthly salary", FieldType.NUMBER, null, true, false),
                ProcessingJobFactory.field(field("salaries", ExpectedFieldType.NUMBER, "Monthly salary", true)));
        assertEquals(new FieldSpec("issued", null, FieldType.DATE, null, false, false),
                ProcessingJobFactory.field(field("issued", ExpectedFieldType.DATE, " ", null)));
        for (ExpectedFieldType type : ExpectedFieldType.values()) {
            assertEquals(type.name(), ProcessingJobFactory.field(field("x", type, null, false)).type().name());
        }
    }
}
