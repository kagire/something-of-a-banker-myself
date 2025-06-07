package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

public class V1_2__insert_default_users extends BaseJavaMigration {

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void migrate(Context context) throws Exception {
        Connection connection = context.getConnection();
        boolean originalAutoCommit = connection.getAutoCommit();

        try {
            connection.setAutoCommit(false);

            for (int i = 1; i <= 100; i++) {
                insertUser(connection, "user" + i, LocalDate.now(),
                    String.valueOf(i * 12345678), "user" + i + "@test.com",
                    String.valueOf(i * 729384), 0.1 * i);
            }
        } catch (Exception e) {
            connection.rollback();
            e.printStackTrace();
            throw e;
        } finally {
            connection.setAutoCommit(originalAutoCommit);
        }
    }

    private void insertUser(
        Connection conn,
        String name, LocalDate dateOfBirth, String password,
        String email, String phone, double initialDeposit
    ) throws Exception {

        long userId = randomId();

        try (
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO \"USER\" (\"ID\", \"NAME\", \"DATE_OF_BIRTH\", \"PASSWORD\") VALUES (?, ?, ?, ?)")
        ) {
            ps.setLong(1, userId);
            ps.setString(2, name);
            ps.setDate(3, Date.valueOf(dateOfBirth));
            ps.setString(4, passwordEncoder.encode(password));
            ps.executeUpdate();
        }

        try (
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO \"ACCOUNT\" (\"ID\", \"USER_ID\", \"BALANCE\") VALUES (?, ?, ?)")
        ) {
            ps.setLong(1, randomId());
            ps.setLong(2, userId);
            ps.setBigDecimal(3, new BigDecimal(initialDeposit).setScale(2, RoundingMode.HALF_DOWN));
            ps.executeUpdate();
        }

        try (
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO \"PHONE_DATA\" (\"ID\", \"USER_ID\", \"PHONE\") VALUES (?, ?, ?)")
        ) {
            ps.setLong(1, randomId());
            ps.setLong(2, userId);
            ps.setString(3, phone);
            ps.executeUpdate();
        }

        try (
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO \"EMAIL_DATA\" (\"ID\", \"USER_ID\", \"EMAIL\") VALUES (?, ?, ?)")
        ) {
            ps.setLong(1, randomId());
            ps.setLong(2, userId);
            ps.setString(3, email);
            ps.executeUpdate();
        }
    }

    private long randomId() {
        return ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE);
    }
}