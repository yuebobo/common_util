package com.mine.common.file.word;

import com.mine.common.annotation.Process;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblBorders;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder;

import java.math.BigInteger;
import java.util.List;

/**
 * @author : zyb
 * 时间 : 2022/3/24 22:27
 */
public class WordUtil {

    public static final String oldValue = "old_value_can_not_change";

    @Process("数据插入到word的表格中")
    public static void insertDataToTable(XWPFTable table, List<String[]> data) {
        for (String[] d : data) {
            final XWPFTableRow row = table.createRow();
            for (int i = 0; i < d.length; i++) {
                XWPFTableCell cell = row.getCell(i);
                if (cell == null) {
                    cell = row.createCell();
                }
                insertDataToCell(cell, d[i], 10);
            }
        }
        formatTableBolder(table);
    }


    @Process("把数据写入到固定的word表格中")
    public static void writeDataToTable(XWPFTable table,String[][] value) {

        for (int i = 0; i < value.length; i++) {
            String[] rowData = value[i];
            if (rowData == null) {
                continue;
            }
            XWPFTableRow row = table.getRow(i);
            for (int j = 0; j < rowData.length; j++) {
                String cellData = rowData[j];
                if (cellData != null) {
                    insertDataToCell(row.getCell(j), cellData, 10);
                }
            }
        }
    }


    @Process("把数据插入到表格的单元格中")
    private static void insertDataToCell(XWPFTableCell cell, String text, int fontSize) {
        if (oldValue.equals(text)){
            return;
        }
        cell.removeParagraph(0);
        XWPFParagraph pr = cell.addParagraph();
        XWPFRun rIO = pr.createRun();
        rIO.setFontFamily("Times New Roman");
        rIO.setColor("000000");
        rIO.setFontSize(fontSize);
        rIO.setText(text);
        cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
        pr.setAlignment(ParagraphAlignment.CENTER);
    }


    /**
     * 设置表格边框样式
     */
    private static void formatTableBolder(XWPFTable table) {
        // 双实线边框
//        String bolderType = "double";
        String bolderType = "single";

        CTTblBorders borders = table.getCTTbl().getTblPr().addNewTblBorders();
        // 设置内边框样式，默认单实线
        CTBorder hBorder = borders.addNewInsideH();
        hBorder.setVal(STBorder.Enum.forString(bolderType));
        hBorder.setSz(new BigInteger("1")); // 线条大小
        hBorder.setColor("000000"); // 设置颜色

        CTBorder vBorder = borders.addNewInsideV();
        vBorder.setVal(STBorder.Enum.forString(bolderType));
        vBorder.setSz(new BigInteger("1"));
        vBorder.setColor("000000");
//
//        // 设置上下左右外边框
        CTBorder lBorder = borders.addNewLeft();
        lBorder.setVal(STBorder.Enum.forString(bolderType));
        lBorder.setSz(new BigInteger("1"));
        lBorder.setColor("000000");

        CTBorder rBorder = borders.addNewRight();
        rBorder.setVal(STBorder.Enum.forString(bolderType));
        rBorder.setSz(new BigInteger("1"));
        rBorder.setColor("000000");

        CTBorder tBorder = borders.addNewTop();
        tBorder.setVal(STBorder.Enum.forString(bolderType));
        tBorder.setSz(new BigInteger("1"));
        tBorder.setColor("000000");

        CTBorder bBorder = borders.addNewBottom();
        bBorder.setVal(STBorder.Enum.forString(bolderType));
        bBorder.setSz(new BigInteger("1"));
        bBorder.setColor("000000");
    }


}
