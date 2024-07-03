package com.teleo.insurances.service.mapper;

import static com.teleo.insurances.domain.RapportAsserts.*;
import static com.teleo.insurances.domain.RapportTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RapportMapperTest {

    private RapportMapper rapportMapper;

    @BeforeEach
    void setUp() {
        rapportMapper = new RapportMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getRapportSample1();
        var actual = rapportMapper.toEntity(rapportMapper.toDto(expected));
        assertRapportAllPropertiesEquals(expected, actual);
    }
}
