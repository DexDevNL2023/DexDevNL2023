package com.teleo.insurances.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class GarantieTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Garantie getGarantieSample1() {
        return new Garantie().id(1L).description("description1");
    }

    public static Garantie getGarantieSample2() {
        return new Garantie().id(2L).description("description2");
    }

    public static Garantie getGarantieRandomSampleGenerator() {
        return new Garantie().id(longCount.incrementAndGet()).description(UUID.randomUUID().toString());
    }
}
