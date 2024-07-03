package com.teleo.claims.service.mapper;

import static com.teleo.claims.domain.SinistreAsserts.*;
import static com.teleo.claims.domain.SinistreTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SinistreMapperTest {

    private SinistreMapper sinistreMapper;

    @BeforeEach
    void setUp() {
        sinistreMapper = new SinistreMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSinistreSample1();
        var actual = sinistreMapper.toEntity(sinistreMapper.toDto(expected));
        assertSinistreAllPropertiesEquals(expected, actual);
    }
}
