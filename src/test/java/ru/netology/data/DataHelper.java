package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.Value;
import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DataHelper {

    private static String jdbcUrl = "jdbc:mysql://localhost:3306/app";
    private static String user = "app";
    private static String password = "pass";

    private DataHelper() {
    }

    @Value
    public static class AuthInfo {
        String login;
        String password;
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    public static AuthInfo getInvalidLogin() {
        Faker faker = new Faker();
        return new AuthInfo(faker.name().username(), "qwerty123");
    }

    public static AuthInfo getInvalidPassword() {
        Faker faker = new Faker();
        return new AuthInfo("vasya", faker.internet().password());
    }

    @Value
    public static class VerificationCode {
        private String code;
    }

    public static String getVerificationCode() {
        val queryAuthCodeInSQL = "SELECT code FROM auth_codes WHERE created >= DATE_SUB(NOW() , INTERVAL 1 SECOND);";
        val runner = new QueryRunner();
        try (
                val conn = DriverManager.getConnection(jdbcUrl, user, password)
        ) {
            val authCode = runner.query(conn, queryAuthCodeInSQL, new ScalarHandler<>());
            return (String) authCode;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static VerificationCode getInvalidVerificationCode() {
        Faker faker = new Faker();
        return new VerificationCode(String.valueOf(faker.random().nextInt(10000, 999999)));
    }

    public static void cleanDataBase() {
        val runner = new QueryRunner();
        val deleteUsers = "DELETE FROM users";
        val deleteCards = "DELETE FROM cards";
        val deleteAuthCodes = "DELETE FROM auth_codes";
        val deleteCardTrans = "DELETE FROM card_transactions";
        try (
                val conn = DriverManager.getConnection(jdbcUrl, user, password)
        ) {
            runner.update(conn, deleteCardTrans);
            runner.update(conn, deleteAuthCodes);
            runner.update(conn, deleteCards);
            runner.update(conn, deleteUsers);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}