package com.github.hcsp.encapsulation;

public class Cat {
    private static final Cat INVALID_CAT = new Cat("Invalid cat", -1);
    private String name;
    private int age;

    private Cat(String name, int age) {
        this.name = name;
        this.age = age;
    }

    /**
     * 创建一只猫的工厂方法。当传入的参数无效，即：
     *
     * <p>1. age小于0 2. name是空字符串或者null时
     *
     * <p>返回预先创建好的{@link #INVALID_CAT}；
     *
     * <p>否则，返回一只新创建的猫
     *
     * @param age 年龄
     * @param name 名字
     * @return 创建的猫
     */
    public static Cat newCat(String name, int age) {
        if(age<0||name==null|| "".equals(name)){
            return INVALID_CAT;
        }
        else {
            return new Cat(name,age);
        }
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}
