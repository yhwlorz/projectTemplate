package com.yhw.constant.enumeration.normal;

public enum ColorEnum {
    RED("red"),
    YELLOW("yellow"),
    BLACK("black"),
    BLUE("blue"),
    GREEN("green");

    private String color;

    ColorEnum(String color){
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}

