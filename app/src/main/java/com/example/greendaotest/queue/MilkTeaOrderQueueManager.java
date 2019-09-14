package com.example.greendaotest.queue;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;


import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class MilkTeaOrderQueueManager {
    private final String TAG = MilkTeaOrderQueueManager.class.getSimpleName();
    private List<OrderMo> mQueue;

    private static MilkTeaOrderQueueManager mTeaMilkQueueManager = new MilkTeaOrderQueueManager();

    private Handler mBlockLoopHandler;

    private Semaphore mSemaphore = new Semaphore(1);

    private QueueSizeChangeListener mListener;
    //当前正在制作的奶茶
    private OrderMo currMakingTea;

    private boolean hasError = false;
    private boolean isInit = false;

    private MilkTeaOrderQueueManager() {
        mQueue = new LinkedList<>();
        initBlockThread();
    }

    public static MilkTeaOrderQueueManager getInstance() {
        if (mTeaMilkQueueManager == null) {
            synchronized (MilkTeaOrderQueueManager.class) {
                if (mTeaMilkQueueManager == null) {
                    mTeaMilkQueueManager = new MilkTeaOrderQueueManager();
                }
            }
        }
        return mTeaMilkQueueManager;
    }

    private void initBlockThread() {
        new Thread() {
            @Override
            public void run() {
                Log.i(TAG, "--------------initBlockThread start-----------");
                Looper.prepare();
                mBlockLoopHandler = new Handler(Looper.myLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        try {
                            if (!hasError) {
                                Log.i(TAG, "没有故障，请求信号量");
                                mSemaphore.acquire();
                                Log.i(TAG, "请求到信号量");
                                OrderMo bean = getOrder();
                                /**
                                 * 制作奶茶
                                 */
                                Log.i(TAG, "制作奶茶状态 -- > bean --> " + (bean == null) + "hasError --> " + hasError);
                                if (bean != null && !hasError) {
                                    Log.i(TAG, "making order --> " + bean.toString());
                                    boolean b = makeMilkTea(bean);
                                    if (!b) {//没有查到该产品
                                        Log.i(TAG, "释放信号量");
                                        if (mSemaphore.availablePermits() < 1)
                                            mSemaphore.release();
                                        //提示订单被取消

                                    }
                                } else if (!hasError) {
                                    Log.i(TAG, "释放信号量");
                                    if (mSemaphore.availablePermits() < 1)
                                        mSemaphore.release();
                                } else {
                                    Log.i(TAG, "有故障");
                                    Message message = mBlockLoopHandler.obtainMessage(11, bean);
                                    mBlockLoopHandler.sendMessageDelayed(message, 1000);
                                    if (mSemaphore.availablePermits() < 1)
                                        mSemaphore.release();

                                }
                            } else {//制作过程有错误的时候
                                if (mQueue.size() != 0) {
                                    Log.i(TAG, "有故障重新发送一个空请求");
                                    Message message = mBlockLoopHandler.obtainMessage(11, null);
                                    mBlockLoopHandler.sendMessageDelayed(message, 1000);
                                    if (mSemaphore.availablePermits() < 1)
                                        mSemaphore.release();
                                }
                            }
                        } catch (InterruptedException e) {
                            Log.i(TAG, "异常退出");
                            e.printStackTrace();
                        } catch (Exception e) {
                            Log.i(TAG, "异常退出");
                            e.printStackTrace();
                        }
                    }
                };
                int i = mSemaphore.availablePermits();
                if (i < 1) {
                    mSemaphore.release();
                }
                Log.i(TAG, "--------------initBlockThread end-----------");
                isInit = true;
                Looper.loop();//开启循环
            }
        }.start();
    }

    public synchronized void setHasError(boolean hasError) {
        this.hasError = hasError;
    }

    public boolean isHasError() {
        return hasError;
    }

    public boolean addBean(OrderMo bean) {
        boolean add = false;
        if (null != bean && null != mQueue) {
            synchronized (MilkTeaOrderQueueManager.class) {
                if (mBlockLoopHandler == null) {
                    try {
                        mSemaphore.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //去重
                if (!mQueue.contains(bean)) {
                    add = mQueue.add(bean);
                }
                //排序
                Collections.sort(mQueue, new Comparator<OrderMo>() {
                    @Override
                    public int compare(OrderMo o1, OrderMo o2) {
                        return o1.serialNo - o2.serialNo;
                    }
                });
                if (null != mListener) {
                    mListener.onItemAdd(bean);
                }
                while (!isInit) {
                    SystemClock.sleep(100);
                }
                Message message = mBlockLoopHandler.obtainMessage(11, bean);
                mBlockLoopHandler.sendMessage(message);
            }
        }
        return add;
    }

    private OrderMo getOrder() {
        OrderMo bean = null;
        if (null != mQueue && !mQueue.isEmpty()) {
            synchronized (MilkTeaOrderQueueManager.class) {
                if (!mQueue.isEmpty() && !hasError) {
                    bean = mQueue.remove(0);
                    setCurrMakingTea(bean);
                    if (null != mListener) {
                        mListener.onMaking(bean);
                    }
                }
            }
        }
        return bean;
    }

    public OrderMo getCurrMakingTea() {
        return currMakingTea;
    }

    public void setCurrMakingTea(OrderMo currMakingTea) {
        this.currMakingTea = currMakingTea;
    }

    public void cancelOrder(OrderMo orderMo) {
        if (orderMo == null) {
            return;
        }
        int i = mQueue.indexOf(orderMo);
        if (i != -1) {
            synchronized (MilkTeaOrderQueueManager.class) {
                OrderMo remove = mQueue.remove(i);
                mBlockLoopHandler.removeMessages(11, orderMo);
                if (mListener != null) {
                    mListener.onOrderCancel(remove);
                }
            }
        } else if (orderMo == currMakingTea) {//取消当前正在制作订单
            if (mListener != null) {
                mListener.onOrderCancel(orderMo);
            }
        }
    }

    public void releaseSemaphore() {
        Log.i(TAG, "释放锁");
        int i = mSemaphore.availablePermits();
        if (i < 1) {//制作队列被锁住  需要释放锁,制作下一杯
            synchronized (MilkTeaOrderQueueManager.class) {
//            OrderMo remove = mQueue.remove(0);
                mListener.onItemDelete(currMakingTea);
                currMakingTea = null;
            }
            mSemaphore.release();
        }
    }

    public int getSize() {
        return mQueue.isEmpty() ? 0 : mQueue.size();
    }

    public QueueSizeChangeListener getmListener() {
        return mListener;
    }

    public void setmListener(QueueSizeChangeListener mListener) {
        this.mListener = mListener;
    }

    public void clearAllBean() {
        if (mQueue != null) {
            mQueue.clear();
        }
    }

    public interface QueueSizeChangeListener {

        void onItemAdd(OrderMo addBean);

        void onItemDelete(OrderMo removeBean);

        void onMaking(OrderMo makingBean);

        void onOrderCancel(OrderMo cancelBean);
    }

    public List<OrderMo> getQueue() {
        return mQueue;
    }

    public boolean makeMilkTea(final OrderMo orderMo) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(5000);
                Log.i(TAG, "--->make milk tea success " + orderMo.getSerialNo());
                releaseSemaphore();
            }
        }).start();
        return true;
    }
}
