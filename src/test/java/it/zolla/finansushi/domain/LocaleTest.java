package it.zolla.finansushi.domain;

import static org.assertj.core.api.Assertions.assertThat;

import it.zolla.finansushi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LocaleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Locale.class);
        Locale locale1 = new Locale();
        locale1.setId(1L);
        Locale locale2 = new Locale();
        locale2.setId(locale1.getId());
        assertThat(locale1).isEqualTo(locale2);
        locale2.setId(2L);
        assertThat(locale1).isNotEqualTo(locale2);
        locale1.setId(null);
        assertThat(locale1).isNotEqualTo(locale2);
    }
}
