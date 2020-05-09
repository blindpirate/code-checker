package com.github.hcsp;

import com.sun.org.apache.xpath.internal.objects.XString;
public class Main {
    public static void main(String[] args) {
        System.out.println("aaa+bbb=" + add("aaa", "bbb"));
        System.out.println("cat+dog=" + add("cat", "dog"));
    }
    // Create an add(String a, String b) method to concatenate two strings
    // 在这里编写一个add(String a, String b)方法进行字符串连接
 public static String add(String a,String b){
        return a+b;
 }
}
