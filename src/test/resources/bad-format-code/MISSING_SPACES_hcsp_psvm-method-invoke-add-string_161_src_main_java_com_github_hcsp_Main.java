package com.github.hcsp;

public class Main {
    public static void main(String[] args) {
        System.out.println("aaa+bbb=" + add("aaa", "bbb"));
        System.out.println("cat+dog=" + add("cat", "dog"));
    }


    // Create an add(String a, String b) method to concatenate two strings
    // 在这里编写一个add(String a, String b)方法进行字符串连接

    private static String add(String a, String b){
     String i=a+b;

        return i;
    }
}
