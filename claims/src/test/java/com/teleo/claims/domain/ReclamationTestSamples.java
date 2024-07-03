package com.teleo.claims.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ReclamationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Reclamation getReclamationSample1() {
        return new Reclamation().id(1L).numeroReclamation("numeroReclamation1").description("description1");
    }

    public static Reclamation getReclamationSample2() {
        return new Reclamation().id(2L).numeroReclamation("numeroReclamation2").description("description2");
    }

    public static Reclamation getReclamationRandomSampleGenerator() {
        return new Reclamation()
            .id(longCount.incrementAndGet())
            .numeroReclamation(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
