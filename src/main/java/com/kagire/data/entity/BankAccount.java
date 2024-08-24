package com.kagire.data.entity;

import com.kagire.data.misc.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BankAccount {

    private String id;
    private String name;
    private Bank bank;
    private Client client;

    private double value;
    private Currency currency;
}
