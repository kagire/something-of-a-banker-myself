package com.kagire.data.entity;

import com.kagire.data.misc.ClientType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Bank {

    private String id;
    private String name;
    private double personFee;
    private double companyFee;

    public double fee(ClientType forClient) {
        return switch (forClient) {
            case PERSON -> personFee;
            case COMPANY -> companyFee;
        };
    }
}
