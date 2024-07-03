package com.teleo.claims.service.mapper;

import static com.teleo.claims.domain.FinanceurSoinAsserts.*;
import static com.teleo.claims.domain.FinanceurSoinTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FinanceurSoinMapperTest {

    private FinanceurSoinMapper financeurSoinMapper;

    @BeforeEach
    void setUp() {
        financeurSoinMapper = new FinanceurSoinMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getFinanceurSoinSample1();
        var actual = financeurSoinMapper.toEntity(financeurSoinMapper.toDto(expected));
        assertFinanceurSoinAllPropertiesEquals(expected, actual);
    }
}
