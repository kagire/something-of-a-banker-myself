package com.kagire.data.dao;

import com.kagire.data.entity.BankAccount;
import com.kagire.data.misc.Currency;
import com.kagire.util.Pair;

import java.sql.ResultSet;

public class BankAccountDao extends CrudDao<BankAccount>{

    @Override
    public String relationName() {
        return "bank_account";
    }

    @Override
    public BankAccount fromResultSet(ResultSet resultSet) throws Exception {

        String bankId = resultSet.getString("bank_id");
        String clientId = resultSet.getString("client_id");

        return new BankAccount(
            resultSet.getString("id"),
            resultSet.getString("name"),
            new BankDao().get(Pair.of("id", bankId)),
            new ClientDao().get(Pair.of("id", clientId)),
            resultSet.getDouble("value"),
            Currency.valueOf(resultSet.getString("currency"))
        );
    }
}
