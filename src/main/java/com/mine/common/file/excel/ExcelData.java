package com.mine.common.file.excel;

import java.util.List;

/**
 * excel 的数据结构
 * @author : zyb
 * 时间 : 2022/3/19 22:48
 */
public class ExcelData {

    private Type type;

    /**
     * 当前节点对象的索引编号
     */
    private int index;

    /**
     * 当前节点对象
     */
    private Object value;

    /**
     * 当前节点的子节点集合
     */
    private List<ExcelData> excelDataList;

    public ExcelData(Type type, int index, Object value, List<ExcelData> excelDataList) {
        this.type = type;
        this.index = index;
        this.value = value;
        this.excelDataList = excelDataList;
    }

    public enum Type{
        WORK_BOOK,
        SHEET,
        ROW,
        CELL
    }


    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public List<ExcelData> getExcelDataList() {
        return excelDataList;
    }

    public void setExcelDataList(List<ExcelData> excelDataList) {
        this.excelDataList = excelDataList;
    }
}
