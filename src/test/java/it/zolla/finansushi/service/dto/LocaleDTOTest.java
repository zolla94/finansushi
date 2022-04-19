package it.zolla.finansushi.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import it.zolla.finansushi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LocaleDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LocaleDTO.class);
        LocaleDTO localeDTO1 = new LocaleDTO();
        localeDTO1.setId(1L);
        LocaleDTO localeDTO2 = new LocaleDTO();
        assertThat(localeDTO1).isNotEqualTo(localeDTO2);
        localeDTO2.setId(localeDTO1.getId());
        assertThat(localeDTO1).isEqualTo(localeDTO2);
        localeDTO2.setId(2L);
        assertThat(localeDTO1).isNotEqualTo(localeDTO2);
        localeDTO1.setId(null);
        assertThat(localeDTO1).isNotEqualTo(localeDTO2);
    }
}
