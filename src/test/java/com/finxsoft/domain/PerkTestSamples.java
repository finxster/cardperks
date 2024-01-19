package com.finxsoft.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PerkTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Perk getPerkSample1() {
        return new Perk().id(1L).name("name1").description("description1");
    }

    public static Perk getPerkSample2() {
        return new Perk().id(2L).name("name2").description("description2");
    }

    public static Perk getPerkRandomSampleGenerator() {
        return new Perk().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).description(UUID.randomUUID().toString());
    }
}
