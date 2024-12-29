package com.flashcard.fileutils;

import java.util.Random;
import java.util.UUID;

public class GetRandomPass {

    private static final Random random = new Random();

    public static long getRandomNum() {

        long low = 0;
        long high = 99999;

        return random.nextLong(high - low) + low;
    }

    public static String getRandomString() {

        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    private GetRandomPass() {
    }
}
