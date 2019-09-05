package com.yhw.constant.enumeration.selenium;

public enum MouseOperationEnum {

    CLICK("click"),
    DOUBLECLICK("doubleClick"),
    RIGHTCLICK("contextClick"),
    MOUSEDOWN("clickAndHold"),  //按下鼠标，不松开
    MOVETOELEMENT("moveToElement"), //悬停鼠标
    DRAGANDDROP("dragAndDrop"); //拖拽

    private String methodName;

    MouseOperationEnum(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodName() {
        return methodName;
    }
}
