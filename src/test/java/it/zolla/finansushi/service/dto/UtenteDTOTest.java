package it.zolla.finansushi.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import it.zolla.finansushi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UtenteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UtenteDTO.class);
        UtenteDTO utenteDTO1 = new UtenteDTO();
        utenteDTO1.setId(1L);
        UtenteDTO utenteDTO2 = new UtenteDTO();
        assertThat(utenteDTO1).isNotEqualTo(utenteDTO2);
        utenteDTO2.setId(utenteDTO1.getId());
        assertThat(utenteDTO1).isEqualTo(utenteDTO2);
        utenteDTO2.setId(2L);
        assertThat(utenteDTO1).isNotEqualTo(utenteDTO2);
        utenteDTO1.setId(null);
        assertThat(utenteDTO1).isNotEqualTo(utenteDTO2);
    }
}
