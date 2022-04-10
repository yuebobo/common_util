package com.mine.common.file;

import com.mine.common.annotation.Process;

import java.io.FileNotFoundException;

/**
 * @author : zyb
 * 时间 : 2022/3/19 23:15
 */
public class FileUtil {

    @Process("获取基本的文件路径")
    public static String getBasePath(String path) throws FileNotFoundException {
        String basePath;
        if (path.contains("\\")) {
            basePath = path.substring(0, path.lastIndexOf("\\") + 1);
        } else if (path.contains("/")) {
            basePath = path.substring(0, path.lastIndexOf("/") + 1);
        } else {
            throw new FileNotFoundException();
        }
        return basePath;
    }
}
