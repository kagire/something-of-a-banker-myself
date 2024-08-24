package com.kagire.data.dao;

import com.kagire.data.entity.Transaction;
import com.kagire.data.misc.Currency;
import com.kagire.util.Pair;

import java.sql.ResultSet;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

public class TransactionDao extends CrudDao<Transaction>{

    @Override
    public String relationName() {
        return "transaction";
    }

    @Override
    public Transaction fromResultSet(ResultSet resultSet) throws Exception {

        String senderId = resultSet.getString("sender_id");
        String receiverId = resultSet.getString("receiver_id");
        BankAccountDao bankAccountDao = new BankAccountDao();

        return new Transaction(
            resultSet.getString("id"),
            bankAccountDao.get(Pair.of("id", senderId)),
            bankAccountDao.get(Pair.of("id", receiverId)),
            resultSet.getDouble("value"),
            Currency.valueOf(resultSet.getString("currency")),
            LocalDateTime.ofInstant(Instant.ofEpochMilli(resultSet.getTimestamp("date").getTime()), ZoneOffset.UTC)
        );
    }

    public List<Transaction> getAllIncomingByClientId(String clientId) {
        return customQuery(String.format("SELECT * FROM %s WHERE receiver_id IN (SELECT id FROM %s WHERE client_id = %s)",
            relationName(), new BankAccountDao().relationName(), toSQLQueryValue(clientId)))
            .map(this::toEntityList)
            .orElse(List.of());
    }

    public List<Transaction> getAllOutgoingByClientId(String clientId) {
        return customQuery(String.format("SELECT * FROM %s WHERE sender_id IN (SELECT id FROM %s WHERE client_id = %s)",
            relationName(), new BankAccountDao().relationName(), toSQLQueryValue(clientId)))
            .map(this::toEntityList)
            .orElse(List.of());
    }
}
