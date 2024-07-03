package com.teleo.insurances.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SouscriptionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Souscription getSouscriptionSample1() {
        return new Souscription().id(1L).numeroSouscription("numeroSouscription1").utilisateurId(1L);
    }

    public static Souscription getSouscriptionSample2() {
        return new Souscription().id(2L).numeroSouscription("numeroSouscription2").utilisateurId(2L);
    }

    public static Souscription getSouscriptionRandomSampleGenerator() {
        return new Souscription()
            .id(longCount.incrementAndGet())
            .numeroSouscription(UUID.randomUUID().toString())
            .utilisateurId(longCount.incrementAndGet());
    }
}
