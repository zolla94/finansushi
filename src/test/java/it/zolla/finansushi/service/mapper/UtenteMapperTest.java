package it.zolla.finansushi.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UtenteMapperTest {

    private UtenteMapper utenteMapper;

    @BeforeEach
    public void setUp() {
        utenteMapper = new UtenteMapperImpl();
    }
}
