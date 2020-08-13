package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;

import static com.codeborne.selenide.Condition.disabled;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
  private ElementsCollection cards = $$(".list__item");
  private final String balanceStart = "баланс: ";
  private final String balanceFinish = " р.";

  private SelenideElement heading = $("[data-test-id=dashboard]");
  private SelenideElement fieldAmount = $("[data-test-id=amount] input");
  private SelenideElement fieldCardFrom = $("[data-test-id=from] input");
  private SelenideElement fieldCardTo = $("[data-test-id=to] input");
  private SelenideElement buttonActionTransfer = $("[data-test-id=action-transfer]");
  private SelenideElement buttonActionReload = $("[data-test-id=action-reload]");
  private SelenideElement errorNotification = $("[data-test-id='error-notification'] .notification__content");

  public DashboardPage() {
    heading.shouldBe(visible);
  }

  private void openTransaction(String fourDigitCardNumber) {
    cards.findBy(Condition.text(fourDigitCardNumber)).$(withText("Пополнить")).click();
    $(withText("Пополнение карты")).shouldBe(visible);
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

  public void actionReload() {
    heading.waitUntil(Condition.visible, 15000);
    buttonActionReload.click();
  }

  public void actionDeposit(String fourDigitCardNumberTo, String cardNumberFrom, int amount) {
    openTransaction(fourDigitCardNumberTo);
    setAmount(amount);
    setCardNumberFrom(cardNumberFrom);
    clickTransfer();
  }

  private int extractBalance(String text) {
    val start = text.indexOf(balanceStart);
    val finish = text.indexOf(balanceFinish);
    val value = text.substring(start + balanceStart.length(), finish);
    return Integer.parseInt(value);
  }

  public int getCard1Balance() {
    val text = cards.first().text();
    return extractBalance(text);
  }

  public int getCard2Balance() {
    val text = cards.last().text();
    return extractBalance(text);
  }

  public String showErrorNotification() {
    errorNotification.shouldBe(visible);
    return errorNotification.getText();
  }
}
