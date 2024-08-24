package com.kagire.data.entity;

import com.kagire.data.misc.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Transaction {

    private String id;
    private BankAccount sender;
    private BankAccount receiver;
    private double value;
    private Currency currency;
    private LocalDateTime date;
}
