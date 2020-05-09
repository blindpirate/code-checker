package com.github.hcsp.datatype;

public class Main {
    public static void main(String[] args) {
        // 这里应该输出两个true，实际输出的却是两个false
        // 请查找、思考并修复numberEquals方法中的问题
        System.out.println(numberEquals("1234", "+1234"));
        System.out.println(numberEquals("1234", "1234"));
    }

    // 判断两个字符串是否包含相等的数字
    // 例如a为"+1234"，b为"1234"，返回true
    public static boolean numberEquals(String a, String b) {

        return Integer.valueOf(a).equals (Integer.valueOf(b));
    }
}
