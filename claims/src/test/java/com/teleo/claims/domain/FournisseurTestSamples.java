package com.teleo.claims.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FournisseurTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Fournisseur getFournisseurSample1() {
        return new Fournisseur()
            .id(1L)
            .nom("nom1")
            .adresse("adresse1")
            .telephone("telephone1")
            .email("email1")
            .addresse("addresse1")
            .ville("ville1")
            .pays("pays1")
            .servicesFournis("servicesFournis1");
    }

    public static Fournisseur getFournisseurSample2() {
        return new Fournisseur()
            .id(2L)
            .nom("nom2")
            .adresse("adresse2")
            .telephone("telephone2")
            .email("email2")
            .addresse("addresse2")
            .ville("ville2")
            .pays("pays2")
            .servicesFournis("servicesFournis2");
    }

    public static Fournisseur getFournisseurRandomSampleGenerator() {
        return new Fournisseur()
            .id(longCount.incrementAndGet())
            .nom(UUID.randomUUID().toString())
            .adresse(UUID.randomUUID().toString())
            .telephone(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .addresse(UUID.randomUUID().toString())
            .ville(UUID.randomUUID().toString())
            .pays(UUID.randomUUID().toString())
            .servicesFournis(UUID.randomUUID().toString());
    }
}
