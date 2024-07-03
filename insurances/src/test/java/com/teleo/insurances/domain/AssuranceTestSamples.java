package com.teleo.insurances.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AssuranceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Assurance getAssuranceSample1() {
        return new Assurance().id(1L).nom("nom1").description("description1").conditions("conditions1");
    }

    public static Assurance getAssuranceSample2() {
        return new Assurance().id(2L).nom("nom2").description("description2").conditions("conditions2");
    }

    public static Assurance getAssuranceRandomSampleGenerator() {
        return new Assurance()
            .id(longCount.incrementAndGet())
            .nom(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .conditions(UUID.randomUUID().toString());
    }
}
