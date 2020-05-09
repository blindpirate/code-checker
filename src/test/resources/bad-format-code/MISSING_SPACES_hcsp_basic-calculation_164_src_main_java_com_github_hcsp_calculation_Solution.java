package com.github.hcsp.calculation;

public class Solution {
    public static void main(String[] args) {
        // 结果应该是4
        System.out.println(calculate(100));
    }
    // 1.首先将该数字加1
    // 2.将上一步的结果乘以2
    // 3.将上一步的结果减3
    // 4.将上一步的结果除以4
    // 5.将上一步的结果对5取余
    // 将得到的结果返回
    public static int calculate(int number) {
        int i =number;
        i = i + 1;
        i = i - 3;
        i = i / 4;
        i = i % 5;
        return i;
        }

    }

