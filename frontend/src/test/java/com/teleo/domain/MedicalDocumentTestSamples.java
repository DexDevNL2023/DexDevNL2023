package com.teleo.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MedicalDocumentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MedicalDocument getMedicalDocumentSample1() {
        return new MedicalDocument().id(1L).nom("nom1").type("type1").url("url1");
    }

    public static MedicalDocument getMedicalDocumentSample2() {
        return new MedicalDocument().id(2L).nom("nom2").type("type2").url("url2");
    }

    public static MedicalDocument getMedicalDocumentRandomSampleGenerator() {
        return new MedicalDocument()
            .id(longCount.incrementAndGet())
            .nom(UUID.randomUUID().toString())
            .type(UUID.randomUUID().toString())
            .url(UUID.randomUUID().toString());
    }
}
