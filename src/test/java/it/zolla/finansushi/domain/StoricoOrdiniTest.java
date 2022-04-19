package it.zolla.finansushi.domain;

import static org.assertj.core.api.Assertions.assertThat;

import it.zolla.finansushi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StoricoOrdiniTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StoricoOrdini.class);
        StoricoOrdini storicoOrdini1 = new StoricoOrdini();
        storicoOrdini1.setId(1L);
        StoricoOrdini storicoOrdini2 = new StoricoOrdini();
        storicoOrdini2.setId(storicoOrdini1.getId());
        assertThat(storicoOrdini1).isEqualTo(storicoOrdini2);
        storicoOrdini2.setId(2L);
        assertThat(storicoOrdini1).isNotEqualTo(storicoOrdini2);
        storicoOrdini1.setId(null);
        assertThat(storicoOrdini1).isNotEqualTo(storicoOrdini2);
    }
}
