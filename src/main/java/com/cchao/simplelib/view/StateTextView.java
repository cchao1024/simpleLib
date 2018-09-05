package com.cchao.simplelib.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;

import com.cchao.simplelib.R;

/**
 * Description: https://github.com/niniloveyou/StateTextView
 * Created by cchao on 2017/6/10.
 */
/*
<div>
    Attribute              default value                     xml                             java

   normalTextColor        original text color            normalTextColor                setNormalTextColor(int color)
   pressedTextColor        original text color            pressedTextColor            setPressedTextColor(int color)
   unableTextColor         original text color            unableTextColor                setUnableTextColor(int color)
   strokeDashWidth              0                        strokeDashWidth                setStrokeDash(int dashWidth, int dashGap)
   strokeDashGap               0                        strokeDashGap                setStrokeDash(int dashWidth, int dashGap)
   normalStrokeWidth        0                        normalStrokeWidth            setNormalStrokeWidth(int widht)
   pressedStrokeWidth        0                        pressedStrokeWidth            setPressedStrokeWidth(int widht)
   unableStrokeWidth        0                        unableStrokeWidth            setUnableStrokeWidth(int widht)
   normalStrokeColor        0                        normalStrokeColor            setNormalStrokeColor(int color)
   pressedStrokeColor        0                        pressedStrokeColor            setPressedStrokeColor(int color)
   unableStrokeColor        0                        unableStrokeColor            setUnableStrokeColor(int color)
   normalBackgroundColor    0                        normalBackgroundColor        setNormalBackgroundColor(int color)
   pressedBackgroundColor    0                        pressedBackgroundColor        setPressedBackgroundColor(int color)
   unableBackgroundColor    0                        unableBackgroundColor        setUnableBackgroundColor(int color)
   radius                    0                        radius                        setRadius(int radius) / setRadius(float[] radii)
   round                    false                    round                        setRound(boolean round)
   animationDuration        0ms                        animationDuration            setAnimationDuration(int duration)

 </div>
 **/
public class StateTextView extends AppCompatTextView {

    //text color
    private int mNormalTextColor = 0;
    private int mPressedTextColor = 0;
    private int mUnableTextColor = 0;
    ColorStateList mTextColorStateList;

    //animation duration
    private int mDuration = 0;

    //radius
    private float mRadius = 0;
    private boolean mRound;

    //stroke
    private float mStrokeDashWidth = 0;
    private float mStrokeDashGap = 0;
    private int mNormalStrokeWidth = 0;
    private int mPressedStrokeWidth = 0;
    private int mUnableStrokeWidth = 0;
    private int mNormalStrokeColor = 0;
    private int mPressedStrokeColor = 0;
    private int mUnableStrokeColor = 0;

    //background color
    private int mNormalBackgroundColor = 0;
    private int mPressedBackgroundColor = 0;
    private int mUnableBackgroundColor = 0;

    private GradientDrawable mNormalBackground;
    private GradientDrawable mPressedBackground;
    private GradientDrawable mUnableBackground;

    private int[][] mStates;

    StateListDrawable mStateBackground;

    public StateTextView(Context context) {
        this(context, null);
    }

