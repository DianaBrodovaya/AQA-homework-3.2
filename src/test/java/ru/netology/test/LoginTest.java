package ru.netology.test;

import lombok.val;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;

public class LoginTest {

    @BeforeEach
    public void openPage() {
        open("http://localhost:9999");
    }

    @AfterAll
    public static void CleanAllTables() {
        DataHelper.cleanDataBase();
    }

    @Test
    public void shouldBeLogin() {
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCode();
        val dashboardPage = verificationPage.validVerify(verificationCode);
        dashboardPage.checkPage();
    }

    @Test
    public void shouldNotLoginWithInvalidUser() {
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getInvalidLogin();
        loginPage.invalidLogin(authInfo);
    }

    @Test
    public void shouldNotLoginWithInvalidPassword() {
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getInvalidPassword();
        loginPage.invalidLogin(authInfo);
    }

    @Test
    public void shouldNotLoginWithInvalidVerificationCode() {
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getInvalidVerificationCode();
        verificationPage.invalidVerify(verificationCode.getCode());
    }

    @Test
    public void shouldBlockUser() {
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getInvalidPassword();
        loginPage.invalidLogin(authInfo);
        loginPage.cleanLoginPageFields();
        loginPage.invalidLogin(authInfo);
        loginPage.cleanLoginPageFields();
        loginPage.invalidLogin(authInfo);
        loginPage.userLockedMessages();
    }
}