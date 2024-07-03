package com.teleo.insurances.service.mapper;

import static com.teleo.insurances.domain.PaiementAsserts.*;
import static com.teleo.insurances.domain.PaiementTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaiementMapperTest {

    private PaiementMapper paiementMapper;

    @BeforeEach
    void setUp() {
        paiementMapper = new PaiementMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPaiementSample1();
        var actual = paiementMapper.toEntity(paiementMapper.toDto(expected));
        assertPaiementAllPropertiesEquals(expected, actual);
    }
}
