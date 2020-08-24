package ru.netology.web.test;

import lombok.val;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;
import ru.netology.web.page.TransactionPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MoneyTransferTest {

    private DashboardPage shouldLogin() {
      open("http://localhost:9999");
      val loginPage = new LoginPage();
      val authInfo = DataHelper.getAuthInfo();
      val verificationPage = loginPage.validLogin(authInfo);
      val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
      return verificationPage.validVerify(verificationCode);
    }

    @Test
    public void shouldSendFromCard1ToCard2() {
        int amount = 3000;
        DashboardPage dashboardPage = shouldLogin();
        TransactionPage transactionPage = new TransactionPage();
        int expectedCard1 = dashboardPage.getCard1Balance() - amount;
        int expectedCard2 = dashboardPage.getCard2Balance() + amount;
        transactionPage.actionDeposit(DataHelper.getCard2Number().substring(15), DataHelper.getCard1Number(), amount);
        dashboardPage.actionReload();
        assertEquals(expectedCard2, dashboardPage.getCard2Balance());
        assertEquals(expectedCard1, dashboardPage.getCard1Balance());
    }

    @Test
    public void shouldSendFromCard2ToCard1() {
        int amount = 3000;
        DashboardPage dashboardPage = shouldLogin();
        TransactionPage transactionPage = new TransactionPage();
        int expectedCard1 = dashboardPage.getCard1Balance() + amount;
        int expectedCard2 = dashboardPage.getCard2Balance() - amount;
        transactionPage.actionDeposit(DataHelper.getCard1Number().substring(15), DataHelper.getCard2Number(), amount);
        dashboardPage.actionReload();
        assertEquals(expectedCard2, dashboardPage.getCard2Balance());
        assertEquals(expectedCard1, dashboardPage.getCard1Balance());
    }

    @Test
    public void shouldNotSendOverLimit() {
        DashboardPage dashboardPage = shouldLogin();
        TransactionPage transactionPage = new TransactionPage();
        int amount = dashboardPage.getCard2Balance() * 2;
        transactionPage.actionDeposit(DataHelper.getCard1Number().substring(15), DataHelper.getCard2Number(), amount);
        transactionPage.showErrorNotification();
    }

    @Test
    public void shouldGetErrorIfInvalidCardNumber() {
        int amount = 3000;
        shouldLogin();
        TransactionPage transactionPage = new TransactionPage();
        transactionPage.actionDeposit(DataHelper.getCard1Number().substring(15), DataHelper.getInvalidCardNumber(), amount);
        transactionPage.showErrorNotification();
    }

}