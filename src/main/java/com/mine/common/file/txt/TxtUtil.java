package com.mine.common.file.txt;

import com.mine.common.annotation.Process;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

/**
 * @author : zyb
 * 时间 : 2022/3/20 14:48
 */
public class TxtUtil {

    public static LinkedList<String> getDataFromTxt(String path) throws IOException {
        return getDataFromTxt(path,-1);
    }

    @Process("从记事本文件中获取原始数据")
    public static LinkedList<String> getDataFromTxt(String path,int rowCount) throws IOException {
        int count = 0;
        try (FileInputStream file = new FileInputStream(path);
             InputStreamReader reader = new InputStreamReader(file, "GBK");
             BufferedReader br = new BufferedReader(reader)) {
            LinkedList<String> list = new LinkedList<>();
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
                if (rowCount > 0 && ++count == rowCount) {
                    break;
                }
            }
            return list;
        }
    }

    /**
     *
     * @param path
     * @param stCh 起始位置的标识字符串
     * @param edCh 结束位置的标识字符串
     * @return
     * @throws IOException
     */
    @Process("根据指定的标识获取记事本中的原始数据")
    public static LinkedList<String> getDataFromTxtByCharacteristic(String path,String stCh,String edCh) throws IOException {
        try (FileInputStream file = new FileInputStream(path);
             InputStreamReader reader = new InputStreamReader(file, "GBK");
             BufferedReader br = new BufferedReader(reader)) {
            LinkedList<String> list = new LinkedList<>();
            String line;

            while ((line = br.readLine()) != null) {
                if (list.size() == 0 && !line.trim().replace(" ","").contains(stCh)){
                    continue;
                }
                if (list.size() > 0 && line.trim().contains(edCh)){
                    list.add(line);
                    return list;
                }
                list.add(line);
            }
            return list;
        }
    }
}
