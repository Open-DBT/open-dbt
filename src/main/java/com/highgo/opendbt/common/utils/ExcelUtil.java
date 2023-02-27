package com.highgo.opendbt.common.utils;

import au.com.bytecode.opencsv.CSVWriter;
import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.highgo.opendbt.common.exception.APIException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 读写excel
 * @Title: ExcelUtil
 * @Package com.highgo.opendbt.common.utils
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/11/14 13:54
 */
public class ExcelUtil {

    static Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

    private static final String EXCEL_XLS = "xls";
    private static final String EXCEL_XLSX = "xlsx";

    public static Workbook getWorbook(String filePath) throws Exception {
        InputStream inputStream = new FileInputStream(filePath);

        try {
            if (filePath.endsWith(EXCEL_XLS)) {
                return new HSSFWorkbook(inputStream);
            } else {
                return new XSSFWorkbook(inputStream);
            }
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            inputStream.close();
        }

    }

    public static void checkFile(String filePath) throws Exception {
        File file = new File(filePath);

        if (!file.exists()) {
            throw new Exception(Message.get("FileParsingFail", filePath));
        }

        if (!(filePath.endsWith(EXCEL_XLS) || filePath.endsWith(EXCEL_XLSX))) {
            throw new Exception(Message.get("FileNotExcelFile", filePath));
        }
    }

    public static void checkFileExists(String filePath) throws Exception {
        File file = new File(filePath);

        if (!file.exists()) {
            throw new Exception(Message.get("FileParsingFail", filePath));
        }
    }

    public static String getProjectPath() {

        File file = null;
        try {
            file = new File(ResourceUtils.getURL("classpath:").getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new APIException(e.getMessage());

        }
        String path = file.getAbsolutePath();
        try {
            path = java.net.URLDecoder.decode(path, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new APIException(e.getMessage());
        }
        String projectPath = path.substring(0, path.lastIndexOf(File.separator + "WEB-INF" + File.separator + "classes"));
        return projectPath;
    }

    public static void writeXLS(String fileName, String[] columnNameArray, List<Map<Integer, Object>> mapList){
        FileOutputStream fileOutputStream = null;
        HSSFWorkbook workbook = null;
        try {
            // 获取路径
            String folderPath = ExcelUtil.getProjectPath() + File.separator + "temp";

            // 验证是否有temp文件夹，如果没有就创建
            File folderPathFile = new File(folderPath);
            if (!folderPathFile.exists()) {
                folderPathFile.mkdir();
            }

            // 验证文件是否存在，如果存在就删除
            String filePath = folderPath + File.separator + fileName;
            File filePathFile = new File(filePath);
            if (filePathFile.exists()) {
                filePathFile.delete();
            }

            // 创建新的文件
            filePathFile.createNewFile();

            workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet();
            HSSFRow row = sheet.createRow(0);

            // 写入表格表头
            for (int i = 0; i < columnNameArray.length; i++) {
                HSSFCell cell = row.createCell(i);
                cell.setCellType(CellType.STRING);
                cell.setCellValue(columnNameArray[i]);
            }

            // 写入表格内容
            for (int i = 0; i < mapList.size(); i++) {
                row = sheet.createRow(i + 1);
                Map<Integer, Object> dataMap = mapList.get(i);
                for (int j = 0; j < columnNameArray.length; j++) {
                    HSSFCell cell = row.createCell(j);
                    cell.setCellType(CellType.STRING);
                    if (null == dataMap.get(j)) {
                        cell.setCellValue("");
                    } else {
                        cell.setCellValue(dataMap.get(j).toString());
                    }
                }
            }

            fileOutputStream = new FileOutputStream(filePath);
            workbook.write(fileOutputStream);
        } catch (Exception e) {
            throw new APIException(e.getMessage());
        } finally {
            CloseUtil.close(fileOutputStream);
            CloseUtil.close(workbook);
        }
    }

    public static void writeCSV(String fileName, String[] columnNameArray, List<Map<Integer, Object>> mapList) {
        CSVWriter csvWriter = null;
        try {
            // 获取路径
            String folderPath = ExcelUtil.getProjectPath() + File.separator + "temp" + File.separator;

            // 验证是否有temp文件夹，如果没有就创建
            File folderPathFile = new File(folderPath);
            if (!folderPathFile.exists()) {
                folderPathFile.mkdir();
            }

            // 验证文件是否存在，如果存在就删除
            String filePath = folderPath + fileName;
            File filePathFile = new File(filePath);
            if (filePathFile.exists()) {
                filePathFile.delete();
            }

            // 创建新的文件
            filePathFile.createNewFile();

            csvWriter = new CSVWriter(new OutputStreamWriter(new FileOutputStream(filePath, true), Charset.forName("UTF-8")), ',', '"', '"');

            // 写入表格表头
            csvWriter.writeNext(columnNameArray);

            // 写入表格内容
            for (int i = 0; i < mapList.size(); i++) {
                Map<Integer, Object> dataMap = mapList.get(i);

                String[] datas = new String[columnNameArray.length];

                for (int j = 0; j < columnNameArray.length; j++) {
                    if (null == dataMap.get(j)) {
                        datas[j] = "";
                    } else {
                        datas[j] = dataMap.get(j).toString();
                    }
                }

                csvWriter.writeNext(datas);
            }
        } catch (Exception e) {
            throw new APIException(e.getMessage());
        } finally {
            CloseUtil.close(csvWriter);
        }
    }

    /**
     * Excel文件导出,导出的文件名默认为:headTitle+当前系统时间
     *
     * @param request
     * @param listData  要导出的list数据
     * @param pojoClass 定义excel属性信息
     * @param headTitle Excel文件头信息
     * @param sheetName Excel文件sheet名称
     * @param response
     */
    public static void exportExcel(HttpServletRequest request, Collection<?> listData, Class<?> pojoClass, String headTitle, String sheetName, HttpServletResponse response) {
        ExportParams params = new ExportParams(headTitle, sheetName);
        params.setHeight((short) 12);
        params.setStyle(ExcelExportMyStyle.class);
        try {
            Workbook workbook = ExcelExportUtil.exportExcel(params, pojoClass, listData);
            String fileName = headTitle + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String codedFileName = java.net.URLEncoder.encode(fileName, "UTF-8");
            //设置信息头，告诉浏览器内容为excel类型
            response.setHeader("content-Type", "application/vnd.ms-excel");
            String agent = request.getHeader("USER-AGENT").toLowerCase();
            if (agent.contains("firefox")) {
                response.setCharacterEncoding("utf-8");
                response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1") + ".xls");
            } else {
                response.setHeader("Content-Disposition", "attachment;filename=" + codedFileName + ".xls");
            }
            ServletOutputStream out = response.getOutputStream();
            workbook.write(out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
