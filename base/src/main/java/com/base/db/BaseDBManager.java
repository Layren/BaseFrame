package com.base.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.base.config.BPConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class BaseDBManager {
    public static String DB_NAME = ""; // 保存的数据库文件名
    public static int DB_ID = -1; // 保存的数据库资源id
    public static final String DB_PATH = BPConfig.CACHE_FILE_PATH + "/database";
    private SQLiteDatabase database;
    private static Context context;

    /* 创建单例数据库管理类 */
    private volatile static BaseDBManager dbManager = new BaseDBManager();

    private BaseDBManager() {
    }

    public static BaseDBManager getInstance(Context context) {
        BaseDBManager.context = context;
        return dbManager;
    }

    public SQLiteDatabase getDatabase() {
        if (database == null)
            openDatabase();
        return database;
    }

    private void openDatabase() {
        File destDir = new File(DB_PATH);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        this.database = openDatabase(DB_PATH + "/" + DB_NAME);
    }

    public void setDbName(String dbName, int dataId) {
        DB_NAME = dbName;
        DB_ID = dataId;
        openDatabase();
    }

    private SQLiteDatabase openDatabase(String dbfile) {

        try {
            if (!(new File(dbfile).exists())) {
                // 判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
                InputStream is = context.getResources().openRawResource(DB_ID); // 欲导入的数据库
                FileOutputStream fos = new FileOutputStream(dbfile);
                byte[] buffer = new byte[400000];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            }
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbfile, null);
            return db;

        } catch (FileNotFoundException e) {
            Log.e("Database", "File not found");
        } catch (IOException e) {
            Log.e("Database", "IO exception");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void closeDatabase() {
        if (this.database != null) {
            this.database.close();
        }

    }

}
