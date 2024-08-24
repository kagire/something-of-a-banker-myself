package com.kagire.data.connection;

import java.sql.*;
import java.util.Optional;

public class ConnectionHolder {

    private static final String jdbcUrl = "jdbc:postgresql://localhost:5432/finances";
    private static final String username = "postgres";
    private static final String password = "1234";

    private static Connection connection = null;

    static {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(jdbcUrl, username, password);

            prepareDB();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Optional<ResultSet> executeQuery(String query) {
        try {
            Statement statement = connection.createStatement();
            return Optional.ofNullable(statement.executeQuery(query));
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public static Optional<Integer> executeUpdate(String query) {
        try {
            Statement statement = connection.createStatement();
            return Optional.of(statement.executeUpdate(query));
        } catch (SQLException e) {
            throw new RuntimeException("sql problem: " + e.getMessage());
        }
    }

    private static void prepareDB() {
        executeUpdate("""
            CREATE TABLE IF NOT EXISTS bank
            (
                id character varying NOT NULL,
                name character varying NOT NULL,
                person_fee double precision NOT NULL,
                company_fee double precision NOT NULL,
                
                CONSTRAINT bank_pkey PRIMARY KEY (id),
                CONSTRAINT bank_name_unique_constraint UNIQUE (name)
            )
            """);

        executeUpdate("""
            CREATE TABLE IF NOT EXISTS client
            (
                id character varying NOT NULL,
                name character varying NOT NULL,
                type character varying NOT NULL,
                
                CONSTRAINT client_pkey PRIMARY KEY (id),
                CONSTRAINT client_name_unique_constraint UNIQUE (name)
            )
            """);

        executeUpdate("""
            CREATE TABLE IF NOT EXISTS bank_account
            (
                id character varying NOT NULL,
                bank_id character varying NOT NULL,
                client_id character varying NOT NULL,
                value double precision NOT NULL,
                currency character varying NOT NULL,
                name character varying NOT NULL,
                
                CONSTRAINT bank_account_pkey PRIMARY KEY (id),
                CONSTRAINT bank_account_name_constraint UNIQUE (name),
                CONSTRAINT bank_account_bank_fk FOREIGN KEY (bank_id)
                    REFERENCES public.bank (id) MATCH SIMPLE,
                CONSTRAINT bank_account_client_fk FOREIGN KEY (client_id)
                    REFERENCES public.client (id) MATCH SIMPLE
            )
            """);

        executeUpdate("""
            CREATE TABLE IF NOT EXISTS transaction
            (
                id character varying NOT NULL,
                sender_id character varying NOT NULL,
                receiver_id character varying NOT NULL,
                value double precision NOT NULL,
                currency character varying NOT NULL,
                date timestamp without time zone NOT NULL,
                
                CONSTRAINT transaction_pkey PRIMARY KEY (id),
                CONSTRAINT transaction_receiver_id_fk FOREIGN KEY (receiver_id)
                    REFERENCES public.bank_account (id) MATCH SIMPLE,
                CONSTRAINT transaction_sender_id_fk FOREIGN KEY (sender_id)
                    REFERENCES public.bank_account (id) MATCH SIMPLE
            )
            """);
    }
}
