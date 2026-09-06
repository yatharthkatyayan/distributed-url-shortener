package com.yatharth.distributedurlshortener.util;

public final class Base62Encoder {

    private static final String CHARACTERS =
            "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final int BASE = CHARACTERS.length();

    private Base62Encoder() {
    }

    public static String encode(long number) {

        if (number == 0) {
            return "0";
        }

        StringBuilder result = new StringBuilder();

        while (number > 0) {
            int remainder = (int) (number % BASE);
            result.append(CHARACTERS.charAt(remainder));
            number /= BASE;
        }

        return result.reverse().toString();
    }
}