package com.kagire.data.misc;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Currency {
    BYN(1),
    USD(0.3),
    EUR(0.28);

    @Getter
    private final double rate;

    public double exchangeRate(Currency to) {
        return 1 / this.rate * to.rate;
    }
}
