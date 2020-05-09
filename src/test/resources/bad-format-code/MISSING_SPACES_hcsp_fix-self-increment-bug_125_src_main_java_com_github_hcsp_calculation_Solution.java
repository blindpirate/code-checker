package com.github.hcsp.calculation;

public class Solution {
    public static void main(String[] args) {
        printNumberTo10(9);
    }

    // 打印从number开始到10的数字
    // 例如，number=6，则打印6,7,8,9,10
    // 例如，number=9，则打印9,10
    // 现在输出的结果和预期不符，请修复此问题
    public static void printNumberTo10(int number) {
        while (true) {
            if (number <= 10) {
                System.out.println(number++);
            }
            else{return;}
        }
    }
}
