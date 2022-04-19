package it.zolla.finansushi.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LocaleMapperTest {

    private LocaleMapper localeMapper;

    @BeforeEach
    public void setUp() {
        localeMapper = new LocaleMapperImpl();
    }
}