    public StateTextView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public StateTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup(attrs);
    }

    private void setup(AttributeSet attrs) {

        mStates = new int[4][];

        Drawable drawable = getBackground();
        if (drawable != null && drawable instanceof StateListDrawable) {
            mStateBackground = (StateListDrawable) drawable;
        } else {
            mStateBackground = new StateListDrawable();
        }

        mNormalBackground = new GradientDrawable();
        mPressedBackground = new GradientDrawable();
        mUnableBackground = new GradientDrawable();

        //pressed, focused, normal, unable
        mStates[0] = new int[]{android.R.attr.state_enabled, android.R.attr.state_pressed};
        mStates[1] = new int[]{android.R.attr.state_enabled, android.R.attr.state_focused};
        mStates[3] = new int[]{-android.R.attr.state_enabled};
        mStates[2] = new int[]{android.R.attr.state_enabled};

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.StateTextView);

        //get original text color as default
        //set text color
        mTextColorStateList = getTextColors();
        int mDefaultNormalTextColor = mTextColorStateList.getColorForState(mStates[2], getCurrentTextColor());
        int mDefaultPressedTextColor = mTextColorStateList.getColorForState(mStates[0], getCurrentTextColor());
        int mDefaultUnableTextColor = mTextColorStateList.getColorForState(mStates[3], getCurrentTextColor());
        mNormalTextColor = a.getColor(R.styleable.StateTextView_normalTextColor, mDefaultNormalTextColor);
        mPressedTextColor = a.getColor(R.styleable.StateTextView_pressedTextColor, mNormalTextColor);
        mUnableTextColor = a.getColor(R.styleable.StateTextView_unableTextColor, mNormalTextColor);
        setTextColor();

        //set animation duration
        mDuration = a.getInteger(R.styleable.StateTextView_animationDuration, mDuration);
        mStateBackground.setEnterFadeDuration(mDuration);
        mStateBackground.setExitFadeDuration(mDuration);

        //set background color
        mNormalBackgroundColor = a.getColor(R.styleable.StateTextView_normalBackgroundColor, 0);
        mPressedBackgroundColor = a.getColor(R.styleable.StateTextView_pressedBackgroundColor, 0);
        mUnableBackgroundColor = a.getColor(R.styleable.StateTextView_unableBackgroundColor, 0);
        mNormalBackground.setColor(mNormalBackgroundColor);
        mPressedBackground.setColor(mPressedBackgroundColor);
        mUnableBackground.setColor(mUnableBackgroundColor);

        //set radius
        mRadius = a.getDimensionPixelSize(R.styleable.StateTextView_radius, 0);
        mRound = a.getBoolean(R.styleable.StateTextView_round, false);
        mNormalBackground.setCornerRadius(mRadius);
        mPressedBackground.setCornerRadius(mRadius);
        mUnableBackground.setCornerRadius(mRadius);

        //set stroke
        mStrokeDashWidth = a.getDimensionPixelSize(R.styleable.StateTextView_strokeDashWidth, 0);
        mStrokeDashGap = a.getDimensionPixelSize(R.styleable.StateTextView_strokeDashWidth, 0);
        mNormalStrokeWidth = a.getDimensionPixelSize(R.styleable.StateTextView_normalStrokeWidth, 0);
        mPressedStrokeWidth = a.getDimensionPixelSize(R.styleable.StateTextView_pressedStrokeWidth, mNormalStrokeWidth);
        mUnableStrokeWidth = a.getDimensionPixelSize(R.styleable.StateTextView_unableStrokeWidth, mNormalStrokeWidth);
        mNormalStrokeColor = a.getColor(R.styleable.StateTextView_normalStrokeColor, 0);
        mPressedStrokeColor = a.getColor(R.styleable.StateTextView_pressedStrokeColor, mNormalStrokeColor);
        mUnableStrokeColor = a.getColor(R.styleable.StateTextView_unableStrokeColor, mNormalStrokeColor);
        setStroke();

        //set background
        mStateBackground.addState(mStates[0], mPressedBackground);
        mStateBackground.addState(mStates[1], mPressedBackground);
        mStateBackground.addState(mStates[3], mUnableBackground);
        mStateBackground.addState(mStates[2], mNormalBackground);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setBackground(getRippleDrawable(ColorStateList.valueOf(mPressedBackgroundColor), mNormalBackground, mPressedBackground));
        } else {
            setBackgroundDrawable(mStateBackground);
        }
        a.recycle();
        setGravity(Gravity.CENTER);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setRound(mRound);
    }

    /****************** stroke color *********************/

    public void setNormalStrokeColor(@ColorInt int normalStrokeColor) {
        this.mNormalStrokeColor = normalStrokeColor;
        setStroke(mNormalBackground, mNormalStrokeColor, mNormalStrokeWidth);
    }

    public void setPressedStrokeColor(@ColorInt int pressedStrokeColor) {
        this.mPressedStrokeColor = pressedStrokeColor;
        setStroke(mPressedBackground, mPressedStrokeColor, mPressedStrokeWidth);
    }

    public void setUnableStrokeColor(@ColorInt int unableStrokeColor) {
        this.mUnableStrokeColor = unableStrokeColor;
        setStroke(mUnableBackground, mUnableStrokeColor, mUnableStrokeWidth);
    }

    public void setStateStrokeColor(@ColorInt int normal, @ColorInt int pressed, @ColorInt int unable) {
        mNormalStrokeColor = normal;
        mPressedStrokeColor = pressed;
        mUnableStrokeColor = unable;
        setStroke();
    }

    /****************** stroke width *********************/

    public void setNormalStrokeWidth(int normalStrokeWidth) {
        this.mNormalStrokeWidth = normalStrokeWidth;
        setStroke(mNormalBackground, mNormalStrokeColor, mNormalStrokeWidth);
    }

    public void setPressedStrokeWidth(int pressedStrokeWidth) {
        this.mPressedStrokeWidth = pressedStrokeWidth;
        setStroke(mPressedBackground, mPressedStrokeColor, mPressedStrokeWidth);
    }

    public void setUnableStrokeWidth(int unableStrokeWidth) {
        this.mUnableStrokeWidth = unableStrokeWidth;
        setStroke(mUnableBackground, mUnableStrokeColor, mUnableStrokeWidth);
    }

    public void setStateStrokeWidth(int normal, int pressed, int unable) {
        mNormalStrokeWidth = normal;
        mPressedStrokeWidth = pressed;
        mUnableStrokeWidth = unable;
        setStroke();
    }

    public void setStrokeDash(float strokeDashWidth, float strokeDashGap) {
        this.mStrokeDashWidth = strokeDashWidth;
        this.mStrokeDashGap = strokeDashWidth;
        setStroke();
    }

    private void setStroke() {
        setStroke(mNormalBackground, mNormalStrokeColor, mNormalStrokeWidth);
        setStroke(mPressedBackground, mPressedStrokeColor, mPressedStrokeWidth);
        setStroke(mUnableBackground, mUnableStrokeColor, mUnableStrokeWidth);
    }

    private void setStroke(GradientDrawable mBackground, int mStrokeColor, int mStrokeWidth) {
        mBackground.setStroke(mStrokeWidth, mStrokeColor, mStrokeDashWidth, mStrokeDashGap);
    }

    /********************   radius  *******************************/

    public void setRadius(@FloatRange(from = 0) float radius) {
        this.mRadius = radius;
        mNormalBackground.setCornerRadius(mRadius);
        mPressedBackground.setCornerRadius(mRadius);
        mUnableBackground.setCornerRadius(mRadius);
    }

    public void setRound(boolean round) {
        this.mRound = round;
        int height = getMeasuredHeight();
        if (mRound) {
            setRadius(height / 2f);
        }
    }

    public void setRadius(float[] radii) {
        mNormalBackground.setCornerRadii(radii);
        mPressedBackground.setCornerRadii(radii);
        mUnableBackground.setCornerRadii(radii);
    }

    /********************  background color  **********************/

    public void setStateBackgroundColor(@ColorInt int normal, @ColorInt int pressed, @ColorInt int unable) {
        mPressedBackgroundColor = normal;
        mNormalBackgroundColor = pressed;
        mUnableBackgroundColor = unable;
        mNormalBackground.setColor(mNormalBackgroundColor);
        mPressedBackground.setColor(mPressedBackgroundColor);
        mUnableBackground.setColor(mUnableBackgroundColor);
    }

    public void setNormalBackgroundColor(@ColorInt int normalBackgroundColor) {
        this.mNormalBackgroundColor = normalBackgroundColor;
        mNormalBackground.setColor(mNormalBackgroundColor);
    }

    public void setPressedBackgroundColor(@ColorInt int pressedBackgroundColor) {
        this.mPressedBackgroundColor = pressedBackgroundColor;
        mPressedBackground.setColor(mPressedBackgroundColor);
    }

    public void setUnableBackgroundColor(@ColorInt int unableBackgroundColor) {
        this.mUnableBackgroundColor = unableBackgroundColor;
        mUnableBackground.setColor(mUnableBackgroundColor);
    }

    /*******************alpha animation duration********************/
    public void setAnimationDuration(@IntRange(from = 0) int duration) {
        this.mDuration = duration;
        mStateBackground.setEnterFadeDuration(mDuration);
    }

    /***************  text color   ***********************/

    private void setTextColor() {
        int[] colors = new int[]{mPressedTextColor, mPressedTextColor, mNormalTextColor, mUnableTextColor};
        mTextColorStateList = new ColorStateList(mStates, colors);
        setTextColor(mTextColorStateList);
    }

    public void setStateTextColor(@ColorInt int normal, @ColorInt int pressed, @ColorInt int unable) {
        this.mNormalTextColor = normal;
        this.mPressedTextColor = pressed;
        this.mUnableTextColor = unable;
        setTextColor();
    }

    public void setNormalTextColor(@ColorInt int normalTextColor) {
        this.mNormalTextColor = normalTextColor;
        setTextColor();

    }

    public void setPressedTextColor(@ColorInt int pressedTextColor) {
        this.mPressedTextColor = pressedTextColor;
        setTextColor();
    }

    public void setUnableTextColor(@ColorInt int unableTextColor) {
        this.mUnableTextColor = unableTextColor;
        setTextColor();
    }

    //-----------
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private Drawable getRippleDrawable(ColorStateList colorStateList, Drawable defaultDrawable, Drawable focusDrawable) {
        return new RippleDrawable(colorStateList, defaultDrawable, focusDrawable);
    }
}