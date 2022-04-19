package it.zolla.finansushi.domain;

import static org.assertj.core.api.Assertions.assertThat;

import it.zolla.finansushi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PiattoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Piatto.class);
        Piatto piatto1 = new Piatto();
        piatto1.setId(1L);
        Piatto piatto2 = new Piatto();
        piatto2.setId(piatto1.getId());
        assertThat(piatto1).isEqualTo(piatto2);
        piatto2.setId(2L);
        assertThat(piatto1).isNotEqualTo(piatto2);
        piatto1.setId(null);
        assertThat(piatto1).isNotEqualTo(piatto2);
    }
}
