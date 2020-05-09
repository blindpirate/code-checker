package com.github.hcsp.calculation;

public class Main {
    /**
     * 给定三个大小不确定的数字a,b,c，将其按照从大到小的顺序输出。
     *
     * <p>例如，a=1,b=3,c=2，返回字符串"3>2>1"。
     *
     * <p>又如，a=-1,b=100,c=4，返回字符串"100>4>-1"
     *
     * @param a 数字一
     * @param b 数字二
     * @param c 数字三
     * @return 所要求的字符串
     */
    public static String printNumbersInOrder(int a, int b, int c) {
        if (a>b&&b>c){
            return a+">"+b+">"+c;
        }else if (a>c&&c>b){
            return a+">"+b+">"+c;
        }else if (b>a&&a>c){
            return b+">"+a+">"+c;
        }else if (b>c&&c>a){
            return b+">"+c+">"+a;
        }else if (c>a&&a>b){
            return c+">"+a+">"+b;
        }else {
            return c+">"+b+">"+a;
        }
    }

    public static void main(String[] args) {
        System.out.println(printNumbersInOrder(1, 2, 3));
        System.out.println(printNumbersInOrder(-1, 2, -3));
    }
}
