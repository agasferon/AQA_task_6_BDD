package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
  private ElementsCollection cards = $$(".list__item");
  private final String balanceStart = "баланс: ";
  private final String balanceFinish = " р.";

  private SelenideElement heading = $("[data-test-id=dashboard]");
  private SelenideElement buttonActionReload = $("[data-test-id=action-reload]");

  public DashboardPage() {
    heading.shouldBe(visible);
  }

  public void openTransaction(String fourDigitCardNumber) {
    cards.findBy(Condition.text(fourDigitCardNumber)).$(withText("Пополнить")).click();
    $(withText("Пополнение карты")).shouldBe(visible);
  }

  public void actionReload() {
    heading.waitUntil(Condition.visible, 15000);
    buttonActionReload.click();
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

}