package com.example.diarythink;

/**
 * Created by zhuyupei on 2017/11/6 0006.
 * 头部月份信息，里面存的月份是系统的计算方式，0代表1月，1代表2月，等等
 */

public class HeaderMonthInfo {
    private int month;
    private int year;

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
