package com.github.hcsp.algorithm;

public class BinarySearch {
    public static void main(String[] args) {
        System.out.println(binarySearch(new String[] {"aaa", "ccc", "fff", "yyy", "zzz"}, "bbb"));
        System.out.println(binarySearch(new String[] {"aaa", "ccc", "fff", "yyy", "zzz"}, "yyy"));
    }

    // 给定一个按照字符串升序升序排好序的用户数组，寻找目标字符串的位置，返回其索引值
    // 如果未找到，返回-1
    // 我们鼓励你使用递归和非递归两种方式
    public static int binarySearch(String[] strings, String target) {
        if (target.compareTo(strings[0]) < 0 || target.compareTo(strings[strings.length - 1]) > 0) {
            return -1;
        }
        return binarySearchHelper(strings, target, 0, strings.length - 1);
    }

    public static int binarySearchHelper(String[] strings, String target, int start, int end) {
        int middle = (start + end) / 2;
        if (target.compareTo(strings[start]) == 0) {
            return start;
        }
        if (target.compareTo(strings[end]) == 0) {
            return end;
        }
        if (target.compareTo(strings[middle]) == 0) {
            return middle;
        } else if (end - start == 1) {
            return -1;
        }
        if (target.compareTo(strings[middle]) < 0) {
            return binarySearchHelper(strings, target, start, middle);
        } else {
            return  binarySearchHelper(strings, target, middle, end);
        }

    }
}
