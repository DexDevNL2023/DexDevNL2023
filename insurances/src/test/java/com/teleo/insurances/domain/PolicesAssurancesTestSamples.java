package com.teleo.insurances.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PolicesAssurancesTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PolicesAssurances getPolicesAssurancesSample1() {
        return new PolicesAssurances().id(1L).numeroPolice("numeroPolice1").description("description1");
    }

    public static PolicesAssurances getPolicesAssurancesSample2() {
        return new PolicesAssurances().id(2L).numeroPolice("numeroPolice2").description("description2");
    }

    public static PolicesAssurances getPolicesAssurancesRandomSampleGenerator() {
        return new PolicesAssurances()
            .id(longCount.incrementAndGet())
            .numeroPolice(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
