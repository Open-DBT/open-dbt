package com.highgo.opendbt.common.batchOperation;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * @Description: 批量插入更新
 * @Title: InsertOrUpdateBath
 * @Package com.highgo.opendbt.common.batchOperation
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/11/3 15:23
 */
public abstract class InsertOrUpdateBathAbstract extends AbstractMethod {
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        final  SqlSource sqlSource = prepareSqlSource(tableInfo, modelClass);
        KeyGenerator keyGenerator = new NoKeyGenerator();
        SqlMethod sqlMethod = SqlMethod.INSERT_ONE;
        String keyProperty = null;
        String keyColumn = null;
        // 表包含主键处理逻辑,如果不包含主键当普通字段处理
        if (StringUtils.isNotEmpty(tableInfo.getKeyProperty())) {
            if (tableInfo.getIdType() == IdType.AUTO) {
                /* 自增主键 */
                keyGenerator = new Jdbc3KeyGenerator();
                keyProperty = tableInfo.getKeyProperty();
                keyColumn = tableInfo.getKeyColumn();
            } else {
                if (null != tableInfo.getKeySequence()) {
                    keyGenerator = TableInfoHelper.genKeyGenerator(sqlMethod.getMethod(),tableInfo, builderAssistant);
                    keyProperty = tableInfo.getKeyProperty();
                    keyColumn = tableInfo.getKeyColumn();
                }
            }
        }
        // 第三个参数必须和RootMapper的自定义方法名一致
        return this.addInsertMappedStatement(mapperClass, modelClass, prepareInsertOrUpdateBathName(), sqlSource, keyGenerator, keyProperty, keyColumn);

    }

    protected abstract SqlSource prepareSqlSource(TableInfo tableInfo, Class<?> modelClass);

    protected abstract String prepareInsertOrUpdateBathName();

}
