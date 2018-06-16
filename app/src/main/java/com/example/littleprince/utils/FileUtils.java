package com.example.littleprince.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

public class FileUtils {
    public static final String FOLDER_NAME = "LittlePrinceEdit";

    /**
     * 获取存贮文件的文件夹路径
     *
     * @return
     */
    public static File createFolders() {
        File baseDir;
        if (android.os.Build.VERSION.SDK_INT < 8) {
            baseDir = Environment.getExternalStorageDirectory();
        } else {
            baseDir = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        }
        if (baseDir == null)
            return Environment.getExternalStorageDirectory();
        File aviaryFolder = new File(baseDir, FOLDER_NAME);
        if (aviaryFolder.exists())
            return aviaryFolder;
        if (aviaryFolder.isFile())
            aviaryFolder.delete();
        if (aviaryFolder.mkdirs())
            return aviaryFolder;
        return Environment.getExternalStorageDirectory();
    }

    public static File genEditFile(){
        return FileUtils.getEmptyFile("edit"
                + System.currentTimeMillis() + ".png");
    }

    public static File getEmptyFile(String name) {
        File folder = FileUtils.createFolders();
        if (folder != null) {
            if (folder.exists()) {
                File file = new File(folder, name);
                return file;
            }
        }
        return null;
    }
}
