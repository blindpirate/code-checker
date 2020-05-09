package com.github.hcsp;

import com.github.hcsp.pet.Cat;

public class Home {
    public  static Cat  cat;

    public static void main(String[] args) {
        // We want to create a new Cat, but it doesn't seem to be working
        // Think why and fix the code to make the program output "Cat(Tom)"
        // 我们想要在这里创建一只新的Cat，但是似乎没有生效
        // 思考一下为什么并修复这个问题，使得程序输出"Cat(Tom)"

        newCat();
        System.out.println(cat);
    }

    public static void newCat() {
        cat = new Cat("Tom");
        Home.cat=cat;

    }
}
