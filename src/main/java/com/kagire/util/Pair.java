package com.kagire.util;

public record Pair<FET, SET>(FET firstValue, SET secondValue) {

    public static <F, S> Pair<F, S> of(F firstValue, S secondValue) {
        return new Pair<>(firstValue, secondValue);
    }
}
