package com.highgo.opendbt.common.utils;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @Description: 关闭连接公共代码提取
 * @Title: CloseUtil
 * @Package com.highgo.opendbt.common.utils
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/9/23 9:22
 */
public class CloseUtil {

    static Logger logger = LoggerFactory.getLogger(CloseUtil.class);

    public static void close(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
                connection = null;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static void close(Statement statement) {
        try {
            if (statement != null) {
                statement.close();
                statement = null;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static void close(ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
                resultSet = null;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static void close(InputStream inputStream) {
        try {
            if (inputStream != null) {
                inputStream.close();
                inputStream = null;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static void close(OutputStream outputStream) {
        try {
            if (outputStream != null) {
                outputStream.close();
                outputStream = null;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static void close(ByteArrayOutputStream byteArrayOutputStream) {
        try {
            if (byteArrayOutputStream != null) {
                byteArrayOutputStream.close();
                byteArrayOutputStream = null;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static void close(Workbook workbook) {
        try {
            if (workbook != null) {
                workbook.close();
                workbook = null;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static void close(CSVWriter csvWriter) {
        try {
            if (csvWriter != null) {
                csvWriter.close();
                csvWriter = null;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static void close(CSVReader csvReader) {
        try {
            if (csvReader != null) {
                csvReader.close();
                csvReader = null;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static void close(InputStreamReader inputStreamReader) {
        try {
            if (inputStreamReader != null) {
                inputStreamReader.close();
                inputStreamReader = null;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

}
