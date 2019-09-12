package com.example.greendaotest.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.example.greendaotest.db.greendao.DaoMaster;
import com.example.greendaotest.db.greendao.ErrorMoDao;
import com.example.greendaotest.db.greendao.UserInfoDao;

import org.greenrobot.greendao.database.Database;

public class MySQLiteOpenHelper extends DaoMaster.OpenHelper {
    private final String TAG = MySQLiteOpenHelper.class.getSimpleName();

    /**
     * @param context 上下文
     * @param name    原来定义的数据库的名字   新旧数据库一致
     * @param factory 可以null
     */
    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    /**
     * @param db
     * @param oldVersion
     * @param newVersion 更新数据库的时候自己调用
     */
    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        Log.i(TAG,"数据库更新");
        //具体的数据转移在MigrationHelper2类中
        /**
         *  将db传入     将gen目录下的所有的Dao.类传入
         */
        MigrationHelper2.migrate(db, UserInfoDao.class, ErrorMoDao.class);
    }
}
