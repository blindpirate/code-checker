package com.github.hcsp.calculation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        //看了别人的代码，发现扩展和维护性太差了
        List<Integer> list=new ArrayList<>();
        Collections.addAll(list, a, b,c);
        Collections.sort(list, Collections.reverseOrder());
        String order = "";

        for (int  num : list) {
            //判断是不是最后一个元素，是就不加>
            if(list.size() - 1 == list.indexOf(num)){ order+=num; }
            else { order+=num+">";}
        }
        return order;
    }

    public static void main(String[] args) {
        System.out.println(printNumbersInOrder(1, 2, 3));
        System.out.println(printNumbersInOrder(-1, 2, -3));
    }
}
