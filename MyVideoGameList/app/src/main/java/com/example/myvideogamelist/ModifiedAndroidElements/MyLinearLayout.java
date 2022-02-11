package com.example.myvideogamelist.ModifiedAndroidElements;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.core.view.MotionEventCompat;

import com.example.myvideogamelist.InterfacesAppli.Scrollable_horizontally;

public class MyLinearLayout extends LinearLayout {

    private int mTouchSlop;
    private boolean mIsScrolling;
    private float mDownX;
    private boolean mShown;

    private boolean isCard;
    private Scrollable_horizontally activity;
    private float x1 = 0, x2 = 0, y1 = 0, y2 = 0;
    static final int MIN_DISTANCE = 150;

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
    public void setActivity(Scrollable_horizontally activity){
        this.activity = activity;
    }


    /**
     * Setter for boolean is a card
     * @param card true is the linearLayout is a card
     */
    public void setCard(boolean card) {
        isCard = card;
    }


    /**
     * This method JUST determines whether we want to intercept the motion.
     * If we return true, onTouchEvent will be called and we do the actual
     * scrolling there.
     * @param ev the user's motion
     * @return boolean to let child's know that we handle or not the motion
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

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

    /**
     * Actions to performs if actual motion is interesting for this
     * @param motionEvent the user's motion
     * @return boolean to let child's know that we handle or not the motion
     */
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        System.out.println("Entered");
        if(! isCard)
            getParent().requestDisallowInterceptTouchEvent(true);
        switch(motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = motionEvent.getX();
                y1 = motionEvent.getY();
                System.out.println(x1+ " x1, y1 :" + y1);
                return true;
            case MotionEvent.ACTION_UP:
                x2 = motionEvent.getX();
                y2 = motionEvent.getY();
                System.out.println(x2 + " x2, y2 : " + y2 +" up");
                return actionsScroll();
            case MotionEvent.ACTION_MOVE: return true;
            case MotionEvent.ACTION_CANCEL: System.out.println("canceled");
        }
        return super.onTouchEvent(motionEvent);
    }

    /**
     * Lateral scroll if possible and motion correspond, else delegate to children
     * @return boolean, true or false if we handle the event or not
     */
    private boolean actionsScroll(){
        if((y1 - y2 < 100 || y2 - y1 < 100) && (x1 - x2 > MIN_DISTANCE || x2 - x1 > MIN_DISTANCE)){
            if(x2 > x1){
                //left2right swipe"
                activity.scrollReceived(0);
            }
            else {
                //right2left swipe
                activity.scrollReceived(1);
            }
            return true;
        }
        else{
            // consider as something else - a screen tap for example
            if(isCard){
                this.performClick();
                return true;
            }
            return false;
        }
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
