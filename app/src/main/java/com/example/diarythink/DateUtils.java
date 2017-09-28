package com.example.diarythink;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
    public static String[][] buildMonthG(int year, int month) {
        String[][] tmp = new String[6][7];

        //确定一个月有多少天
        int daysInMonth = 0;
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 ||
                month == 12) {
            daysInMonth = 31;
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            daysInMonth = 30;
        } else if (month == 2) {
            if (isLeapYear(year)) {
                daysInMonth = 29;
            } else {
                daysInMonth = 28;
            }
        }

        //初始化给数组赋值为空字符串
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                tmp[i][j] = "";
            }
        }

        //把日期设置到数组对应的位置中(如1号是周五，则把1号设置到tmp[0][5]的位置)
        int day = 1;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if(day <= daysInMonth) {
                    j = getDayOfWeek(year, month, day) - 1;
                    tmp[i][j] = day + "";
                    day++;
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
        //把年月日格式化成yyyy-MM-dd格式的
        StringBuilder sb = new StringBuilder();
        sb.append(year);
        sb.append(month < 10 ? "-0":"-");
        sb.append(month);
        sb.append(day < 10 ? "-0":"-");
        sb.append(day);

        //获取指定日期的calendar，并返回指定日期是一个星期中的第几天，注意周日算是第一天
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
        Date date= null;
        try {
            date = formatter.parse(sb.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
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
