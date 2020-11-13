package com.cchao.simplelib.util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @author cch
 * @version 2020/10/7
 */
public class UuidUtil {
    private static final String TAG = "UniqueIDUtils";
    private static String uniqueID;
    private static final String uniqueIDDirPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    private static final String uniqueIDFile = "unique.txt";

    public static String getUniqueID(Context context) {
        //三步读取：内存中，存储的SP表中，外部存储文件中
        if (!TextUtils.isEmpty(uniqueID)) {
            Log.e(TAG, "getUniqueID: 内存中获取" + uniqueID);
            return uniqueID;
        }
        readUniqueFile(context);
        if (!TextUtils.isEmpty(uniqueID)) {
            Log.e(TAG, "getUniqueID: 外部存储中获取" + uniqueID);
            return uniqueID;
        }
        createUniqueID(context);
        return uniqueID;
    }

    private static void createUniqueID(Context context) {
        if (!TextUtils.isEmpty(uniqueID)) {
            return;
        }
        uniqueID = UUID.randomUUID().toString();
        Log.e(TAG, "getUniqueID: UUID生成成功" + uniqueID);
        File filesDir = new File(uniqueIDDirPath + File.separator + context.getApplicationContext().getPackageName());
        if (!filesDir.exists()) {
            filesDir.mkdir();
        }
        File file = new File(filesDir, uniqueIDFile);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(uniqueID.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void readUniqueFile(Context context) {
        File filesDir = new File(uniqueIDDirPath + File.separator + context.getApplicationContext().getPackageName());
        File file = new File(filesDir, uniqueIDFile);
        if (file.exists()) {
            FileInputStream inputStream = null;
            try {
                inputStream = new FileInputStream(file);
                byte[] bytes = new byte[(int) file.length()];
                inputStream.read(bytes);
                uniqueID = new String(bytes);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void clearUniqueFile(Context context) {
        File filesDir = new File(uniqueIDDirPath + File.separator + context.getApplicationContext().getPackageName());
        deleteFile(filesDir);
    }

    private static void deleteFile(File file) {
        if (file.isDirectory()) {
            for (File listFile : file.listFiles()) {
                deleteFile(listFile);
            }
        } else {
            file.delete();
        }
    }
}
