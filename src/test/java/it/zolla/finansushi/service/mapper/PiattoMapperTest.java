package it.zolla.finansushi.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PiattoMapperTest {

    private PiattoMapper piattoMapper;

    @BeforeEach
    public void setUp() {
        piattoMapper = new PiattoMapperImpl();
    }
}
