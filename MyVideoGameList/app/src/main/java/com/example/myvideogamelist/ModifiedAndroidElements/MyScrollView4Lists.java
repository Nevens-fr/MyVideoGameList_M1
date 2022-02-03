package com.example.myvideogamelist.ModifiedAndroidElements;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

import androidx.core.view.MotionEventCompat;

import com.example.myvideogamelist.GamesListActivity;
import com.example.myvideogamelist.InterfacesAppli.MyActivityImageDiplayable;
import com.example.myvideogamelist.R;

public class MyScrollView4Lists extends ScrollView {
    private int mTouchSlop;
    private boolean mIsScrolling;
    private float mDownX;
    private boolean mShown;
    private MyActivityImageDiplayable activity;
    private float x1 = 0, x2 = 0;

    public MyScrollView4Lists(Context context) {
        super(context);
        init();
    }

    public MyScrollView4Lists(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyScrollView4Lists(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MyScrollView4Lists(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        ViewConfiguration vc = ViewConfiguration.get(getContext());
        mTouchSlop = vc.getScaledTouchSlop();
    }

    public void setActivity(MyActivityImageDiplayable activity){
        this.activity = activity;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        /*
         * This method JUST determines whether we want to intercept the motion.
         * If we return true, onTouchEvent will be called and we do the actual
         * scrolling there.
         */
        final int action = MotionEventCompat.getActionMasked(ev);

        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mIsScrolling = false;
            return false;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mShown = false;
                mDownX = ev.getRawX();
                return false;
            case MotionEvent.ACTION_MOVE:
                if (mIsScrolling) {
                    return true;
                }

                final int xDiff = calculateDistanceX(ev);
                if (xDiff > mTouchSlop) {
                    mIsScrolling = true;
                    return true;
                }
                break;
        }
        // In general, we don't want to intercept touch events. They should be
        // handled by the child view.
        return false;
    }

    private int calculateDistanceX(MotionEvent ev) {
        return (int) (ev.getRawX() - mDownX);
    }
}
