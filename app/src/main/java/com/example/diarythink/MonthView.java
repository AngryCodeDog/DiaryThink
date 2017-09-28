package com.example.diarythink;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.View;

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

    public MonthView(Context context) {
        this(context,null);
    }

    public MonthView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public MonthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(measureWidth, (int) (measureWidth * 6F / 7F));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

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
        paint.setColor(Color.LTGRAY);
        paint.setStyle(Paint.Style.STROKE);
        for (int i = 0; i < monthRegionsSix.length; i++) {
            for (int j = 0; j < monthRegionsSix[i].length; j++) {
               canvas.drawRect(monthRegionsSix[i][j].getBounds(),paint);
            }
        }

        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setTextSize(50);
        String[][] dateData = DateUtils.buildMonthG(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH) + 1);


        for (int i = 0; i < infoSix.length; i++) {
            for (int j = 0; j < infoSix[i].length; j++) {
                float y = monthRegionsSix[i][j].getBounds().centerY()+paint.descent();
                canvas.drawText(dateData[i][j],monthRegionsSix[i][j].getBounds().centerX(),y,paint);
            }
        }
    }
}
