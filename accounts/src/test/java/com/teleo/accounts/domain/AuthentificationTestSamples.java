package com.teleo.accounts.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AuthentificationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Authentification getAuthentificationSample1() {
        return new Authentification().id(1L).ipAdresse("ipAdresse1");
    }

    public static Authentification getAuthentificationSample2() {
        return new Authentification().id(2L).ipAdresse("ipAdresse2");
    }

    public static Authentification getAuthentificationRandomSampleGenerator() {
        return new Authentification().id(longCount.incrementAndGet()).ipAdresse(UUID.randomUUID().toString());
    }
}
