package com.ablanco.delayedactionview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;


/**
 * Created by Álvaro Blanco Cabrero on 21/1/17.
 * DelayedActionView.
 */

public class DelayedActionView extends View {

    private static final int DELAY_TIME = 3000;
    private int mDelayTime;

    private Bitmap mActionBitmap;
    private Paint mArcPaint = new Paint();
    private Paint mBgPaint = new Paint();
    private Paint mIconPaint = new Paint();
    private RectF mIconRect;
    private ActionListener mActionListener;
    private ValueAnimator mValueAnimator;
    private boolean mAnimationCanceled = false;
    private boolean mCanceledByUser = false;
    private float mCurrAngle = 0;

    private Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            mAnimationCanceled = false;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if (mActionListener != null && !mAnimationCanceled) mActionListener.onAction();
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            mAnimationCanceled = true;
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }
    };

    private ValueAnimator.AnimatorUpdateListener updateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            mCurrAngle = (float) animation.getAnimatedValue();
            invalidate();
        }
    };

    public DelayedActionView(Context context) {
        this(context, null);
    }

    public DelayedActionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DelayedActionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DelayedActionView, defStyleAttr, 0);
        mDelayTime = ta.getInteger(R.styleable.DelayedActionView_davDelay, DELAY_TIME);
        int actionIconRes = ta.getResourceId(R.styleable.DelayedActionView_davDrawable, R.drawable.ic_dismiss);
        int progressColor = ta.getColor(R.styleable.DelayedActionView_davProgressColor,
                ThemeUtils.getThemeColor(context, R.attr.colorAccent));
        int backGroundColor = ta.getColor(R.styleable.DelayedActionView_davBackGroundColor,
                ThemeUtils.getThemeColor(context, R.attr.colorPrimaryDark));
        int drawableTintColor = ta.getColor(R.styleable.DelayedActionView_davDrawableTintColor,
                ThemeUtils.getThemeColor(context, R.attr.colorPrimary));
        ta.recycle();

        mActionBitmap = BitmapFactory.decodeResource(getResources(), actionIconRes);

        //store icon's rect in order to draw inside its bounds
        mIconRect = new RectF(10, 10, mActionBitmap.getWidth() - 10, mActionBitmap.getHeight() - 10);

        mArcPaint.setColor(progressColor);
        mArcPaint.setAntiAlias(true);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeCap(Paint.Cap.SQUARE);
        mArcPaint.setStrokeWidth(12f);

        mBgPaint.setColor(backGroundColor);
        mBgPaint.setAlpha(180);
        mBgPaint.setAntiAlias(true);

        mIconPaint.setColorFilter(new PorterDuffColorFilter(drawableTintColor, PorterDuff.Mode.SRC_ATOP));

    }

    public void setDelayTime(int delayTime) {
        this.mDelayTime = delayTime;
    }

    public void setProgressColor(@ColorInt int color) {
        this.mArcPaint.setColor(color);
    }

    @Override
    public void setBackgroundColor(@ColorInt int color) {
        this.mBgPaint.setColor(color);
    }

    public void setIconDrawable(@DrawableRes int drawable) {
        this.setIconBitmap(BitmapFactory.decodeResource(getResources(), drawable));
    }

    public void setIconBitmap(Bitmap bitmap) {
        this.mActionBitmap = bitmap;
        this.requestLayout();
    }

    public void setIconTintColor(@ColorInt int color) {
        this.mIconPaint.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP));
    }

    public void start(ActionListener listener) {
        mCurrAngle = 0;
        mActionListener = listener;
        mCanceledByUser = false;
        startAnimation();
    }

    private void startAnimation() {

        if (mValueAnimator != null) mValueAnimator.cancel();

        mValueAnimator = ValueAnimator.ofFloat(mCurrAngle, 360);
        mValueAnimator.setDuration(getDeltaDelayedTime());
        mValueAnimator.addUpdateListener(updateListener);
        mValueAnimator.addListener(animatorListener);
        mValueAnimator.start();
    }

    /**
     * Calculated relative time needed to complete the animation based on current angle
     *
     * @return time needed for animation to be completed
     */
    private int getDeltaDelayedTime() {
        return (int) (((360 - mCurrAngle) / 360) * mDelayTime);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //draw bg circle
        canvas.drawCircle(mIconRect.centerX(), mIconRect.centerY(),
                Math.max(mIconRect.width() / 2, mIconRect.height() / 2), mBgPaint);
        //draw our start image
        canvas.drawBitmap(mActionBitmap, null, mIconRect, mIconPaint);
        //draw arc start progress
        canvas.drawArc(mIconRect, 0, mCurrAngle, false, mArcPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(mCanceledByUser) return false;

        int action = event.getAction() & event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                scaleDown();
                return true;
            case MotionEvent.ACTION_UP:
                scaleUp();
                //if we touch up inside our view bounds, lets notify a cancel start
                if (mIconRect.contains((int) event.getX(), (int) event.getY())) {
                    mCanceledByUser = true;
                    if (mValueAnimator != null) mValueAnimator.cancel();
                    if (mActionListener != null) mActionListener.onCanceled();
                }

                return true;
        }
        return super.onTouchEvent(event);
    }

    private void scaleDown() {
        animate().scaleY(0.8f)
                .scaleX(0.8f)
                .setInterpolator(new DecelerateInterpolator());
    }

    private void scaleUp() {
        animate().scaleY(1)
                .scaleX(1)
                .setInterpolator(new OvershootInterpolator());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //make the view as big as the maximum dimension of our start drawable
        //to make sure its completely visible
        int size = Math.max(mActionBitmap.getHeight(), mActionBitmap.getWidth());
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);

        switch (visibility) {
            case VISIBLE:
                //if we are visible again, check if the animation was previously canceled in order to
                //resume it
                if (mAnimationCanceled && !mCanceledByUser) startAnimation();
                break;
            case GONE:
            case INVISIBLE:
            default:
                //if we are not visible, cancel animation if running
                if (mValueAnimator != null && mValueAnimator.isRunning())
                    mValueAnimator.cancel();
                break;
        }
    }

    @Override
    public void onScreenStateChanged(int screenState) {
        super.onScreenStateChanged(screenState);

        switch (screenState) {
            case SCREEN_STATE_ON:
                //if screen turns on, check if the animation was previously canceled in order to
                //resume it
                if (mAnimationCanceled && !mCanceledByUser) startAnimation();
                break;
            case SCREEN_STATE_OFF:
                //if screen is off and animation is running, cancel it
                if (mValueAnimator != null && mValueAnimator.isRunning())
                    mValueAnimator.cancel();
                break;
        }
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //we are getting destroyed, so cancel animation if running
        if (mValueAnimator != null && mValueAnimator.isRunning()) mValueAnimator.cancel();
    }

    public interface ActionListener {
        void onAction();

        void onCanceled();
    }

}
