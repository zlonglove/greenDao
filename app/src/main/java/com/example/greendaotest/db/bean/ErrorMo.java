package com.example.greendaotest.db.bean;

import android.os.Parcel;
import android.os.Parcelable;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class ErrorMo implements Parcelable {
    @Id
    public long id;
    public boolean newErrorFlag;
    public String time;
    public String errorInfo;
    public boolean fixed;
    public int errorType;
    public String space1;
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeByte(newErrorFlag ? (byte) 1 : (byte) 0);
        dest.writeString(this.time);
        dest.writeString(this.errorInfo);
        dest.writeByte(fixed ? (byte) 1 : (byte) 0);
        dest.writeInt(this.errorType);
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean getNewErrorFlag() {
        return this.newErrorFlag;
    }

    public void setNewErrorFlag(boolean newErrorFlag) {
        this.newErrorFlag = newErrorFlag;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getErrorInfo() {
        return this.errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public boolean getFixed() {
        return this.fixed;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    public int getErrorType() {
        return errorType;
    }

    public void setErrorType(int errorType) {
        this.errorType = errorType;
    }

    public ErrorMo() {
    }

    protected ErrorMo(Parcel in) {
        this.id = in.readLong();
        this.newErrorFlag = in.readByte() != 0;
        this.time = in.readString();
        this.errorInfo = in.readString();
        this.fixed = in.readByte() != 0;
        this.errorType=in.readInt();
    }

    @Generated(hash = 1565323390)
    public ErrorMo(long id, boolean newErrorFlag, String time, String errorInfo,
            boolean fixed, int errorType, String space1) {
        this.id = id;
        this.newErrorFlag = newErrorFlag;
        this.time = time;
        this.errorInfo = errorInfo;
        this.fixed = fixed;
        this.errorType = errorType;
        this.space1 = space1;
    }


    public static final Creator<ErrorMo> CREATOR = new Creator<ErrorMo>() {
        public ErrorMo createFromParcel(Parcel source) {
            return new ErrorMo(source);
        }

        public ErrorMo[] newArray(int size) {
            return new ErrorMo[size];
        }
    };

    @Override
    public String toString() {
        return "ErrorMo{" +
                "id=" + id +
                ", newErrorFlag=" + newErrorFlag +
                ", time='" + time + '\'' +
                ", errorInfo='" + errorInfo + '\'' +
                ", fixed=" + fixed +
                '}';
    }

    public String getSpace1() {
        return this.space1;
    }

    public void setSpace1(String space1) {
        this.space1 = space1;
    }
}
