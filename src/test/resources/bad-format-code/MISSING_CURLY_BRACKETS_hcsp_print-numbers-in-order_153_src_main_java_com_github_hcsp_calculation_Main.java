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
        /*int min = a < b ? (a > c ? c : a) : (c < b ? c : a);
        int max = a > b ? (a > c ? a : b) : (b > c ? b : c);
        int mid = 0;
        if (a > min && a < max) {
            mid = a;
        } else if (b > min && b < max) {
            mid = b;
        } else {
            mid = c;
        }
        return max + " > " + mid + " > " + min;*/

        int[] help = new int[]{a, b, c};
        for (int i = 1; i < help.length; i++) {
            for (int j = i; j > 0 && help[j] > help[j - 1]; j--) {
                swap(help, j, j - 1);
            }
        }
        return help[0] + ">" + help[1] + ">" + help[2];
    }

    public static void swap(int[] arr, int i, int j) {
        if (i > arr.length - 1 || j > arr.length - 1 || i == j)
            return;

        arr[i] = arr[i] ^ arr[j];
        arr[j] = arr[i] ^ arr[j];
        arr[i] = arr[j] ^ arr[i];
    }

    public static void main(String[] args) {
        System.out.println(printNumbersInOrder(1, 2, 3));
        System.out.println(printNumbersInOrder(-1, 2, -3));
    }
}
