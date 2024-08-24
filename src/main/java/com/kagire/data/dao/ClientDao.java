package com.kagire.data.dao;

import com.kagire.data.entity.Client;
import com.kagire.data.misc.ClientType;

import java.sql.ResultSet;

public class ClientDao extends CrudDao<Client>{

    @Override
    public String relationName() {
        return "client";
    }

    @Override
    public Client fromResultSet(ResultSet resultSet) throws Exception {
        return new Client(
            resultSet.getString("id"),
            resultSet.getString("name"),
            ClientType.valueOf(resultSet.getString("type"))
        );
    }
}
