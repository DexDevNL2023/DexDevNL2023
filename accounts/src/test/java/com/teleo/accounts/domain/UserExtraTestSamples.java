package com.teleo.accounts.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserExtraTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserExtra getUserExtraSample1() {
        return new UserExtra()
            .id(1L)
            .numNiu("numNiu1")
            .nom("nom1")
            .prenom("prenom1")
            .email("email1")
            .motDePasse("motDePasse1")
            .numCni("numCni1")
            .telephone("telephone1")
            .addresse("addresse1")
            .ville("ville1")
            .pays("pays1");
    }

    public static UserExtra getUserExtraSample2() {
        return new UserExtra()
            .id(2L)
            .numNiu("numNiu2")
            .nom("nom2")
            .prenom("prenom2")
            .email("email2")
            .motDePasse("motDePasse2")
            .numCni("numCni2")
            .telephone("telephone2")
            .addresse("addresse2")
            .ville("ville2")
            .pays("pays2");
    }

    public static UserExtra getUserExtraRandomSampleGenerator() {
        return new UserExtra()
            .id(longCount.incrementAndGet())
            .numNiu(UUID.randomUUID().toString())
            .nom(UUID.randomUUID().toString())
            .prenom(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .motDePasse(UUID.randomUUID().toString())
            .numCni(UUID.randomUUID().toString())
            .telephone(UUID.randomUUID().toString())
            .addresse(UUID.randomUUID().toString())
            .ville(UUID.randomUUID().toString())
            .pays(UUID.randomUUID().toString());
    }
}
