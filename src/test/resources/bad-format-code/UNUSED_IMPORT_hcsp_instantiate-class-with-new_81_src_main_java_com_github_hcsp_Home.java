package com.github.hcsp;

import com.github.hcsp.pet.Cat;


public class Home {
    private static Cat cat;

    public static void main(String[] args) {
        System.out.println(cat);
        cat = newCat();
        System.out.println(cat);
        new Cat();
    }

    public static Cat newCat() {
        return new Cat();
    }

        // Create a new Cat instance and return it here
        // 在这里创建一个新的Cat实例并返回之
}

