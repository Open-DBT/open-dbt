
package com.highgo.opendbt.common.utils;
import lombok.extern.slf4j.Slf4j;
import java.io.File;
/**
 * <p>
 * 文件转换工具类$
 * </p>
 *
 * @author
 * @description
 * @date 2020/11/23$ 16:19$
 */

@Slf4j
public class FileConvertUtil {



/**
     * `word` 转 `pdf`
     *
     * @param wordBytes: word字节码
     * @return 生成的`pdf`字节码
     * @author
     * @date 2020/11/26 13:39
     */

    public static byte[] wordBytes2PdfBytes(byte[] wordBytes) {
        MatchLicense.init();
        return Word2PdfUtil.wordBytes2PdfBytes(wordBytes);
    }


/**
     * `word` 转 `pdf`
     *
     * @param wordBytes:   word字节码
     * @param pdfFilePath: 需转换的`pdf`文件路径
     * @return 生成的`pdf`文件数据
     * @author
     * @date 2020/11/26 13:39
     */

    public static File wordBytes2PdfFile(byte[] wordBytes, String pdfFilePath) {
        MatchLicense.init();
        return Word2PdfUtil.wordBytes2PdfFile(wordBytes, pdfFilePath);
    }


/**
     * `excel` 转 `pdf`
     *
     * @param excelBytes: html字节码
     * @return 生成的`pdf`文件流
     * @author zhengqing
     * @date 2020/11/24 11:26
     */

    public static byte[] excelBytes2PdfBytes(byte[] excelBytes) {
        MatchCellLicense.init();
        return Excel2PdfUtil.excelBytes2PdfBytes(excelBytes);
    }


/**
     * `excel` 转 `pdf`
     *
     * @param excelBytes:  excel文件字节码
     * @param pdfFilePath: 待生成的`pdf`文件路径
     * @return 生成的`Pdf`文件数据
     * @author zhengqing
     * @date 2020/11/24 11:26
     */

    public static File excelBytes2PdfFile(byte[] excelBytes, String pdfFilePath) {
        MatchCellLicense.init();
        return Excel2PdfUtil.excelBytes2PdfFile(excelBytes, pdfFilePath);
    }

}

