package com.teleo.claims.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PrestationSoinTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PrestationSoin getPrestationSoinSample1() {
        return new PrestationSoin().id(1L).nom("nom1").description("description1");
    }

    public static PrestationSoin getPrestationSoinSample2() {
        return new PrestationSoin().id(2L).nom("nom2").description("description2");
    }

    public static PrestationSoin getPrestationSoinRandomSampleGenerator() {
        return new PrestationSoin()
            .id(longCount.incrementAndGet())
            .nom(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
