package com.github.hcsp.controlflow;

public class Main {
    public static void main(String[] args) {
        System.out.println(factorial(0));
        System.out.println(factorial(1));
        System.out.println(factorial(2));
        System.out.println(factorial(5));
    }

    /**
     * 计算一个数的阶乘。 阶乘的定义为： 0的阶乘为1 n的阶乘为n*(n-1)*(n-2)*...*2*1
     *
     * @param n 输入数字
     * @return 该数字的阶乘
     */
    public static int factorial(int n) {
                if (n < 0) {
            System.out.println("请输入正整数");
            return -1;
        }
        if (n == 0) return 1;
        if (n > 12) {
            System.out.println("数据溢出");
            return -1;
        }
        int answer = 1;
        for (int i = 1; i <= n; i++) {
            answer = answer * i;
        }
        return answer;
}
}
