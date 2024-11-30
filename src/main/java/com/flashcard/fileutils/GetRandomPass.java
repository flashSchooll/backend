package com.flashcard.fileutils;

import java.util.Random;
import java.util.UUID;

public class GetRandomPass {

    public static long getRandomNum() {

        Random r = new Random();
        long low = 0;
        long high = 99999;

        return r.nextLong(high - low) + low;
    }

    public static String getRandomString() {

        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
