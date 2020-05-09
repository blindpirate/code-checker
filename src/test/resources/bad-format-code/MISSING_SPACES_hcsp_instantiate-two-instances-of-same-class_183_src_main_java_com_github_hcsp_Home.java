package com.github.hcsp;

import com.github.hcsp.pet.Cat;

public class Home {
    public static Cat black;
    public static Cat white;

    public static void main(String[] args) {
        createTwoCats();
        System.out.println(black);
        System.out.println(white);
    }

    public static void createTwoCats() {
        // Create two new Cats named "Black" and "White"
        // then assign them to "black" and "white" static variable respectively
        // 在这里创建两个Cat实例，分别赋值给"black"和"white"静态变量，然后将它们的"name"成员变量分别设置为"Black"和"White"
        black=new Cat();
        black.name="Black";
        white=new Cat();
        white.name="White";

    }
}
