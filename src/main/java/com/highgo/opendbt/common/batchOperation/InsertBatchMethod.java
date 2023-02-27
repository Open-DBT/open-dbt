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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: 批量插入的方法
 * @Title: InsertBatchMethod
 * @Package com.highgo.opendbt.common.batchOperation
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/11/3 15:16
 */
public class InsertBatchMethod extends AbstractMethod {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        final String sql = "<script>insert into %s %s values %s</script>";
        final String fieldSql = prepareFieldSql(tableInfo);
        final String valueSql = prepareValuesSql(tableInfo);
        final String sqlResult = String.format(sql, tableInfo.getTableName(), fieldSql, valueSql);
        logger.debug("sqlResult----->{}", sqlResult);
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sqlResult, modelClass);
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
        return this.addInsertMappedStatement(mapperClass, modelClass, "insertBatch", sqlSource, keyGenerator, keyProperty, keyColumn);
    }

    /**
     * @description: 拼接字段值
     * @author:
     * @date: 2022/11/3 15:20
     * @param: [tableInfo]
     * @return: java.lang.String
     **/
    private String prepareValuesSql(TableInfo tableInfo) {
        final StringBuilder valueSql = new StringBuilder();
        valueSql.append("<foreach collection=\"list\" item=\"item\" index=\"index\" open=\"(\" separator=\"),(\" close=\")\">");
        //valueSql.append("#{item.").append(tableInfo.getKeyProperty()).append("},");
        tableInfo.getFieldList().forEach(x -> valueSql.append("#{item.").append(x.getProperty()).append("},"));
        valueSql.delete(valueSql.length() - 1, valueSql.length());
        valueSql.append("</foreach>");
        return valueSql.toString();
    }

    /**
     * @description: 拼接字段
     * @author:
     * @date: 2022/11/3 15:20
     * @param: [tableInfo]
     * @return: java.lang.String
     **/
    private String prepareFieldSql(TableInfo tableInfo) {
        StringBuilder fieldSql = new StringBuilder();
        //fieldSql.append(tableInfo.getKeyColumn()).append(",");
        tableInfo.getFieldList().forEach(x -> {
            fieldSql.append(x.getColumn()).append(",");
        });
        fieldSql.delete(fieldSql.length() - 1, fieldSql.length());
        fieldSql.insert(0, "(");
        fieldSql.append(")");
        return fieldSql.toString();
    }
}
