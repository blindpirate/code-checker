package com.github.hcsp.controlflow;

public class Main {
    public static void main(String[] args) {
        System.out.println(howManyPrimeNumbers(100));
    }

    /**
     * 给定一个数字n，返回1到n之间的质数（素数）个数，不包括n本身。
     *
     * <p>例如，n=5，返回2，因为1到5之间有2个质数：2和3。
     *
     * <p>提示：对于正整数n，如果用2到Math.sqrt(n)+1之间的所有整数去除，均无法整除，则n为质数。
     *
     * @param n 给定的数字
     * @return 1到n之间(不包括n)质数的个数
     */
    public static int howManyPrimeNumbers(int n) {
        int flag=0;
        int num=0;
        for(int i=3;i<n;++i){
            flag=0;
            for(int x=2;x<=(int)Math.sqrt(i)+1;++x){
                if(i%x!=0){++flag;}
                else{break;}
            }
            if(flag==(int)Math.sqrt(i)){
                ++num;
                //System.out.print(i+" ");
            }
        }
        return num+1;
    }
}
