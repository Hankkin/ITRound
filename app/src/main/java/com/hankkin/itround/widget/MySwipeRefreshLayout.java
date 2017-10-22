package com.hankkin.itround.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Created by hankkin on 2017/10/16.
 * Blog: http://hankkin.cn
 * Mail: 1019283569@qq.com
 */

public class MySwipeRefreshLayout extends SwipeRefreshLayout {

    private int mTouchSlop;
    private float mPrevX;

    public MySwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        //判断用户在进行滑动操作的最小距离
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPrevX = MotionEvent.obtain(event).getX();
                break;

            case MotionEvent.ACTION_MOVE:
                final float eventX = event.getX();
                //获取水平移动距离
                float xDiff = Math.abs(eventX - mPrevX);
                //当水平移动距离大于滑动操作的最小距离的时候就认为进行了横向滑动
                //不进行事件拦截,并将这个事件交给子View处理
                if (xDiff > mTouchSlop) {
                    return false;
                }
        }

        return super.onInterceptTouchEvent(event);

    }
}
