package com.example.mylibrary;

import android.graphics.Bitmap;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

/**
 * @author wuk
 */
public class FileUtil {

    /**
     * 创建文件
     * @param path 路径
     * @return 返回
     */
    public static boolean createFolder(String path) {
        if (path!=null && !path.isEmpty() && !"null".equals(path.substring(path.lastIndexOf("/") + 1))) {
            File file = new File(path);
            if (!file.exists()) {
                return file.mkdirs();
            }
        }
        return false;
    }


}
