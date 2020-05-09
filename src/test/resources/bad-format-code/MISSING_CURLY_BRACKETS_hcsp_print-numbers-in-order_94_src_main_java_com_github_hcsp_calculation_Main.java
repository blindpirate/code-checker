package com.github.hcsp.calculation;

import org.omg.PortableInterceptor.INACTIVE;

import java.lang.reflect.Array;
import java.util.Arrays;

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
        int[] d = new int[]{a,b,c};
        Arrays.sort(d);


        String s= Integer.toString(d[2]);
        for(int i=1;i>=0;i--){
            if(d[i]==d[i+1])
                s = s+"="+Integer.toString(d[i]);
            else
                s = s+">"+Integer.toString(d[i]);
        }
        return s;
    }

    public static void main(String[] args) {
        System.out.println(printNumbersInOrder(1, 2, 3));
        System.out.println(printNumbersInOrder(-1, 2, -3));
    }
}
