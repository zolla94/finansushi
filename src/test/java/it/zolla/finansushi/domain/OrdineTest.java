package it.zolla.finansushi.domain;

import static org.assertj.core.api.Assertions.assertThat;

import it.zolla.finansushi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrdineTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ordine.class);
        Ordine ordine1 = new Ordine();
        ordine1.setId(1L);
        Ordine ordine2 = new Ordine();
        ordine2.setId(ordine1.getId());
        assertThat(ordine1).isEqualTo(ordine2);
        ordine2.setId(2L);
        assertThat(ordine1).isNotEqualTo(ordine2);
        ordine1.setId(null);
        assertThat(ordine1).isNotEqualTo(ordine2);
    }
}
