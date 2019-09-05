package com.yhw.constant.Enum;

public enum DateFormatEnum {
    one ("YYYY-MM-dd HH:mm:ss"),

    over("");

    String format;

    DateFormatEnum(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }
}
