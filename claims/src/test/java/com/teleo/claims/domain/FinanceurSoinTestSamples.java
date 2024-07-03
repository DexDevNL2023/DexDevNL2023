package com.teleo.claims.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FinanceurSoinTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static FinanceurSoin getFinanceurSoinSample1() {
        return new FinanceurSoin()
            .id(1L)
            .nom("nom1")
            .adresse("adresse1")
            .telephone("telephone1")
            .email("email1")
            .servicesFournis("servicesFournis1");
    }

    public static FinanceurSoin getFinanceurSoinSample2() {
        return new FinanceurSoin()
            .id(2L)
            .nom("nom2")
            .adresse("adresse2")
            .telephone("telephone2")
            .email("email2")
            .servicesFournis("servicesFournis2");
    }

    public static FinanceurSoin getFinanceurSoinRandomSampleGenerator() {
        return new FinanceurSoin()
            .id(longCount.incrementAndGet())
            .nom(UUID.randomUUID().toString())
            .adresse(UUID.randomUUID().toString())
            .telephone(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .servicesFournis(UUID.randomUUID().toString());
    }
}
