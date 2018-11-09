package com.zhao.damprelativelayout;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

/**
 * 创建者 ：赵鹏   时间：2018/11/6
 */
public class DampRelativeLayout extends RelativeLayout{

    private float mLastY = -1;
    private int mOriginalHeight;

    private float mDampValue = 3.0f;

    private View mDestView;

    public DampRelativeLayout(Context context) {
        this(context, null);
    }

    public DampRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DampRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init(){
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mLastY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float difY = event.getRawY() - mLastY;
                mLastY = event.getRawY();
                if(!canChildScrollUp()){
                    onMove(difY / mDampValue);
                }
                break;
            default:
                mLastY = -1;
                if(getCurHeight() > mOriginalHeight){
                    smoothScrollTo(mOriginalHeight);
                }
                break;

        }
        return super.dispatchTouchEvent(event);
    }

    private void onMove(float y){
        ViewGroup.LayoutParams params = mDestView.getLayoutParams();
        if((int) (params.height + y) > mOriginalHeight){
            params.height = (int) (params.height + y);
            mDestView.setLayoutParams(params);
        }
    }

    public void setDestView(View view){
        this.mDestView = view;
        mOriginalHeight = mDestView.getMeasuredHeight();
    }

    private void smoothScrollTo(int destHeight) {
        ValueAnimator animator = ValueAnimator.ofInt(getCurHeight(), destHeight);
        animator.setDuration(300).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                setCurHeight((int) animation.getAnimatedValue());
            }
        });
        animator.start();
    }

    private int getCurHeight(){
        if(mDestView != null){
            return mDestView.getLayoutParams().height;
        }else{
            return 0;
        }
    }

    private void setCurHeight(int newHeight){
        if(mDestView != null){
            ViewGroup.LayoutParams params = mDestView.getLayoutParams();
            params.height = newHeight;
            mDestView.setLayoutParams(params);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(getCurHeight() > mOriginalHeight){
            return true;
        }else{
            return super.onInterceptTouchEvent(ev);
        }
    }

    public boolean canChildScrollUp() {
        boolean isChildCanScroll = false;
        if(getCurHeight() > mOriginalHeight){
            return false;
        }
        for(int i = 0; i < getChildCount(); i++){
            View child = getChildAt(i);
            if (android.os.Build.VERSION.SDK_INT < 14) {
                if (child instanceof AbsListView) {
                    final AbsListView absListView = (AbsListView) child;
                    isChildCanScroll =  absListView.getChildCount() > 0
                            && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                            .getTop() < absListView.getPaddingTop());
                } else {
                    isChildCanScroll =  ViewCompat.canScrollVertically(child, -1) || child.getScrollY() != 0;
                }
            } else {
                isChildCanScroll =  ViewCompat.canScrollVertically(child, -1);
            }
            if(isChildCanScroll){
                break;
            }
        }
        return isChildCanScroll;
    }
}
