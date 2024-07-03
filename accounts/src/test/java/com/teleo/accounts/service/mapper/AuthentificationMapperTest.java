package com.teleo.accounts.service.mapper;

import static com.teleo.accounts.domain.AuthentificationAsserts.*;
import static com.teleo.accounts.domain.AuthentificationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AuthentificationMapperTest {

    private AuthentificationMapper authentificationMapper;

    @BeforeEach
    void setUp() {
        authentificationMapper = new AuthentificationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAuthentificationSample1();
        var actual = authentificationMapper.toEntity(authentificationMapper.toDto(expected));
        assertAuthentificationAllPropertiesEquals(expected, actual);
    }
}
