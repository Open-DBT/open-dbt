package com.highgo.opendbt.common.batchOperation;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description: 自定义方法SQL注入器
 * @Title: CustomizedSqlInjector
 * @Package com.highgo.opendbt.common.batchOperation
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/11/3 16:21
 */
@Component
public class CustomizedSqlInjector  extends DefaultSqlInjector {
    /**
     * 如果只需增加方法，保留mybatis plus自带方法，
     * 可以先获取super.getMethodList()，再添加add
     */
    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass);
        methodList.add(new InsertBatchMethod());
        methodList.add(new PGInsertOrUpdateBath());
        return methodList;
    }

}
