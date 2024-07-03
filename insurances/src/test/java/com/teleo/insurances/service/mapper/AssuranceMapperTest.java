package com.teleo.insurances.service.mapper;

import static com.teleo.insurances.domain.AssuranceAsserts.*;
import static com.teleo.insurances.domain.AssuranceTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AssuranceMapperTest {

    private AssuranceMapper assuranceMapper;

    @BeforeEach
    void setUp() {
        assuranceMapper = new AssuranceMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAssuranceSample1();
        var actual = assuranceMapper.toEntity(assuranceMapper.toDto(expected));
        assertAssuranceAllPropertiesEquals(expected, actual);
    }
}
