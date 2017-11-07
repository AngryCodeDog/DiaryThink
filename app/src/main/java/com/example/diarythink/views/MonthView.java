package com.example.diarythink.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Region;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import com.example.diarythink.bean.DateInfo;
import com.example.diarythink.utils.DateUtils;

import java.util.Calendar;

/**
 * Created by zhuyupei on 2017/9/26 0026.
 * 通过一个6*7的数组来展现日历数据
 * 通过三个6*7的数组来分别展示左右和当前月份的数据
 */

public class MonthView extends View {

    private final Region[][] monthRegionsSix = new Region[6][7];


    /**
     * 当前界面显示的年份
     */
    private int curPageYear;
    /**
     * 目前界面显示的月份(比实际月份少1）
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
    OnClickDateListener onClickDateListener;


    private DateInfo[][] dateInfoArray = new DateInfo[6][7];

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

        //初始化6*7的绘图区域
        for (int i = 0; i < monthRegionsSix.length; i++) {
            for (int j = 0; j < monthRegionsSix[i].length; j++) {
                Region region = new Region();
                region.set((j * cellW), (i * cellH), cellW + (j * cellW),
                        cellW + (i * cellH));
                monthRegionsSix[i][j] = region;
            }
        }
    }

    public void setCurMonth(int month,int year){
//        curPageIndex = 0;
        curPageMonth = month;
        curPageYear = year;
        lastMoveX = 0;
        requestLayout();
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        mCanvas = canvas;
        Log.e("TAG","MonthView--onDraw");
        Calendar calendar = Calendar.getInstance();
        calendar.set(curPageYear,curPageMonth,1);
        //画左边界面的日历
        calendar.add(Calendar.MONTH,-1);
        draw(canvas,(width* (curPageIndex - 1)),0, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));
        //画当前界面的日历
        calendar.add(Calendar.MONTH,1);
        draw(canvas,width * curPageIndex,0, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));
        //画右边界面的日历
        calendar.add(Calendar.MONTH,1);
        draw(canvas,width * (curPageIndex + 1),0,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH));

    }

    private void draw(Canvas canvas,int x,int y,int year,int month){
        Log.e("TAG","MonthView--Draw: "+year+"-"+month);
        canvas.save();
        canvas.translate(x,0);
        Paint paint = new Paint();
        paint.setColor(Color.LTGRAY);
        paint.setStyle(Paint.Style.STROKE);
        for (int i = 0; i < monthRegionsSix.length; i++) {
            for (int j = 0; j < monthRegionsSix[i].length; j++) {
                int topLeftCornerX = monthRegionsSix[i][j].getBounds().left;
                int topLeftCornerY = monthRegionsSix[i][j].getBounds().top;
                int bottomRightCornerX = monthRegionsSix[i][j].getBounds().right;
                int bottomRightCornerY = monthRegionsSix[i][j].getBounds().bottom;
                int rectHeight = monthRegionsSix[i][j].getBounds().height();
                int rectWidth = monthRegionsSix[i][j].getBounds().width();

                //画矩形区域上线条
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
        dateInfoArray = DateUtils.buildMonthG(year,month);
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if(dateInfoArray[i][j].date != 0 ) {
                    float yy = monthRegionsSix[i][j].getBounds().centerY() - (paint.descent() + paint.ascent()) / 2;
                    canvas.drawText(dateInfoArray[i][j].date + "", monthRegionsSix[i][j].getBounds().centerX() - paint.descent(), yy, paint);
                    if(!TextUtils.isEmpty(dateInfoArray[i][j].desc)){//如果有备注信息，则绘制备注信息
                        Paint paint1 = new Paint();
                        paint1.setColor(Color.YELLOW);
                        paint1.setStyle(Paint.Style.STROKE);
                        paint1.setStrokeWidth(3f);
                        canvas.drawRect(monthRegionsSix[i][j].getBounds(),paint1);
                        paint1.setStrokeWidth(1f);
                        paint1.setTextSize(20);
                        paint1.setStyle(Paint.Style.FILL_AND_STROKE);
                        canvas.drawText(dateInfoArray[i][j].desc,monthRegionsSix[i][j].getBounds().left + paint1.descent(), yy-(paint.ascent()+paint.descent())+5, paint1);
                    }
                }
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

                //当前界面显示的月份
                Calendar calendar = Calendar.getInstance();
                calendar.set(curPageYear,curPageMonth,1);//更改到当前界面的日期

                if (Math.abs(downX - event.getX()) > 25) {
                    if (downX > event.getX()
                            && Math.abs(downX - event.getX()) >= width/5) {
                        curPageIndex++;
                        calendar.add(Calendar.MONTH,1);//切换日期
                    }else if (downX < event.getX()
                            && Math.abs(downX - event.getX()) >= width/5) {
                        curPageIndex--;
                        calendar.add(Calendar.MONTH,-1);//切换日期
                    }
                }
                smoothScrollTo(width * curPageIndex, 0);
                //保存滑动的距离
                lastMoveX = width * curPageIndex;

                curPageMonth = calendar.get(Calendar.MONTH);
                curPageYear = calendar.get(Calendar.YEAR);

//                Log.e("TAG","curPageIndex="+curPageIndex);

                if(Math.abs(downX - event.getX()) > 25 && Math.abs(downX - event.getX()) >= width/5 && onMonthChangeListener != null){//界面是否切换了月份
                    onMonthChangeListener.monthChange(curPageMonth,curPageYear);
                }

                if (Math.abs(downX - event.getX()) < 10) {//判断是点击还是滑动
                    //直接通过坐标来计算出所属区域，再得到日期信息
                    int dateX = (int) (Math.ceil(event.getX()/monthRegionsSix[0][0].getBounds().width()) - 1);
                    int dateY = (int) (Math.ceil(event.getY()/monthRegionsSix[0][0].getBounds().height()) - 1);
                    dateInfoArray = DateUtils.buildMonthG(curPageYear,curPageMonth+1);
                    if (onClickDateListener != null) {
                        dateX = dateX > 0 ? dateX : 0;
                        dateY = dateY > 0 ? dateY : 0;
                        onClickDateListener.clickDate(dateInfoArray[dateY][dateX].month, dateInfoArray[dateY][dateX].date);
                    }
                }
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
    public void setOnClickDateListener(OnClickDateListener onClickDateListener){
        this.onClickDateListener = onClickDateListener;
    }


    public interface OnMonthChangeListener{
        void monthChange(int month,int year);
    }

    public interface OnClickDateListener{
        void clickDate(int month,int date);
    }

}
