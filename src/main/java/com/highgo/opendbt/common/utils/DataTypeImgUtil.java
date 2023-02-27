package com.highgo.opendbt.common.utils;

/**
 * @Description: 根据数据类型转换相应图片名称
 * @Title: DataTypeImgUtil
 * @Package com.highgo.opendbt.common.utils
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/9/23 9:22
 */
public class DataTypeImgUtil {

    public static String getDataTypeImgUrl(String dataType) throws Exception {
        StringBuffer imgUrl = new StringBuffer("/dataTypeImg/");

        switch (dataType) {
            case "bool":
            case "boolean":
                imgUrl.append("boolean.png");
                break;
            case "bit":
            case "smallint":
            case "int2":
            case "int":
            case "integer":
            case "serial":
            case "int4":
            case "bigint":
            case "int8":
            case "oid":
            case "bigserial":
            case "real":
            case "double precision":
            case "float8":
            case "numeric":
            case "binary_float":
            case "binary_double":
            case "float4":
            case "money":
                imgUrl.append("number.png");
                break;
            case "binary":
            case "bytea":
            case "blob":
            case "oracle.blob":
                imgUrl.append("binary.png");
                break;
            case "text":
            case "clob":
            case "oracle.clob":
            case "varchar":
            case "varchar2":
            case "nvarchar2":
            case "character varying":
            case "character":
            case "char":
            case "bpchar":
                imgUrl.append("string.png");
                break;
            case "date":
            case "timestamp":
            case "timestamptz":
            case "time":
            case "timetz":
            case "time with time zone":
            case "time without time zone":
            case "timestamp with time zone":
            case "timestamp without time zone":
                imgUrl.append("datetime.png");
                break;
            case "xml":
                imgUrl.append("xml.png");
                break;
            case "_time with time zone":
            case "_time without time zone":
            case "_timestamp with time zone":
            case "_timestamp without time zone":
            case "geometry":
                imgUrl.append("object.png");
                break;
            default:
                imgUrl.append("object.png");
                break;
        }

        return imgUrl.toString();
    }

}
