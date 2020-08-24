package ru.netology.web.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.disabled;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class TransactionPage {
    private SelenideElement heading = $("[data-test-id=dashboard]");
    private SelenideElement fieldAmount = $("[data-test-id=amount] input");
    private SelenideElement fieldCardFrom = $("[data-test-id=from] input");
    private SelenideElement fieldCardTo = $("[data-test-id=to] input");
    private SelenideElement buttonActionTransfer = $("[data-test-id=action-transfer]");
    private SelenideElement errorNotification = $("[data-test-id='error-notification'] .notification__content");

    public TransactionPage() {
        heading.shouldBe(visible);
    }

    private void setAmount(int amount) {
        fieldAmount.clear();
        fieldAmount.setValue(String.valueOf(amount));
    }

    private void setCardNumberFrom(String cardNumber) {
        fieldCardFrom.setValue(cardNumber);
    }

    private void clickTransfer() {
        fieldCardTo.shouldBe(disabled);
        buttonActionTransfer.click();
    }

    public void showErrorNotification() {
        errorNotification.shouldBe(visible);
    }

    public void actionDeposit(String fourDigitCardNumberTo, String cardNumberFrom, int amount) {
        new DashboardPage().openTransaction(fourDigitCardNumberTo);
        setAmount(amount);
        setCardNumberFrom(cardNumberFrom);
        clickTransfer();
    }
}