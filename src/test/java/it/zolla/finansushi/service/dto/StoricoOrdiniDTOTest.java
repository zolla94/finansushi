package it.zolla.finansushi.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import it.zolla.finansushi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StoricoOrdiniDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StoricoOrdiniDTO.class);
        StoricoOrdiniDTO storicoOrdiniDTO1 = new StoricoOrdiniDTO();
        storicoOrdiniDTO1.setId(1L);
        StoricoOrdiniDTO storicoOrdiniDTO2 = new StoricoOrdiniDTO();
        assertThat(storicoOrdiniDTO1).isNotEqualTo(storicoOrdiniDTO2);
        storicoOrdiniDTO2.setId(storicoOrdiniDTO1.getId());
        assertThat(storicoOrdiniDTO1).isEqualTo(storicoOrdiniDTO2);
        storicoOrdiniDTO2.setId(2L);
        assertThat(storicoOrdiniDTO1).isNotEqualTo(storicoOrdiniDTO2);
        storicoOrdiniDTO1.setId(null);
        assertThat(storicoOrdiniDTO1).isNotEqualTo(storicoOrdiniDTO2);
    }
}
