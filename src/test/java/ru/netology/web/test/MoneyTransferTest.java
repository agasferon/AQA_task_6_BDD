package ru.netology.web.test;

import lombok.val;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;

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
        DashboardPage page = shouldLogin();
        int expectedCard1 = page.getCard1Balance() - amount;
        int expectedCard2 = page.getCard2Balance() + amount;
        page.actionDeposit(DataHelper.getCard2Number().substring(15), DataHelper.getCard1Number(), amount);
        page.actionReload();
        assertEquals(expectedCard2, page.getCard2Balance());
        assertEquals(expectedCard1, page.getCard1Balance());
    }

    @Test
    public void shouldSendFromCard2ToCard1() {
        int amount = 3000;
        DashboardPage page = shouldLogin();
        int expectedCard1 = page.getCard1Balance() + amount;
        int expectedCard2 = page.getCard2Balance() - amount;
        page.actionDeposit(DataHelper.getCard1Number().substring(15), DataHelper.getCard2Number(), amount);
        page.actionReload();
        assertEquals(expectedCard2, page.getCard2Balance());
        assertEquals(expectedCard1, page.getCard1Balance());
    }

    @Test
    public void shouldNotSendOverLimit() {
        DashboardPage page = shouldLogin();
        int amount = page.getCard2Balance() * 2;
        page.actionDeposit(DataHelper.getCard1Number().substring(15), DataHelper.getCard2Number(), amount);
        assertEquals("Недостаточно средств на карте!", page.showErrorNotification());
    }

    @Test
    public void shouldGetErrorIfInvalidCardNumber() {
        int amount = 3000;
        DashboardPage page = shouldLogin();
        page.actionDeposit(DataHelper.getCard1Number().substring(15), DataHelper.getInvalidCardNumber(), amount);
        assertEquals("Неверный номер карты!", page.showErrorNotification());
    }

}