package com.highgo.opendbt.common.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 将源List按照指定元素数量拆分为多个List
 * @Title: AverageAssign
 * @Package com.highgo.opendbt.common.utils
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/11/3 17:10
 */
public class DetachList {

    /**
     * 将源List按照指定元素数量拆分为多个List
     *
     * @param source       源List
     * @param splitItemNum 每个List中元素数量
     */
    public static <T> List<List<T>> averageAssign(List<T> source, int splitItemNum) {
        List<List<T>> result = new ArrayList<List<T>>();

        if (source != null && source.size() > 0 && splitItemNum > 0) {
            if (source.size() <= splitItemNum) {
                // 源List元素数量小于等于目标分组数量
                result.add(source);
            } else {
                // 计算拆分后list数量
                int splitNum = (source.size() % splitItemNum == 0) ? (source.size() / splitItemNum) : (source.size() / splitItemNum + 1);

                List<T> value = null;
                for (int i = 0; i < splitNum; i++) {
                    if (i < splitNum - 1) {
                        value = source.subList(i * splitItemNum, (i + 1) * splitItemNum);
                    } else {
                        // 最后一组
                        value = source.subList(i * splitItemNum, source.size());
                    }
                    result.add(value);
                }
            }
        }
        return result;
    }
}
