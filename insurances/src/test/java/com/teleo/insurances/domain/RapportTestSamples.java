package com.teleo.insurances.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RapportTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Rapport getRapportSample1() {
        return new Rapport().id(1L).titre("titre1").description("description1").url("url1");
    }

    public static Rapport getRapportSample2() {
        return new Rapport().id(2L).titre("titre2").description("description2").url("url2");
    }

    public static Rapport getRapportRandomSampleGenerator() {
        return new Rapport()
            .id(longCount.incrementAndGet())
            .titre(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .url(UUID.randomUUID().toString());
    }
}
