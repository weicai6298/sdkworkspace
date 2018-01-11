package com.kkgame.sdk.db;

import java.io.File;

import android.os.Environment;

/**
 * 外部数据库,
 */
public class SDBHelper {

    public static String DB_DIR = Environment.getExternalStorageDirectory()
            .getPath()
            + File.separator
            + "GameUserData"
            + File.separator
            + SDBHelper.class.getPackage().getName();
    static {
//        while (!Environment.getExternalStorageState().equals(
//                Environment.MEDIA_MOUNTED)) {
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//                break;
//            }
//        }
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            DB_DIR = Environment.getExternalStorageDirectory().getPath()
                    + File.separator + "GameUserData" + File.separator
                    + SDBHelper.class.getPackage().getName();
        } else {
            DB_DIR = Environment.getRootDirectory().getPath() + File.separator
                    + "GameUserData" + File.separator
                    + SDBHelper.class.getPackage().getName();
        }

        File dbFolder = new File(DB_DIR);
        if (!dbFolder.exists()) {
            dbFolder.mkdirs();
        }
    }
}
