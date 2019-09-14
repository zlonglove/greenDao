package com.example.greendaotest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.greendaotest.db.DBManager;
import com.example.greendaotest.db.bean.ErrorMo;
import com.example.greendaotest.queue.MilkTeaOrderQueueManager;
import com.example.greendaotest.queue.OrderMo;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getSimpleName();

    /**
     * v.1.1.2注释
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //testGreenDao();
        //milkTeaOrder();
    }

    /**
     * GreenDAO数据库测试
     */
    private void testGreenDao() {
        ErrorMo errorMo = new ErrorMo();
        errorMo.errorInfo = "网络连接错误";
        errorMo.id = 1;
        errorMo.fixed = false;
        errorMo.newErrorFlag = true;
        errorMo.time = "2019/09/12 23:05";
        errorMo.errorType = 9;
        errorMo.space1 = "space1";
        boolean b = DBManager.getInstance().saveError(errorMo);
        if (b) {
            Log.i(TAG, "--->入库成功");
            ArrayList<ErrorMo> errorMoResult = DBManager.getInstance().queryAllErrorByFixed(false);
            Log.i(TAG, "--->" + errorMoResult.toString());
        } else {
            Log.i(TAG, "--->入库失败");
        }
    }

    private void milkTeaOrder() {
        MilkTeaOrderQueueManager.getInstance().setmListener(new MilkTeaOrderQueueManager.QueueSizeChangeListener() {
            @Override
            public void onItemAdd(OrderMo addBean) {

            }

            @Override
            public void onItemDelete(OrderMo removeBean) {

            }

            @Override
            public void onMaking(OrderMo makingBean) {

            }

            @Override
            public void onOrderCancel(OrderMo cancelBean) {

            }
        });
        for (int i = 11; i > 0; i--) {
            OrderMo orderMo = new OrderMo(i, "this is" + i);
            MilkTeaOrderQueueManager.getInstance().addBean(orderMo);
        }
    }

    public void beginOrder(View view) {
        milkTeaOrder();
    }
}
