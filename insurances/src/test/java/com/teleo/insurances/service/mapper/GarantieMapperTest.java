package com.teleo.insurances.service.mapper;

import static com.teleo.insurances.domain.GarantieAsserts.*;
import static com.teleo.insurances.domain.GarantieTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GarantieMapperTest {

    private GarantieMapper garantieMapper;

    @BeforeEach
    void setUp() {
        garantieMapper = new GarantieMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getGarantieSample1();
        var actual = garantieMapper.toEntity(garantieMapper.toDto(expected));
        assertGarantieAllPropertiesEquals(expected, actual);
    }
}
