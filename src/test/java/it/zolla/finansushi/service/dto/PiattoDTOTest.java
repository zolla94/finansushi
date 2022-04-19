package it.zolla.finansushi.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import it.zolla.finansushi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PiattoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PiattoDTO.class);
        PiattoDTO piattoDTO1 = new PiattoDTO();
        piattoDTO1.setId(1L);
        PiattoDTO piattoDTO2 = new PiattoDTO();
        assertThat(piattoDTO1).isNotEqualTo(piattoDTO2);
        piattoDTO2.setId(piattoDTO1.getId());
        assertThat(piattoDTO1).isEqualTo(piattoDTO2);
        piattoDTO2.setId(2L);
        assertThat(piattoDTO1).isNotEqualTo(piattoDTO2);
        piattoDTO1.setId(null);
        assertThat(piattoDTO1).isNotEqualTo(piattoDTO2);
    }
}
