package com.example.diarythink.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.example.diarythink.R;

/**
 * Created by zhuyupei on 2017/9/28 0028.
 */

public class ItemTaskView extends LinearLayout {
    public ItemTaskView(Context context) {
        this(context,null);
    }

    public ItemTaskView(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public ItemTaskView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.thingsview, this);
    }
}
