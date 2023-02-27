package com.highgo.opendbt.common.batchOperation;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * @description: 自定义接口覆盖BaseMapper，解决mybatis-plus 批量操作慢的问题
 * @author:
 * @date: 2022/11/3 15:14
 * @param: null
 * @return:
 **/
public interface RootMapper<T> extends BaseMapper<T> {
    /**
     * @description: 批量插入
     * @author:
     * @date: 2022/11/3 15:13
     * @param: [list]
     * @return: int
     **/
    int insertBatch(@Param("list") List<T> list);
    /**
     * @description: 批量插入更新
     * @author:
     * @date: 2022/11/3 15:14
     * @param: [list]
     * @return: int
     **/
    int mysqlInsertOrUpdateBatch(@Param("list") List<T> list);

    int pgInsertOrUpdateBatch(@Param("list") List<T> list);
}
