package com.lihuia.flyaway.enums;

/**
 * @author lihuia.com
 * @date 2022/6/20 5:14 PM
 */

public enum BodyTypeEnum {

    JSON("json", 0),
    X_WWW_FORM_URLENCODED("x-www-form-urlencoded", 1),
    FORM_DATA("form-data", 2),
    ;

    private String desc;
    private Integer code;

    BodyTypeEnum(String desc, Integer code) {
        this.desc = desc;
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public Integer getCode() {
        return code;
    }
}
