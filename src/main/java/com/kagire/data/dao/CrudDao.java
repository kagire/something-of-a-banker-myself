package com.kagire.data.dao;

import com.kagire.data.connection.ConnectionHolder;
import com.kagire.util.Pair;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class CrudDao<EntityType> {

    protected abstract String relationName();

    protected abstract EntityType fromResultSet(ResultSet resultSet) throws Exception;

    public List<EntityType> getAll(Pair<?, ?> ...parameters) {
        return ConnectionHolder
            .executeQuery(String.format("SELECT * FROM %s %s", relationName(), whereClause(parameters)))
            .map(this::toEntityList)
            .orElse(List.of());
    }

    public EntityType get(Pair<?, ?> ...parameters) {
        return ConnectionHolder
            .executeQuery(String.format("SELECT * FROM %s %s", relationName(), whereClause(parameters)))
            .map(this::toEntity)
            .orElse(null);
    }

    public int update(List<Pair<?, ?>> values, Pair<?, ?> ...parameters) {

        if (values == null || values.isEmpty())
            throw new RuntimeException("cannot execute update");

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < values.size(); i++) {

            Object field = values.get(i).firstValue();
            Object value = values.get(i).secondValue();

            builder
                .append(field)
                .append(" = ")
                .append(value instanceof Number ? value : "'" + value + "'");

            if (i < values.size() - 2) builder.append(", ");
        }

        return ConnectionHolder
            .executeUpdate(String.format("UPDATE %s SET %s %s", relationName(), builder, whereClause(parameters)))
            .orElse(0);
    }

    public int save(List<Pair<?, ?>> values) {

        if (values == null || values.isEmpty())
            throw new RuntimeException("cannot execute update");

        StringBuilder fieldBuilder = new StringBuilder();
        StringBuilder valueBuilder = new StringBuilder();
        for (int i = 0; i < values.size(); i++) {
            fieldBuilder.append(values.get(i).firstValue());
            valueBuilder.append(toSQLQueryValue(values.get(i).secondValue()));

            if (i < values.size() - 1) {
                fieldBuilder.append(", ");
                valueBuilder.append(", ");
            }
        }

        return ConnectionHolder
            .executeUpdate(String.format("INSERT INTO %s (%s) VALUES (%s)", relationName(), fieldBuilder, valueBuilder))
            .orElse(0);
    }

    public int delete(Pair<?, ?> ...parameters) {
        return ConnectionHolder
            .executeUpdate(String.format("DELETE FROM %s %s", relationName(), whereClause(parameters)))
            .orElse(0);
    }

    protected Optional<ResultSet> customQuery(String query) {
        return ConnectionHolder.executeQuery(query);
    }

    private String whereClause(Pair<?, ?> ...parameters) {

        StringBuilder builder = new StringBuilder();

        if (parameters != null && parameters.length > 0) {

            builder.append(" WHERE ");
            for (int i = 0; i < parameters.length; i++) {

                Object field = parameters[i].firstValue();
                Object value = parameters[i].secondValue();

                builder
                    .append(field)
                    .append(" = ")
                    .append(toSQLQueryValue(value));

                if (i < parameters.length - 1) builder.append(" AND ");
            }
        }

        return builder.toString();
    }

    protected EntityType toEntity(ResultSet resultSet) {
        try {
            if (resultSet.next())
                return fromResultSet(resultSet);
            else
                return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected List<EntityType> toEntityList(ResultSet resultSet) {
        List<EntityType> entities = new ArrayList<>();

        try {
            while (true) {
                var entity = toEntity(resultSet);

                if (entity != null)
                    entities.add(entity);
                else
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return entities;
    }

    protected String toSQLQueryValue(Object value) {
        if (value instanceof Number)
            return value.toString();
        if (value instanceof Enum<?> e)
            return "'" + e.name() + "'";

        return "'" + value + "'";
    }
}
