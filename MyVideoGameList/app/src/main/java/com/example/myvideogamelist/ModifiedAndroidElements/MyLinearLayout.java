package com.example.myvideogamelist.ModifiedAndroidElements;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.core.view.MotionEventCompat;

import com.example.myvideogamelist.GamesListActivity;
import com.example.myvideogamelist.InterfacesAppli.MyActivityImageDiplayable;

public class MyLinearLayout extends LinearLayout {

    private int mTouchSlop;
    private boolean mIsScrolling;
    private float mDownX;
    private boolean mShown;
    private MyActivityImageDiplayable activity;
    private float x1 = 0, x2 = 0;

    /**
     * Initialize data for calculs
     */
    private void init(){
        ViewConfiguration vc = ViewConfiguration.get(getContext());
        mTouchSlop = vc.getScaledTouchSlop();
    }

    /**
     * Set the activity running
     * @param activity running activity
     */
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

    /**
     * return distance between two points
     * @param ev last movement made by user
     * @return distance between the points
     */
    private int calculateDistanceX(MotionEvent ev) {
        return (int) (ev.getRawX() - mDownX);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        // Here we actually handle the touch event (e.g. if the action is ACTION_MOVE,
        // scroll this container).
        // This method will only be called if the touch event was intercepted in
        // onInterceptTouchEvent
        System.out.println("Entered");
        int MIN_DISTANCE = 150;
        switch(motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = motionEvent.getX();
                return false;
            case MotionEvent.ACTION_UP:
                x2 = motionEvent.getX();
                float deltaX = x2 - x1;
                System.out.println("ici");
                if (Math.abs(deltaX) > MIN_DISTANCE){
                    if (x2 > x1){// Left to right swipe action
                        System.out.println("left2right swipe");
                        if(((GamesListActivity)activity).getCurrentButtonInd() > 0){
                            ((GamesListActivity)activity).getArraybuttons().get(((GamesListActivity)activity).getCurrentButtonInd() - 1).performClick();
                            ((GamesListActivity)activity).getHorizontalScrollView().requestChildFocus(((GamesListActivity)activity).getArraybuttons().get(((GamesListActivity)activity).getCurrentButtonInd()),  ((GamesListActivity)activity).getArraybuttons().get(((GamesListActivity)activity).getCurrentButtonInd()));
                        }
                    }
                    else{// Right to left swipe action
                        System.out.println("right2left swipe");
                        if(((GamesListActivity)activity).getCurrentButtonInd() < ((GamesListActivity)activity).getArraybuttons().size() - 1){
                            ((GamesListActivity)activity).getArraybuttons().get(((GamesListActivity)activity).getCurrentButtonInd() + 1).performClick();
                            ((GamesListActivity)activity).getHorizontalScrollView().requestChildFocus( ((GamesListActivity)activity).getArraybuttons().get(((GamesListActivity)activity).getCurrentButtonInd()),  ((GamesListActivity)activity).getArraybuttons().get(((GamesListActivity)activity).getCurrentButtonInd()));
                        }
                    }
                }
                else{
                    // consider as something else - a screen tap for example
                }
                break;
        }
        return true;
    }

    //**********************************************************************************************
    //Constructor area
    public MyLinearLayout(Context context) {
        super(context);
        init();
    }

    public MyLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
}
