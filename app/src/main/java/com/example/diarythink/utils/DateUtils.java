package com.example.diarythink.utils;



import com.example.diarythink.bean.DateInfo;

import java.util.Calendar;

/**
 * Created by zhuyupei on 2017/9/26 0026.
 */

public class DateUtils {

    /**
     * 生成某年某月的公历天数数组
     * 数组为6x7的二维数组因为一个月的周数永远不会超过六周
     * 日期填充到对应的数组位置
     * 如果某个数组下标中没有对应天数那么则填充一个空字符串
     *
     * @param year  某年
     * @param month 某月
     * @return 某年某月的公历数组
     */
    public static DateInfo[][] buildMonthG(int year, int month) {
        DateInfo[][] tmp = new DateInfo[6][7];

        int realMonth = month +1;//真实月份
        //确定一个月有多少天
        int daysInMonth = 0;
        if (realMonth == 1 || realMonth == 3 || realMonth == 5 || realMonth == 7 || realMonth == 8 || realMonth == 10 ||
                realMonth == 12) {
            daysInMonth = 31;
        } else if (realMonth == 4 || realMonth == 6 || realMonth == 9 || realMonth == 11) {
            daysInMonth = 30;
        } else if (realMonth == 2) {
            if (isLeapYear(year)) {
                daysInMonth = 29;
            } else {
                daysInMonth = 28;
            }
        }

        //初始化给数组赋值为空字符串
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                tmp[i][j] = new DateInfo();
                tmp[i][j].date = 0;
            }
        }

        //把日期设置到数组对应的位置中(如1号是周五，则把1号设置到tmp[0][5]的位置)
        int day = 1;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if(day <= daysInMonth) {
                    j = getDayOfWeek(year, month, day) - 1;
                    tmp[i][j].date = day;
                    tmp[i][j].dayOfWeek = j;
                    tmp[i][j].month = month;
                    tmp[i][j].year = year;
                    day++;
                }
            }
        }

        //在第一排插入上月的日期信息
        Calendar calendar= Calendar.getInstance();
        calendar.set(year,month,1);//设置为这个月第一天
        for (int i = 6; i >= 0; i--) {
            if(tmp[0][i].date == 0){
                calendar.add(Calendar.DATE,-1);
                tmp[0][i].date = calendar.get(Calendar.DATE);
                tmp[0][i].dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                tmp[0][i].month = calendar.get(Calendar.MONTH);
                tmp[0][i].year = calendar.get(Calendar.YEAR);
            }
        }
        //在倒数第二排开始插入下个月的信息
        Calendar calendar1= Calendar.getInstance();
        calendar1.set(year,month,daysInMonth);//设置为这个月最后一天
        for (int i = 4; i < 6 ; i++) {
            for (int j = 0; j < 7; j++) {
                if(tmp[i][j].date == 0){
                    calendar1.add(Calendar.DATE,1);
                    tmp[i][j].date = calendar1.get(Calendar.DATE);
                    tmp[i][j].dayOfWeek = calendar1.get(Calendar.DAY_OF_WEEK);
                    tmp[i][j].month = calendar1.get(Calendar.MONTH);
                    tmp[i][j].year = calendar1.get(Calendar.YEAR);
                }
            }
        }

        return tmp;
    }



    /**
     * 获取指定日期是星期几
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static int getDayOfWeek(int year,int month,int day){
        //获取指定日期的calendar，并返回指定日期是一个星期中的第几天，注意周日算是第一天
        Calendar calendar= Calendar.getInstance();
        calendar.set(year,month,day);
        return  calendar.get(Calendar.DAY_OF_WEEK);

    }

    /**
     * 判断某年是否为闰年
     *
     * @param year ...
     * @return true表示闰年
     */
    private static boolean isLeapYear(int year) {
        return ((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0);
    }
}
