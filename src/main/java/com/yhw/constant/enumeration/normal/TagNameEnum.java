package com.yhw.constant.enumeration.normal;

public enum TagNameEnum {
    TRTD("tr","td"),
    ULLI("ul","li");


    String tagName1;
    String tagName2;


    TagNameEnum(String tagName1,String tagName2){
        this.tagName1 = tagName1;
        this.tagName2 = tagName2;
    }

    public String getTagName1() {
        return tagName1;
    }

    public void setTagName1(String tagName1) {
        this.tagName1 = tagName1;
    }

    public String getTagName2() {
        return tagName2;
    }

    public void setTagName2(String tagName2) {
        this.tagName2 = tagName2;
    }
}
