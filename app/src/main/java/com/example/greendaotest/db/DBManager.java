package com.example.greendaotest.db;


import com.example.greendaotest.AppApplication;
import com.example.greendaotest.db.bean.ErrorMo;
import com.example.greendaotest.db.greendao.DaoMaster;
import com.example.greendaotest.db.greendao.DaoSession;
import com.example.greendaotest.db.greendao.ErrorMoDao;
import com.example.greendaotest.db.greendao.UserInfoDao;

import java.util.ArrayList;

public class DBManager {
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private ErrorMoDao mErrorMoDao;
    private UserInfoDao mUserinfoDao;

    private DBManager() {
    }

    private static volatile DBManager instance = null;

    public static DBManager getInstance() {
        if (instance == null) {
            synchronized (DBManager.class) {
                if (instance == null) {
                    instance = new DBManager();
                }
            }
        }
        return instance;
    }

    public void init() {
        MySQLiteOpenHelper helper = new MySQLiteOpenHelper(AppApplication.getInstance().getApplicationContext(),
                "greenDao.db", null);
        mDaoMaster = new DaoMaster(helper.getWritableDb());
        mDaoSession = mDaoMaster.newSession();

        mUserinfoDao = mDaoSession.getUserInfoDao();
        mErrorMoDao = mDaoSession.getErrorMoDao();
    }


    /***************** Error Dao ************************/
    public ArrayList<ErrorMo> queryAllErrorByFixed(boolean fixed) {
        mErrorMoDao.detachAll();
        return (ArrayList<ErrorMo>) mErrorMoDao.queryBuilder()
                .where(ErrorMoDao.Properties.Fixed.eq(fixed))
                .build().list();
    }

    public ErrorMo queryErrorById(long id) {
        return mErrorMoDao.queryBuilder()
                .where(ErrorMoDao.Properties.Id.eq(id))
                .build().unique();
    }

    public ErrorMo queryErrorByIdAndFixed(long id, boolean fixed) {
        return mErrorMoDao.queryBuilder()
                .where(ErrorMoDao.Properties.Id.eq(id))
                .where(ErrorMoDao.Properties.Fixed.eq(fixed))
                .build().unique();
    }

    public void updateAllError(ArrayList<ErrorMo> errorMos) {
        mErrorMoDao.insertOrReplaceInTx(errorMos);
    }

    public boolean saveError(ErrorMo errorMo) {
        long id = mErrorMoDao.insertOrReplace(errorMo);
        System.out.println("id === " + id);
        return id > 0;
    }
}