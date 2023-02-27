package com.highgo.opendbt.common.batchOperation;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.SqlSource;
import org.springframework.util.StringUtils;

/**
 * @Description: 批量插入更新
 * @Title: InsertOrUpdateBath
 * @Package com.highgo.opendbt.common.batchOperation
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/11/3 15:23
 */
public class MysqlInsertOrUpdateBath extends InsertOrUpdateBathAbstract {

    @Override
    protected SqlSource prepareSqlSource(TableInfo tableInfo, Class<?> modelClass) {
        final String sql = "<script>insert into %s %s values %s ON DUPLICATE KEY UPDATE %s</script>";
        final String tableName = tableInfo.getTableName();
        final String filedSql = prepareFieldSql(tableInfo);
        final String modelValuesSql = prepareModelValuesSql(tableInfo);
        final String duplicateKeySql = prepareDuplicateKeySql(tableInfo);
        final String sqlResult = String.format(sql, tableName, filedSql, modelValuesSql, filedSql, duplicateKeySql);
        //String.format(sql, tableName, filedSql, modelValuesSql, duplicateKeySql);
        //System.out.println("savaorupdatesqlsql="+sqlResult);
        return languageDriver.createSqlSource(configuration, sqlResult, modelClass);
    }


    @Override
    protected String prepareInsertOrUpdateBathName() {
        return "mysqlInsertOrUpdateBath";
    }

    String prepareDuplicateKeySql(TableInfo tableInfo) {
        final StringBuilder duplicateKeySql = new StringBuilder();
        if (!StringUtils.isEmpty(tableInfo.getKeyColumn())) {
            duplicateKeySql.append(tableInfo.getKeyColumn()).append("=values(").append(tableInfo.getKeyColumn()).append("),");
        }

        tableInfo.getFieldList().forEach(x -> {
            duplicateKeySql.append(x.getColumn())
                    .append("=values(")
                    .append(x.getColumn())
                    .append("),");
        });
        duplicateKeySql.delete(duplicateKeySql.length() - 1, duplicateKeySql.length());
        return duplicateKeySql.toString();
    }

    String prepareModelValuesSql(TableInfo tableInfo) {
        final StringBuilder valueSql = new StringBuilder();
        valueSql.append("<foreach collection=\"list\" item=\"item\" index=\"index\" open=\"(\" separator=\"),(\" close=\")\">");
        if (!StringUtils.isEmpty(tableInfo.getKeyProperty())) {
            valueSql.append("#{item.").append(tableInfo.getKeyProperty()).append("},");
        }
        tableInfo.getFieldList().forEach(x -> valueSql.append("#{item.").append(x.getProperty()).append("},"));
        valueSql.delete(valueSql.length() - 1, valueSql.length());
        valueSql.append("</foreach>");
        return valueSql.toString();
    }

    /**
     * @description:准备属性名
     * @author:
     * @date: 2022/11/3 15:25
     * @param: [tableInfo]
     * @return: java.lang.String
     **/
    String prepareFieldSql(TableInfo tableInfo) {
        StringBuilder fieldSql = new StringBuilder();
        fieldSql.append(tableInfo.getKeyColumn()).append(",");
        tableInfo.getFieldList().forEach(x -> {
            fieldSql.append(x.getColumn()).append(",");
        });
        fieldSql.delete(fieldSql.length() - 1, fieldSql.length());
        fieldSql.insert(0, "(");
        fieldSql.append(")");
        return fieldSql.toString();
    }
}
