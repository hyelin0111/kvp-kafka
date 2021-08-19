package com.kvp.domain;

import java.util.Arrays;

public enum Grade {
    BRONZE(0L, 100_000L),
    SILVER(100_000L, 200_000L),
    GOLD(200_000L, 500_000L),
    PLATINUM(500_000L, 1_000_000L),
    VIP(1_000_000L, Long.MAX_VALUE);

    private final Long min_price;
    private final Long max_price;

    Grade(Long min_price, Long max_price) {
        this.min_price = min_price;
        this.max_price = max_price;
    }

    public static Grade getRank(Long price) {
        return Arrays.stream(values())
                .filter(grade -> price >= grade.min_price && price < grade.max_price)
                .findFirst()
                .orElse(BRONZE);
    }
}
