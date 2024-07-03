package com.teleo.claims.service.mapper;

import static com.teleo.claims.domain.ReclamationAsserts.*;
import static com.teleo.claims.domain.ReclamationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReclamationMapperTest {

    private ReclamationMapper reclamationMapper;

    @BeforeEach
    void setUp() {
        reclamationMapper = new ReclamationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getReclamationSample1();
        var actual = reclamationMapper.toEntity(reclamationMapper.toDto(expected));
        assertReclamationAllPropertiesEquals(expected, actual);
    }
}
