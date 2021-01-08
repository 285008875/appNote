package com.maimemo.android.memonote.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class VerticalSwipeRefreshLayout extends SwipeRefreshLayout {

    private int mTouchSlop;
    // 上一次触摸时的X坐标
    private float mPrevX;

    public VerticalSwipeRefreshLayout(@NonNull Context context) {
        super(context);
    }

    public VerticalSwipeRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPrevX = event.getX();
                break;

            case MotionEvent.ACTION_MOVE:
                final float eventX = event.getX();
                float xDiff = Math.abs(eventX - mPrevX);
                 Log.d("refresh" ,"move----" + eventX + "  " +mPrevX+"  " + xDiff + "  "+  "   " + mTouchSlop);
                // 增加60的容差，让下拉刷新在竖直滑动时就可以触发
//                Log.e("TAG", "onInterceptTouchEvent: "+xDiff  +"  " +mTouchSlop   );
//                Log.i("TAG", "onInterceptTouchEvent: 测试xDiff"+ xDiff );
//                Log.i("TAG", "onInterceptTouchEvent: 测试mTouchSlop "+( mTouchSlop+60));
                if (xDiff > mTouchSlop+60) {

                    Log.i("refresh", "onInterceptTouchEvent: 测试内" +xDiff+ " " +mTouchSlop);
                    return false;
                }
                break;
        }

        return super.onInterceptTouchEvent(event);
    }
}
