package com.teleo.claims.service.mapper;

import static com.teleo.claims.domain.PrestationSoinAsserts.*;
import static com.teleo.claims.domain.PrestationSoinTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PrestationSoinMapperTest {

    private PrestationSoinMapper prestationSoinMapper;

    @BeforeEach
    void setUp() {
        prestationSoinMapper = new PrestationSoinMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPrestationSoinSample1();
        var actual = prestationSoinMapper.toEntity(prestationSoinMapper.toDto(expected));
        assertPrestationSoinAllPropertiesEquals(expected, actual);
    }
}
