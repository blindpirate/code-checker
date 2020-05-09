package com.github.hcsp.controlflow;

public class Main {
    /**
     * 判断一个给定的年份数字是不是闰年。一个年份是闰年的条件是： 如果该年份能被100整除，那当且仅当它被400整除时，它才是闰年； 否则，当它能被4整除，就是闰年。
     *
     * <p>例如，1999年不是闰年；2000年是闰年；1900年不是闰年。
     *
     * @param year 给定的年份数字
     * @return 如果该年份是闰年，返回true，否则返回false
     */
    public static boolean isLeapYear(int year) {
    	boolean leapYear = false;
    	if(year % 100 == 0) {
    		if(year % 400 == 0) leapYear = true;
    	}
    	else {
    		if(year % 4 == 0) leapYear = true;
    	}
    	return leapYear;
    }

    public static void main(String[] args) {
        System.out.println(isLeapYear(1999));
        System.out.println(isLeapYear(2000));
        System.out.println(isLeapYear(1900));
        System.out.println(isLeapYear(2004));
    }
}
