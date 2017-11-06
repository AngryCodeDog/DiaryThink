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
import android.widget.Scroller;

import com.example.diarythink.utils.DateUtils;

import java.util.Calendar;

/**
 * Created by zhuyupei on 2017/9/26 0026.
 */

public class MonthView extends View {

    private final Region[][] monthRegionsSix = new Region[6][7];


    /**
     * 当前界面显示的年份
     */
    private int curPageYear;
    /**
     * 目前界面显示的月份
     */
    private int curPageMonth;
    /**
     * 真实的当前月份
     */
    private int realCurMonth;

    /**
     * 真实的当前年份
     */
    private int realCurYear;



    /**
     * 点击事件ACTION_DOWN的x坐标
     */
    int downX = 0;
    /**
     * 点击事件ACTION_DOWN的y坐标
     */
    int downY = 0;

    /**当前界面所在下标*/
    int curPageIndex = 0;

    /**
     * view上一次移动后的最后位置
     */
    int lastMoveX = 0;
    /**
     * 屏幕宽度
     */
    private int width;

    private Scroller mScroller;

    OnMonthChangeListener onMonthChangeListener;

    private String[][] dateData;

    private Canvas mCanvas;


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
        curPageYear = calendar.get(Calendar.YEAR);
        realCurYear = calendar.get(Calendar.YEAR);
        curPageMonth = calendar.get(Calendar.MONTH);
        realCurMonth = calendar.get(Calendar.MONTH);

        mScroller = new Scroller(context);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(measureWidth, (int) (measureWidth * 6F / 7F));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        width = w;
        initRegion(w, h);
    }

    private void initRegion(int w, int h) {
        int cellW = (int) (w / 7F);
        int cellH = (int) (h / 6F);

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
        mCanvas = canvas;
        draw(canvas,(width* (curPageIndex - 1)),0, curPageYear, curPageMonth);

        draw(canvas,width * curPageIndex,0, curPageYear, curPageMonth +1);

        draw(canvas,width * (curPageIndex + 1),0, curPageYear, curPageMonth +2);

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
        paint.setTextSize(25);
        dateData = DateUtils.buildMonthG(year,month);
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                float yy = monthRegionsSix[i][j].getBounds().centerY()-(paint.descent()+paint.ascent())/2;
                canvas.drawText(dateData[i][j],monthRegionsSix[i][j].getBounds().centerX()-paint.descent(),yy,paint);
            }
        }
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mScroller.forceFinished(true);
                downX = (int) event.getRawX();
                downY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(downX - event.getX()) > 100) {
                    int totalMoveX = (int) (downX - event.getX()) + lastMoveX ;
                    smoothScrollTo(totalMoveX, 0);
                } else if (Math.abs(downY - event.getY()) > 50) {
                }
                break;
            case MotionEvent.ACTION_UP:

                if (Math.abs(downX - event.getX()) > 25) {
                    if (downX > event.getX()
                            && Math.abs(downX - event.getX()) >= width/5) {
                        curPageIndex++;
                    }else if (downX < event.getX()
                            && Math.abs(downX - event.getX()) >= width/5) {
                        curPageIndex--;
                    }
                }
                smoothScrollTo(width * curPageIndex, 0);
                //获取上一次滑动的距离
                lastMoveX = width * curPageIndex;
                //当前界面显示的月份
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MONTH,curPageIndex);
                curPageMonth = calendar.get(Calendar.MONTH);
                curPageYear = calendar.get(Calendar.YEAR);

                Log.e("TAG", curPageYear +"-"+(1+curPageMonth));
                Log.e("TAG","curPageIndex="+curPageIndex+"");
                if(onMonthChangeListener != null){
                    onMonthChangeListener.monthChange(curPageMonth,curPageYear);
                }


                monthRegionsSix[0][0].getBounds().contains((int)event.getRawX(),(int)event.getRawY());
                int dateX = Math.round(event.getX()/monthRegionsSix[0][0].getBounds().width()) - 1;
                int dateY = Math.round(event.getY()/monthRegionsSix[0][0].getBounds().height()) - 1;
                dateData = DateUtils.buildMonthG(curPageYear,curPageMonth+1);
                Log.e("TAG",curPageMonth+1 + "-" +dateData[dateY][dateX]);



                break;
        }

        return super.onTouchEvent(event);
    }

    private void smoothScrollTo(int fx, int fy) {
        //预计目标点 到  view前一次滚动后 的最终点 的距离，即这次view需要滚动多远
        int dx = fx - mScroller.getFinalX();
        int dy = fy - mScroller.getFinalY();
        smoothScrollBy(dx, dy);
    }


    private void smoothScrollBy(int dx, int dy) {
        mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx,dy, 500);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }

    public void setOnMonthChangeListener(OnMonthChangeListener onMonthChangeListener){
        this.onMonthChangeListener = onMonthChangeListener;
    }


    public interface OnMonthChangeListener{
        void monthChange(int month,int year);
    }

    public interface OnClickDateListener{
        void clickDate(int month,int date);
    }

}
