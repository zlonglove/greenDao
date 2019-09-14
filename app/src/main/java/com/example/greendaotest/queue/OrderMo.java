package com.example.greendaotest.queue;

/**
 * 测试订单类
 */
public class OrderMo {
    public int serialNo;
    public String desc;

    public OrderMo(int serialNo, String desc) {
        this.serialNo = serialNo;
        this.desc = desc;
    }

    public int getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(int serialNo) {
        this.serialNo = serialNo;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "OrderMo{" +
                "serialNo=" + serialNo +
                ", desc='" + desc + '\'' +
                '}';
    }
}
