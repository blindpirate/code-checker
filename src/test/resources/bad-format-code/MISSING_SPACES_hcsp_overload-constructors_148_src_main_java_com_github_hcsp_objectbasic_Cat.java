package com.github.hcsp.objectbasic;

public class Cat {
    /** 猫咪的名字 */
    String name;
    /** 猫咪的年龄 */
    int age;
    /** 猫咪是否萌，true为萌，false为不萌 */
    boolean cute;

    /** 创建一只指定名字的猫，age的默认值是1，cute的默认值是true */
    public Cat(String name) {
        // 请在这里使用this()调用其他的构造器
        this(name,1,true);
    }

    /** 创建一只指定名字和年龄的猫，cute的默认值是true */
    public Cat(String name, int age) {
        // 请在这里使用this()调用其他的构造器
        this(name,age,true);
    }

    /** 创建一只指定名字、年龄和萌属性的猫 */
    public Cat(String name, int age, boolean cute) {
        this.name = name;
        this.age = age;
        this.cute = cute;
    }
}
