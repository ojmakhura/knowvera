package bw.co.roguesystems.ai.storage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class DocumentStoreTest {

    @Test
    void acceptsKeysAndUrls() {
        assertThat(DocumentStore.objectKey("KYC_RECORD/1/id/omang.pdf")).isEqualTo("KYC_RECORD/1/id/omang.pdf");
        assertThat(DocumentStore.objectKey("/KYC_RECORD/1/omang.pdf")).isEqualTo("KYC_RECORD/1/omang.pdf");
        assertThat(DocumentStore.objectKey("http://garage:3900/KYC_RECORD/1/omang.pdf"))
                .isEqualTo("KYC_RECORD/1/omang.pdf");
    }

    @Test
    void rejectsEmptyReferences() {
        assertThatThrownBy(() -> DocumentStore.objectKey(" ")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> DocumentStore.objectKey("https://host/")).isInstanceOf(IllegalArgumentException.class);
    }
}
