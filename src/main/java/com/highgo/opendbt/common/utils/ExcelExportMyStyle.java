package com.highgo.opendbt.common.utils;

import cn.afterturn.easypoi.excel.export.styler.AbstractExcelExportStyler;
import cn.afterturn.easypoi.excel.export.styler.IExcelExportStyler;
import org.apache.poi.ss.usermodel.*;

/**
 * @Description: 自定义报表导出样式，可以修改表头颜色，高度等
 * @Title: ExcelExportMyStyle
 * @Package com.highgo.opendbt.common.utils
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/11/14 13:54
 */
public class ExcelExportMyStyle extends AbstractExcelExportStyler implements IExcelExportStyler {

    public ExcelExportMyStyle(Workbook workbook) {
        super.createStyles(workbook);
    }

    @Override
    public CellStyle getTitleStyle(short color) {
        CellStyle titleStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);// 加粗
        titleStyle.setFont(font);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);// 居中
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直居中
        titleStyle.setFillForegroundColor(IndexedColors.SEA_GREEN.index);// 设置颜色
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        titleStyle.setBorderRight(BorderStyle.THIN);
        titleStyle.setWrapText(true);
        return titleStyle;
    }

    @SuppressWarnings("deprecation")
    @Override
    public CellStyle stringSeptailStyle(Workbook workbook, boolean isWarp) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setDataFormat(STRING_FORMAT);
        if (isWarp) {
            style.setWrapText(true);
        }
        return style;
    }

    @Override
    public CellStyle getHeaderStyle(short color) {
        CellStyle titleStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);// 加粗
        font.setColor(IndexedColors.SEA_GREEN.index);
        font.setFontHeightInPoints((short) 11);
        titleStyle.setFont(font);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);// 居中
        titleStyle.setFillForegroundColor(IndexedColors.WHITE.index);// 设置颜色
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直居中
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        titleStyle.setBorderRight(BorderStyle.THIN);
        titleStyle.setWrapText(true);
        return titleStyle;
    }

    @SuppressWarnings("deprecation")
    @Override
    public CellStyle stringNoneStyle(Workbook workbook, boolean isWarp) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setDataFormat(STRING_FORMAT);
        if (isWarp) {
            style.setWrapText(true);
        }
        return style;
    }
}
