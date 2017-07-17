package com.guoqiangma.circlemove;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


public class CircleMoveView extends View {
    public CircleMoveView(Context context) {
        this(context, null);
    }

    public CircleMoveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleMoveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = getMeasuredWidth();
        setMeasuredDimension(measuredWidth, measuredWidth);
        if (width == 0) {
            width = measuredWidth;
            initX = width - ringWidth / 2.0f;
            initY = width / 2.0f - (float) Math.sqrt(Math.pow(width / 2.0f - ringWidth / 2.0f, 2)
                    - Math.pow(ringWidth / 2.0f, 2));
            moveX = initX;
            moveY = initY;

            halfWidth = width / 2.0f;
            centerX = width;
            centerY = halfWidth;
        }
    }

    private Paint paint;

    private void init() {
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
    }


    private int width;
    private float circleCenterWidth = 30.0f;
    private float ringWidth = 100;
    private float initX, initY;
    private float moveX, moveY;
    private float centerX, centerY;
    private float halfWidth;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(width, halfWidth, circleCenterWidth, paint);
        canvas.drawCircle(width, halfWidth, halfWidth, paint);
        canvas.drawCircle(width, halfWidth, halfWidth - ringWidth, paint);
        canvas.drawCircle(moveX, moveY, ringWidth / 2.0f, paint);
    }

    private float tempX, tempY;

    private void calculatePoint(float x, float y) {
        double dx = Math.abs(width - x);
        double dy = Math.abs(halfWidth - y);
        double dxy = Math.sqrt(Math.pow(dx, 2)
                + Math.pow(dy, 2));
        double yinshu = (halfWidth - ringWidth / 2.0) / dxy;

        if (y < halfWidth) {
            tempX = (float) (centerX - dx * yinshu);
            tempY = (float) (centerY - dy * yinshu);
        } else {
            tempX = (float) (centerX - dx * yinshu);
            tempY = (float) (centerY + dy * yinshu);
        }

    }

    private boolean isTouched = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                double dxy = Math.sqrt(Math.pow(x - initX, 2)
                        + Math.pow(y - initY, 2));
                isTouched = dxy < ringWidth/2.0f;
                //System.out.println(isTouched);
                break;
            case MotionEvent.ACTION_MOVE:
                if (isTouched){
                    calculatePoint(x,y);
                    moveX = tempX;
                    moveY = tempY;

                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isTouched = false;

                moveX = initX;
                moveY = initY;
                invalidate();
                break;
        }

        return isTouched;
    }
}
