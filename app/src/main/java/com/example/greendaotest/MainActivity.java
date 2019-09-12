package com.example.greendaotest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.greendaotest.db.DBManager;
import com.example.greendaotest.db.bean.ErrorMo;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ErrorMo errorMo = new ErrorMo();
        errorMo.errorInfo = "网络连接错误";
        errorMo.id = 1;
        errorMo.fixed = false;
        errorMo.newErrorFlag = true;
        errorMo.time = "2019/09/12 23:05";
        errorMo.errorType = 9;
        boolean b = DBManager.getInstance().saveError(errorMo);
        if (b) {
            Log.i(TAG, "--->入库成功");
            ArrayList<ErrorMo> errorMoResult = DBManager.getInstance().queryAllErrorByFixed(false);
            Log.i(TAG, "--->" + errorMoResult.toString());
        } else {
            Log.i(TAG, "--->入库失败");
        }
    }
}
