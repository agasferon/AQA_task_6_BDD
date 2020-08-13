package ru.netology.web.data;

import lombok.Value;

public class DataHelper {
  private DataHelper() {}

  @Value
  public static class AuthInfo {
    private String login;
    private String password;
  }

  public static AuthInfo getAuthInfo() {
    return new AuthInfo("vasya", "qwerty123");
  }

  @Value
  public static class VerificationCode {
    private String code;
  }

  public static VerificationCode getVerificationCodeFor(AuthInfo authInfo) {
    return new VerificationCode("12345");
  }

  public static String getCard1Number(){
    return "5559 0000 0000 0001";
  }

  public static String getCard2Number(){
    return "5559 0000 0000 0002";
  }

  public static String getInvalidCardNumber(){
    return "4276 4000 0000 6666";
  }

}