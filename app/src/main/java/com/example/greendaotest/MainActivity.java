package com.example.greendaotest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.greendaotest.db.DBManager;
import com.example.greendaotest.db.bean.ErrorMo;

import java.util.ArrayList;

/**
 * v1.1.1注释
 */
public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getSimpleName();

    /**
     * v1.1.1注释
     * @param savedInstanceState
     */
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
        errorMo.space1="space1";
        boolean b = DBManager.getInstance().saveError(errorMo);
        if (b) {
            Log.i(TAG, "--->入库成功");
            ArrayList<ErrorMo> errorMoResult = DBManager.getInstance().queryAllErrorByFixed(false);
            Log.i(TAG, "--->" + errorMoResult.toString());
        } else {
            Log.i(TAG, "--->入库失败");
        }
    }
    //我现在是v1.1.1上的修改,测试完成后需要合并到master分支
}
