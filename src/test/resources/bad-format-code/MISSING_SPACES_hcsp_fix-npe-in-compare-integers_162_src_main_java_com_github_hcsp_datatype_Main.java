package com.github.hcsp.datatype;

public class Main {
    // 修复compare方法，使得main方法不再抛出空指针异常
    public static void main(String[] args) {
        System.out.println(compare(123, 456));
        System.out.println(compare(123, 123));
        System.out.println(compare(123, null));
    }

    // 比较一个int和一个Integer是否相等
    // 当且仅当它们代表的整数相等时，此方法返回true
    // 不要修改本方法参数的类型
    public static boolean compare(int a, Integer b) {
        if(b!=null){
            return b==a;
        }
        return false;
    }
}
