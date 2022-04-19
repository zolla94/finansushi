package it.zolla.finansushi.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StoricoOrdiniMapperTest {

    private StoricoOrdiniMapper storicoOrdiniMapper;

    @BeforeEach
    public void setUp() {
        storicoOrdiniMapper = new StoricoOrdiniMapperImpl();
    }
}
