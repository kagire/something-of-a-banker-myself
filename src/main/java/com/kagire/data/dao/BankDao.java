package com.kagire.data.dao;

import com.kagire.data.entity.Bank;

import java.sql.ResultSet;

public class BankDao extends CrudDao<Bank>{

    @Override
    public String relationName() {
        return "bank";
    }

    @Override
    public Bank fromResultSet(ResultSet resultSet) throws Exception {
        return new Bank(
            resultSet.getString("id"),
            resultSet.getString("name"),
            resultSet.getDouble("person_fee"),
            resultSet.getDouble("company_fee")
        );
    }
}
