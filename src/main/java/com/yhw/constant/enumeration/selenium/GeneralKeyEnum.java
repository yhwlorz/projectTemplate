package com.yhw.constant.Enum.selenium;

public enum GeneralKeyEnum {

    A("a"),
    C("c"),
    V("v"),
    X("x"),
    F("f");
    String generalKey;

    GeneralKeyEnum(String generalKey) {
        this.generalKey = generalKey;
    }

    public String getGeneralKey() {
        return generalKey;
    }
}
