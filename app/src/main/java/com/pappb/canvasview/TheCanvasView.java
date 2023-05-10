package com.pappb.canvasview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

public class TheCanvasView extends View {
    private Paint thePaint;
    private Path thePath;
    private int theColor;
    private int backColor;
    private Canvas theCanvas;
    private Bitmap theBitmap;
    private Rect rect;

    private float theX, theY;
    private static final float TOUCH_TOLERANCE = 4;

    public TheCanvasView(Context context) {
        this(context, null);
    }

    public TheCanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        backColor = ResourcesCompat.getColor(getResources(), R.color.opaque_orange, null);
        theColor = ResourcesCompat.getColor(getResources(), R.color.opaque_yellow, null);
        thePath = new Path();
        thePaint = new Paint();

        thePaint.setColor(theColor);
        thePaint.setAntiAlias(true);
        thePaint.setDither(true);

        thePaint.setStyle(Paint.Style.STROKE);
        thePaint.setStrokeJoin(Paint.Join.ROUND);
        thePaint.setStrokeCap(Paint.Cap.ROUND);
        thePaint.setStrokeWidth(12);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        theBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        theCanvas = new Canvas(theBitmap);
        theCanvas.drawColor(backColor);

        int inset = 40;
        rect = new Rect(inset, inset, w - inset, h - inset);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(theBitmap, 0, 0, null);
        canvas.drawRect(rect, thePaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStart(x, y);
                break;

            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                touchUp();
                break;

            default:
        }
        return true;
    }

    private void touchStart(float x, float y) {
        thePath.moveTo(x, y);
        theX = x;
        theY = y;
    }

    private void touchMove(float x, float y) {
        float dx = Math.abs(x - theX);
        float dy = Math.abs(y - theY);

        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            thePath.quadTo(theX, theY, (x + theX) / 2, (y + theY) / 2);
            theX = x;
            theY = y;

            theCanvas.drawPath(thePath, thePaint);
        }
    }

    private void touchUp() {
        thePath.reset();
    }
}
