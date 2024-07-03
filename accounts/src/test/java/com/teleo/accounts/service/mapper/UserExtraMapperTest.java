package com.teleo.accounts.service.mapper;

import static com.teleo.accounts.domain.UserExtraAsserts.*;
import static com.teleo.accounts.domain.UserExtraTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserExtraMapperTest {

    private UserExtraMapper userExtraMapper;

    @BeforeEach
    void setUp() {
        userExtraMapper = new UserExtraMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUserExtraSample1();
        var actual = userExtraMapper.toEntity(userExtraMapper.toDto(expected));
        assertUserExtraAllPropertiesEquals(expected, actual);
    }
}
