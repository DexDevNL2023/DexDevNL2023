package com.teleo.insurances.service.mapper;

import static com.teleo.insurances.domain.SouscriptionAsserts.*;
import static com.teleo.insurances.domain.SouscriptionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SouscriptionMapperTest {

    private SouscriptionMapper souscriptionMapper;

    @BeforeEach
    void setUp() {
        souscriptionMapper = new SouscriptionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSouscriptionSample1();
        var actual = souscriptionMapper.toEntity(souscriptionMapper.toDto(expected));
        assertSouscriptionAllPropertiesEquals(expected, actual);
    }
}
