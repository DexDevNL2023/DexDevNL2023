package com.teleo.claims.service.mapper;

import static com.teleo.claims.domain.DocumentDeSinistreAsserts.*;
import static com.teleo.claims.domain.DocumentDeSinistreTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DocumentDeSinistreMapperTest {

    private DocumentDeSinistreMapper documentDeSinistreMapper;

    @BeforeEach
    void setUp() {
        documentDeSinistreMapper = new DocumentDeSinistreMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDocumentDeSinistreSample1();
        var actual = documentDeSinistreMapper.toEntity(documentDeSinistreMapper.toDto(expected));
        assertDocumentDeSinistreAllPropertiesEquals(expected, actual);
    }
}
