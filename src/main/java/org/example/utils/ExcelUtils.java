package org.example.utils;

import com.alibaba.excel.EasyExcel;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Excel工具类，用于处理Excel导入导出功能
 */
@Component
public class ExcelUtils {

    /**
     * 导出Excel到响应流
     *
     * @param response HttpServletResponse对象
     * @param dataList 要导出的数据列表
     * @param sheetName 工作表名称
     * @param clazz 数据类型
     */
    public static void exportExcel(HttpServletResponse response, List<?> dataList,
                                   String sheetName, Class<?> clazz) throws IOException {
        // 设置响应头
        response.setCharacterEncoding("utf-8");
        // 使用EasyExcel写入数据
        EasyExcel.write(response.getOutputStream(), clazz)
                .sheet(sheetName)
                .doWrite(dataList);
    }

    /**
     * 读取Excel文件
     *
     * @param filePath 文件路径
     * @param clazz 数据类型
     * @param <T> 泛型
     * @return 数据列表
     */
    public static <T> List<T> readExcel(String filePath, Class<T> clazz) {
        return EasyExcel.read(filePath, clazz, null).sheet().doReadSync();
    }

    /**
     * 读取Excel文件并校验数据
     *
     * @param filePath 文件路径
     * @param clazz 数据类型
     * @param maxRows 最大读取行数
     * @param <T> 泛型
     * @return 数据列表
     */
    public static <T> List<T> readExcelWithValidation(String filePath, Class<T> clazz, int maxRows) {
        return EasyExcel.read(filePath, clazz, null)
                .sheet()
                .headRowNumber(1) // 从第一行开始读取数据
                .doReadSync();
    }

    /**
     * 从输入流读取Excel文件
     *
     * @param inputStream Excel文件输入流
     * @param clazz 数据类型
     * @param <T> 泛型
     * @return 数据列表
     */
    public static <T> List<T> readExcelFromInputStream(InputStream inputStream, Class<T> clazz) {
        return EasyExcel.read(inputStream, clazz, null).sheet().doReadSync();
    }

    /**
     * 从输入流读取Excel文件并使用监听器处理
     *
     * @param inputStream Excel文件输入流
     * @param clazz 数据类型
     * @param listener 数据处理监听器
     * @param <T> 泛型
     */
    public static <T> void readExcelWithListener(InputStream inputStream, Class<T> clazz,
                                                 com.alibaba.excel.event.AnalysisEventListener<T> listener) {
        EasyExcel.read(inputStream, clazz, listener).sheet().doRead();
    }
}
