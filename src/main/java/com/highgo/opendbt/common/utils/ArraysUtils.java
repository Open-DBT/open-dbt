package com.highgo.opendbt.common.utils;

import java.util.Arrays;

/**
 * @Description: 数组比较
 * @Title: ArraysUtils
 * @Package com.highgo.opendbt.common.utils
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/9/23 9:22
 */
public class ArraysUtils {
    private ArraysUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean equals(Object[] a, Object[] a2) {
        if (a == a2)
            return true;
        if (a == null || a2 == null)
            return false;

        int length = a.length;
        if (a2.length != length)
            return false;
        Arrays.sort(a);
        Arrays.sort(a2);
        for (int i = 0; i < length; i++) {
            Object o1 = a[i];
            Object o2 = a2[i];
            if (!(o1 == null ? o2 == null : o1.equals(o2)))
                return false;
        }

        return true;
    }

    public static boolean equalsIgnoreCase(String[] a, String[] a2) {
        if (a == a2)
            return true;
        if (a == null || a2 == null)
            return false;

        int length = a.length;
        if (a2.length != length)
            return false;

        for (int i = 0; i < length; i++) {
            String o1 = a[i];
            String o2 = a2[i];
            if (!(o1 == null ? o2 == null : o1.equalsIgnoreCase(o2)))
                return false;
        }
        return true;
    }

    /**
     * @description: 多选题比较是否半对
     * @author:
     * @date: 2022/9/23 16:58
     * @param: [a 正确答案, a2 作答答案]
     * @return: boolean
     **/
    public static boolean contains(Object[] a, Object[] a2) {

        if (a == a2)
            return true;
        if (a == null || a2 == null)
            return false;
        for (int i = 0; i < a2.length; i++) {

            Object o2 = a2[i];
            if (Arrays.asList(a).contains(o2)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @description: 填空题是否半对
     * @author:
     * @date: 2022/9/23 16:59
     * @param: [a, a2]
     * @return: boolean
     **/
    public static boolean containsFill(String[] a, String[] a2) {

        if (a == a2)
            return true;
        if (a == null || a2 == null)
            return false;
        for (int i = 0; i < a.length; i++) {
            Object o1 = a[i];
            Object o2 = a2[i];
            if (o1.equals(o2)) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsFillIgnoreCase(String[] a, String[] a2) {

        if (a == a2)
            return true;
        if (a == null || a2 == null)
            return false;
        for (int i = 0; i < a.length; i++) {
            String o1 = a[i];
            String o2 = a2[i];
            if (o1.equalsIgnoreCase(o2)) {
                return true;
            }
        }
        return false;
    }
}
