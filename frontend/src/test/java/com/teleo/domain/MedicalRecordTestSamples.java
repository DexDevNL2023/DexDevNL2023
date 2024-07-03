package com.teleo.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MedicalRecordTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MedicalRecord getMedicalRecordSample1() {
        return new MedicalRecord().id(1L).numMedicalRecord("numMedicalRecord1").details("details1");
    }

    public static MedicalRecord getMedicalRecordSample2() {
        return new MedicalRecord().id(2L).numMedicalRecord("numMedicalRecord2").details("details2");
    }

    public static MedicalRecord getMedicalRecordRandomSampleGenerator() {
        return new MedicalRecord()
            .id(longCount.incrementAndGet())
            .numMedicalRecord(UUID.randomUUID().toString())
            .details(UUID.randomUUID().toString());
    }
}
