package com.github.hcsp.controlflow;

public class Main {
    public static void main(String[] args) {
        printNarcissisticNumber();
    }

    /**
     * 打印所有的水仙花数。水仙花数是指一个三位数，其各位数字立方和等于该数本身。
     *
     * <p>例如，153是一个水仙花数，因为1的立方+5的立方+3的立方 = 153
     *
     * <p>提示：可用除法和求余运算得到一个数字的个、十、百位上的数字。
     */
    public static void printNarcissisticNumber() {
        StringBuilder sb = new StringBuilder();
        for (int i = 100; i < 1001; i++) {
            if(isNarcissisticNumber(i)) sb.append(i).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        System.out.println(sb);
    }
    private static boolean isNarcissisticNumber(int num) {
        int single = num % 10;
        int ten = num / 10 % 10;
        int hundred = num / 100 % 10;
        return (int)(Math.pow(single,3) + Math.pow(ten,3) + Math.pow(hundred,3)) == num;
    }
}
