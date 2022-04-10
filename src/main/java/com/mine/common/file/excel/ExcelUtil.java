package com.mine.common.file.excel;

import com.mine.common.JavaUtil;
import com.mine.common.annotation.Process;
import com.mine.common.exception.MyException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

/**
 * @author : zyb
 * 时间 : 2022/3/14 19:58
 */
public class ExcelUtil {


    @Process("根据路径从excel中获取数据")
    public static <T> List<T> getDataFromExcel(String path, int sheetIndex, int startRow,
                                               int[] columns, Function<String[], T> function) throws IOException {
        try (FileInputStream stream = new FileInputStream(path)) {
            XSSFWorkbook workbook = new XSSFWorkbook(stream);
            XSSFSheet sheet = workbook.getSheetAt(sheetIndex);
            return getDataFromSheet(sheet, startRow, columns, function);
        }
    }

    /**
     * @param sheet
     * @param startRow
     * @param count    表示取几行数据，为0表示没有固定值，
     * @param columns
     * @param function
     * @param <T>
     * @return
     */
    public static <T> List<T> getDataFromSheet(XSSFSheet sheet, int startRow, int count, int[] columns, Function<String[], T> function) {
        Iterator<Row> it = sheet.iterator();
        JavaUtil.iteratorNext(it, startRow);
        List<T> data = getDataFromExcel(it, count, columns, function);
        return data;
    }


    @Process("从sheet中获取数据")
    public static <T> List<T> getDataFromSheet(XSSFSheet sheet, int startRow, int[] columns, Function<String[], T> function) {
        return getDataFromSheet(sheet, startRow, 0, columns, function);
    }

    @Process("根据Iterator从excel里逐行获取数据")
    public static <T> List<T> getDataFromExcel(Iterator<Row> it, int count, int[] columns, Function<String[], T> function) {
        Row row;
        T t;
        List<T> data = new ArrayList<>(150);
        while (it.hasNext()) {
            row = it.next();
            try {
                t = getDataFromRow(row, columns, function);
            } catch (Exception e) {
                throw MyException.build("从excel中获取数据异常,在第" + (row.getRowNum() + 1) + "行前后", e);
            }
            if (t == null) {
                break;
            } else {
                data.add(t);
                if (count > 0 && data.size() >= count) {
                    break;
                }
            }
        }
        return data;
    }

    @Process("从excel的一行中获取数据")
    public static <T> T getDataFromRow(Row row, int[] columns, Function<String[], T> function) {
        int length = columns.length;
        String[] data = new String[length];
        Cell cell;
        for (int i = 0; i < length; i++) {
            cell = row.getCell(columns[i]);
            if (cell == null) {
                return null;
            }
            data[i] = getValueFromCell(cell);
            if (JavaUtil.isEmpty(data[i])) {
                return null;
            }
        }
        return function.apply(data);
    }


    @Process("从单元格中取数据")
    public static String getValueFromCell(Cell cell) {
        if (cell == null) return null;
        try {
            return cell.getNumericCellValue() + "";
        } catch (Exception e) {
            try {
                return cell.getStringCellValue();
            } catch (Exception e1) {
                if (cell instanceof XSSFCell) {
                    return ((XSSFCell) cell).getRawValue();
                }
                return cell.getRichStringCellValue().toString();
            }
        }
    }

    @Process("数据插入到Excel中")
    public static void insertDataToExcel(String path, String targetPath, int sheetIndex, int startRow, int[] columns, List<String[]> data) throws IOException {
        try (FileInputStream stream = new FileInputStream(path); FileOutputStream out = new FileOutputStream(targetPath)) {
            XSSFWorkbook workbook = new XSSFWorkbook(stream);
            int count = workbook.getNumberOfSheets();
            if (sheetIndex + 1 > count) {
                int sub = sheetIndex - count + 1;
                for (int i = 0; i < sub; i++) {
                    workbook.createSheet();
                }
            }
            XSSFSheet sheet = workbook.getSheetAt(sheetIndex);
            insertDataToSheet(sheet, startRow, columns, data);
            workbook.write(out);
        }
    }

    @Process("数据插入到sheet中")
    public static void insertDataToSheet(XSSFSheet sheet, int startRow, int[] columns, List<String[]> data) {
        int size = data.size();
        for (int i = 0; i < size; i++) {
            XSSFRow row = sheet.getRow(i + startRow);
            row = row != null ? row : sheet.createRow(i + startRow);
            insertToRow(row, columns, data.get(i));
        }
    }

    @Process("数据插入到row中")
    public static void insertToRow(XSSFRow row, int[] columns, String[] values) {
        for (int i = 0; i < columns.length; i++) {
            XSSFCell cell = row.getCell(columns[i]);
            cell = cell != null ? cell : row.createCell(columns[i]);
            cell.setCellValue(values[i]);
        }
    }

    @Process("通过excel节点数据从excel中获取数据")
    public static void getDataByExcelData(ExcelData excelData) throws IOException {
        if (ExcelData.Type.WORK_BOOK.equals(excelData.getType())) {
            String path = excelData.getValue().toString();
            try (FileInputStream stream = new FileInputStream(path)) {
                XSSFWorkbook workbook = new XSSFWorkbook(stream);
                for (ExcelData e : excelData.getExcelDataList()) {
                    e.setValue(workbook.getSheetAt(e.getIndex()));
                    getDataByExcelData(e);
                }
            }
        }

        if (ExcelData.Type.SHEET.equals(excelData.getType())) {
            XSSFSheet sheet = (XSSFSheet) excelData.getValue();
            for (ExcelData e : excelData.getExcelDataList()) {
                e.setValue(sheet.getRow(e.getIndex()));
                getDataByExcelData(e);
            }
        }

        if (ExcelData.Type.ROW.equals(excelData.getType())) {
            XSSFRow row = (XSSFRow) excelData.getValue();
            for (ExcelData e : excelData.getExcelDataList()) {
                e.setValue(row.getCell(e.getIndex()));
                getDataByExcelData(e);
            }
        }

        if (ExcelData.Type.CELL.equals(excelData.getType())) {
            XSSFCell cell = (XSSFCell) excelData.getValue();
            String value = getValueFromCell(cell);
            excelData.setValue(value);
        }
    }

}
