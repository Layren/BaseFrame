package com.base.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.base.config.BPConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class BaseDBManager {
    private String dbName = ""; // 保存的数据库文件名
    private int dbId = -1; // 保存的数据库资源id
    private static final String DB_PATH = BPConfig.CACHE_FILE_PATH + "/database";
    private SQLiteDatabase database;
    private final Context context;

    /* 创建单例数据库管理类 */
    private static volatile BaseDBManager dbManager;

    private BaseDBManager(Context context) {
        this.context = context;
    }

    public static BaseDBManager getInstance(Context context) {
        if (dbManager == null) {
            synchronized (BaseDBManager.class) {
                if (dbManager == null) {
                    dbManager = new BaseDBManager(context);
                }
            }
        }
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
        this.database = openDatabase(DB_PATH + "/" + dbName);
    }

    public void setDbName(String dbName, int dataId) {
        this.dbName = dbName;
        this.dbId = dataId;
        openDatabase();
    }

    private SQLiteDatabase openDatabase(String dbfile) {
        InputStream is = null;

        if (!(new File(dbfile).exists())) {
            // 判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
            try (FileOutputStream fos = new FileOutputStream(dbfile)) {
                is = context.getResources().openRawResource(dbId); // 欲导入的数据库
                byte[] buffer = new byte[400000];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return SQLiteDatabase.openOrCreateDatabase(dbfile, null);
    }

    public void closeDatabase() {
        if (this.database != null) {
            this.database.close();
        }

    }

}
