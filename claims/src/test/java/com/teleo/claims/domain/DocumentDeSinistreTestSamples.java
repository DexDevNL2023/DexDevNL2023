package com.teleo.claims.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DocumentDeSinistreTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static DocumentDeSinistre getDocumentDeSinistreSample1() {
        return new DocumentDeSinistre().id(1L).nom("nom1").type("type1").url("url1");
    }

    public static DocumentDeSinistre getDocumentDeSinistreSample2() {
        return new DocumentDeSinistre().id(2L).nom("nom2").type("type2").url("url2");
    }

    public static DocumentDeSinistre getDocumentDeSinistreRandomSampleGenerator() {
        return new DocumentDeSinistre()
            .id(longCount.incrementAndGet())
            .nom(UUID.randomUUID().toString())
            .type(UUID.randomUUID().toString())
            .url(UUID.randomUUID().toString());
    }
}
