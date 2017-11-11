package com.example.diarythink.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


import com.example.diarythink.HeaderMonthInfo;

import java.util.Calendar;

/**
 * Created by zhuyupei on 2017/11/6 0006.
 */

public class HeaderMonthView extends View {

    Region[] monthRegionsSeven = new Region[7];
    String[] monthStr = {"1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"};
    //头部月份信息，里面存的月份是系统的计算方式，0代表1月，1代表2月，等等
    HeaderMonthInfo[] headerMonthInfoList = new HeaderMonthInfo[7];
    private int curMouth = 0;
    private int curYear = 0;

    private int downX;
    private int downY;


    private OnClickHeaderMonthListener onClickHeaderMonthListener;

    private void initData(int year,int month){
        //初始化7个数据
        Calendar calender = Calendar.getInstance();
        calender.set(year,month,1);

        for (int i = 0; i < 7; i++) {
            headerMonthInfoList[i] = new HeaderMonthInfo();
            headerMonthInfoList[i].setMonth(calender.get(Calendar.MONTH));
            headerMonthInfoList[i].setYear(calender.get(Calendar.YEAR));
            calender.add(Calendar.MONTH,1);
        }
    }

    public HeaderMonthView(Context context) {
        this(context,null);
    }

    public HeaderMonthView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HeaderMonthView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Calendar calendar = Calendar.getInstance();
        initData(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(measureWidth, measureWidth/7+20);
    }

    public void setCurMouth(int curMouth,int curYear){
        this.curMouth = curMouth;
        this.curYear = curYear;
        boolean isRefreshData = getIsRefresh(curMouth, curYear);
        if(isRefreshData){//判断是否需要刷新
            Calendar curDate = getCalendar(curYear,curMouth,1);
            Calendar firstDate = getCalendar(headerMonthInfoList[0].getYear(),headerMonthInfoList[0].getMonth(),1);
            if(curDate.compareTo(firstDate) == -1){//如果当前日期小于第一个日期
                //向前刷新
                initData(curYear, curMouth);
            }
            Calendar endDate = getCalendar(headerMonthInfoList[headerMonthInfoList.length - 1].getYear(),headerMonthInfoList[headerMonthInfoList.length-1].getMonth(),1);
            if(curDate.compareTo(endDate) == 1){//如果当前日期大于最后一个日期
                //向后刷新
                Calendar calendar = Calendar.getInstance();
                calendar.set(curYear,curMouth,1);
                calendar.add(Calendar.MONTH,-(headerMonthInfoList.length-1));//这样初始化保证当前月还是在最后一个
                initData(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH));
            }
        }
        invalidate();
    }

    /**
     * 判断当前月份是否在数组内，否则需要刷新页面数据
     * @param curMouth
     * @param curYear
     * @return
     */
    private boolean getIsRefresh(int curMouth, int curYear) {
        boolean isRefreshData = false;
        for (int i = 0; i < headerMonthInfoList.length; i++) {
            if(curMouth == headerMonthInfoList[i].getMonth() && curYear == headerMonthInfoList[i].getYear()){
                isRefreshData = false;
                break;
            }else {
                isRefreshData = true;
            }
        }
        return isRefreshData;
    }

    private Calendar getCalendar(int year, int month, int date){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month,date);
        return calendar;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        draw(canvas,0,0);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        initRegion(w,h);
    }

    private void initRegion(int w, int h) {
        int cellW = (int) (w / 7F);
        int cellH = cellW;
        //初始化7个矩形区域，用来画圆圈的地方
        for (int i = 0; i < 7; i++) {
                Region region = new Region();
                region.set((i * cellW), 0, cellW + (i * cellW),
                        cellH);
                monthRegionsSeven[i] = region;
        }
    }

    private void draw(Canvas canvas, int x, int y){

        Paint paint = new Paint();
        for (int i = 0; i < 7; i++) {
            int rectWidth = monthRegionsSeven[i].getBounds().width();
            int centerX = monthRegionsSeven[i].getBounds().centerX();
            int centerY = monthRegionsSeven[i].getBounds().centerY();

            //画外圆圈
            canvas.save();
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.WHITE);
            paint.setStrokeWidth(3f);
            canvas.drawCircle(centerX,centerY,rectWidth/2-10,paint);
            canvas.restore();

            //当前月份白色背景
            if(curMouth == headerMonthInfoList[i].getMonth() && curYear == headerMonthInfoList[i].getYear() ){
                canvas.save();
                paint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(centerX ,centerY,rectWidth/2-15,paint);
                canvas.drawLine(centerX ,centerY+(rectWidth/2-10),centerX ,centerY+rectWidth,paint);
                canvas.restore();
            }

            //画文字
            canvas.save();
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            if(curMouth == headerMonthInfoList[i].getMonth()&& curYear == headerMonthInfoList[i].getYear()){
                paint.setColor(Color.RED);
            }else{
                paint.setColor(Color.WHITE);
            }
            paint.setStrokeWidth(1f);
            paint.setTextSize(20);
            if(headerMonthInfoList[i].getMonth() >= 9){//10月份以上，字符较多，故x轴位置不一样
                canvas.drawText(monthStr[headerMonthInfoList[i].getMonth()],centerX+(paint.ascent()-paint.descent()),centerY-((paint.descent()+paint.ascent())/2),paint);
            }else{
                canvas.drawText(monthStr[headerMonthInfoList[i].getMonth()],centerX+(paint.ascent()+paint.descent()),centerY-((paint.descent()+paint.ascent())/2),paint);
            }
            canvas.restore();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getRawX();
                downY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:

                if (Math.abs(downX - event.getRawX()) < 10) {//判断是点击还是滑动
                    int dateX = (int) (Math.ceil(event.getX()/monthRegionsSeven[0].getBounds().width()) - 1);
                    if(onClickHeaderMonthListener != null){
                        dateX = dateX > 0 ? dateX:0;
                        onClickHeaderMonthListener.onMonthClickChange(headerMonthInfoList[dateX].getMonth(),headerMonthInfoList[dateX].getYear());
                    }
                }
                break;

        }
        return super.onTouchEvent(event);

    }

    public void setOnClickHeaderMonthListener(OnClickHeaderMonthListener onClickHeaderMonthListener){
        this.onClickHeaderMonthListener = onClickHeaderMonthListener;
    }


    public interface OnClickHeaderMonthListener{
        void onMonthClickChange(int month, int year);
    }

}
