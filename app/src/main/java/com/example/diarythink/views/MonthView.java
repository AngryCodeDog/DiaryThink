package com.example.diarythink.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.diarythink.bean.DateInfo;
import com.example.diarythink.utils.DateUtils;

import java.util.Calendar;

/**
 * Created by zhuyupei on 2017/9/26 0026.
 */

public class MonthView extends View {

    private final Region[][] monthRegionsFour = new Region[4][7];
    private final Region[][] monthRegionsFive = new Region[5][7];
    private final Region[][] monthRegionsSix = new Region[6][7];

    private final DateInfo[][] infoFour = new DateInfo[4][7];
    private final DateInfo[][] infoFive = new DateInfo[5][7];
    private final DateInfo[][] infoSix = new DateInfo[6][7];

    private int leftYear,leftMonth;
    private int curYear,curMonth;
    private int rightYear,rightMonth;

    private int mLastX;
    private int mLastY;


    public MonthView(Context context) {
        this(context,null);
    }

    public MonthView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public MonthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //当前
        Calendar calendar = Calendar.getInstance();
        curYear = calendar.get(Calendar.YEAR);
        curMonth = calendar.get(Calendar.YEAR);
        //左边
        calendar.add(Calendar.YEAR,-1);
        leftYear = calendar.get(Calendar.YEAR);
        leftMonth = calendar.get(Calendar.YEAR);

        //右边
        calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR,1);
        rightYear = calendar.get(Calendar.YEAR);
        rightMonth = calendar.get(Calendar.YEAR);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(measureWidth, (int) (measureWidth * 6F / 7F));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        initRegion(w, h);
    }

    private void initRegion(int w, int h) {
        int cellW = (int) (w / 7F);
        int cellH = (int) (h / 6F);

        for (int i = 0; i < monthRegionsFour.length; i++) {
            for (int j = 0; j < monthRegionsFour[i].length; j++) {
                Region region = new Region();
                region.set(j * cellW, i * cellH, cellW + (j * cellW),
                        cellW + (i * cellH));
                monthRegionsFour[i][j] = region;
            }
        }
        for (int i = 0; i < monthRegionsFive.length; i++) {
            for (int j = 0; j < monthRegionsFive[i].length; j++) {
                Region region = new Region();
                region.set(j * cellW, i * cellH, cellW + (j * cellW),
                        cellW + (i * cellH));
                monthRegionsFive[i][j] = region;
            }
        }
        for (int i = 0; i < monthRegionsSix.length; i++) {
            for (int j = 0; j < monthRegionsSix[i].length; j++) {
                Region region = new Region();
                region.set((j * cellW), (i * cellH), cellW + (j * cellW),
                        cellW + (i * cellH));
                monthRegionsSix[i][j] = region;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();


    }

    private void draw(Canvas canvas,int x,int y,int year,int month){
        canvas.save();
        canvas.translate(x,0);
        Paint paint = new Paint();
        paint.setColor(Color.LTGRAY);
        paint.setStyle(Paint.Style.STROKE);
        for (int i = 0; i < monthRegionsSix.length; i++) {
            for (int j = 0; j < monthRegionsSix[i].length; j++) {
//                canvas.drawRect(monthRegionsSix[i][j].getBounds(),paint);
                int topLeftCornerX = monthRegionsSix[i][j].getBounds().left;
                int topLeftCornerY = monthRegionsSix[i][j].getBounds().top;
                int bottomRightCornerX = monthRegionsSix[i][j].getBounds().right;
                int bottomRightCornerY = monthRegionsSix[i][j].getBounds().bottom;
                int rectHeight = monthRegionsSix[i][j].getBounds().height();
                int rectWidth = monthRegionsSix[i][j].getBounds().width();

                //画矩形上线条
                int tempLeftX = (j == 0 ? topLeftCornerX + 8 : topLeftCornerX);
                int tempRightX = (j == monthRegionsSix[i].length-1 ? bottomRightCornerX - 8 : bottomRightCornerX);
                canvas.drawLine(tempLeftX,
                        topLeftCornerY,
                        tempRightX,
                        bottomRightCornerY-rectHeight,paint);

                //下线条
                if(i == monthRegionsSix.length-1) {//只有最下边一条矩形框需要绘制底部线条
                    canvas.drawLine(tempLeftX,
                            topLeftCornerY + rectHeight,
                            tempRightX,
                            bottomRightCornerY, paint);
                }

                //左线条
                if(j!=0) {//最左边不画线条
                    canvas.drawLine(topLeftCornerX,
                            topLeftCornerY + 8,
                            bottomRightCornerX - rectWidth,
                            bottomRightCornerY - 8, paint);
                }
                //右线条
                if(j != monthRegionsSix[i].length-1) {//最右边不画线条
                    canvas.drawLine(topLeftCornerX + rectWidth,
                            topLeftCornerY + 8,
                            bottomRightCornerX,
                            bottomRightCornerY - 8, paint);
                }
            }
        }

        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setTextSize(50);
        String[][] dateData = DateUtils.buildMonthG(year,month);
        for (int i = 0; i < infoSix.length; i++) {
            for (int j = 0; j < infoSix[i].length; j++) {
                float yy = monthRegionsSix[i][j].getBounds().centerY()+paint.descent();
                canvas.drawText(dateData[i][j],monthRegionsSix[i][j].getBounds().centerX(),yy,paint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                if (Math.abs(deltaX) > 100) {

                }
                break;
            case MotionEvent.ACTION_UP:
                break;

        }


        mLastX = x;
        mLastY = y;
        return true;
    }
}
