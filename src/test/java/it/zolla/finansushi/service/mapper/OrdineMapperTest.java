package it.zolla.finansushi.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrdineMapperTest {

    private OrdineMapper ordineMapper;

    @BeforeEach
    public void setUp() {
        ordineMapper = new OrdineMapperImpl();
    }
}
