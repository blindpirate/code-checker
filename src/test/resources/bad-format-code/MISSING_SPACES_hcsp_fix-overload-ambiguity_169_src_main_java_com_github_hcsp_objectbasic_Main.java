package com.github.hcsp.objectbasic;

import java.util.HashMap;

import static java.lang.System.*;

public class Main {
    public static void main(String[] args) {
        // 请修复这里的编译错误，令实际调用的方法是print(HashMap)
        print(((HashMap)null));
    }

    public static void print(int i) {
        out.println("I'm int!");
    }

    public static void print(Integer i) {
        out.println("I'm Integer!");
    }

    public static void print(Number i) {
        out.println("I'm Number!");
    }

    public static void print(Object i) {
        out.println("I'm Object!");
    }

    public static void print(HashMap i) {
        out.println("I'm HashMap!");
    }
}
