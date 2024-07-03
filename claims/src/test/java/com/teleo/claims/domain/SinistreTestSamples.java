package com.teleo.claims.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SinistreTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Sinistre getSinistreSample1() {
        return new Sinistre().id(1L).numeroSinistre("numeroSinistre1").raison("raison1").souscriptionId(1L);
    }

    public static Sinistre getSinistreSample2() {
        return new Sinistre().id(2L).numeroSinistre("numeroSinistre2").raison("raison2").souscriptionId(2L);
    }

    public static Sinistre getSinistreRandomSampleGenerator() {
        return new Sinistre()
            .id(longCount.incrementAndGet())
            .numeroSinistre(UUID.randomUUID().toString())
            .raison(UUID.randomUUID().toString())
            .souscriptionId(longCount.incrementAndGet());
    }
}
