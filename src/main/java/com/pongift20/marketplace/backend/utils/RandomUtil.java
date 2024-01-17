package com.pongift20.marketplace.backend.utils;

import java.security.SecureRandom;

/**
 * 랜덤값 생성 유틸
 */
public class RandomUtil {
    private static final SecureRandom secureRandom = new SecureRandom();

    /**
     * 정수형 랜덤 값 생성
     *
     * @param size 생성할 자릿수
     * @return int
     */
    public static int getRandomIntWithSize(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("size는 0보다 커야 합니다.");
        }

        int min = (int) Math.pow(10, size - 1);
        int max = (int) Math.pow(10, size) - 1;

        return secureRandom.nextInt(max - min + 1) + min;
    }
}
