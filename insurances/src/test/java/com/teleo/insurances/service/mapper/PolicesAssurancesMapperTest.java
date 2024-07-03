package com.teleo.insurances.service.mapper;

import static com.teleo.insurances.domain.PolicesAssurancesAsserts.*;
import static com.teleo.insurances.domain.PolicesAssurancesTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PolicesAssurancesMapperTest {

    private PolicesAssurancesMapper policesAssurancesMapper;

    @BeforeEach
    void setUp() {
        policesAssurancesMapper = new PolicesAssurancesMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPolicesAssurancesSample1();
        var actual = policesAssurancesMapper.toEntity(policesAssurancesMapper.toDto(expected));
        assertPolicesAssurancesAllPropertiesEquals(expected, actual);
    }
}
