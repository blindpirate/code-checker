package com.github.hcsp.datatype;

import java.util.Objects;

public class Light {
    // 一盏灯可能有三种状态：
    // 亮 -> true
    // 灭 -> off
    // 未知 -> null
    Boolean on;

    public Light(Boolean on) {
        this.on = on;
    }

    // 当灯亮时返回true，灭和未知状态返回false
    public boolean isOn() {
        if (Objects.isNull(on)) {
            return false;
        } else return on;
    }

    public Boolean isOnRawValue() {
        return on;
    }
}
