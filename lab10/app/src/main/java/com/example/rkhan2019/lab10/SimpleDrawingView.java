package com.example.rkhan2019.lab10;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

public class SimpleDrawingView extends View {

    // set up final color
    public int paintColor = Color.BLACK;
    private Paint drawPaint;
    private Path path = new Path();
    private int paintChanges = 0;
    private int bgChanges = 0;
    public int[] colorOptions = {Color.BLACK, Color.RED, Color.MAGENTA, Color.BLUE};
    public int[] colorOptions2 = {Color.CYAN, Color.GREEN, Color.LTGRAY, Color.DKGRAY};
    public int[] currentColors = colorOptions;

    public SimpleDrawingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setupPaint();
    }

    // setup paint with color and stroke styles
    private void setupPaint(){
        drawPaint = new Paint();
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(5);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
    }


    // draw new circle onto the view
    @Override
    protected void onDraw(Canvas canvas){
        canvas.drawPath(path, drawPaint);
    }

    // Append new circle each time user presses on screen
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float pointX = event.getX();
        float pointY = event.getY();
        // Checks for the event that occurs
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Starts a new line in the path
                path.moveTo(pointX, pointY);
                break;
            case MotionEvent.ACTION_MOVE:
                // Draws line between last point and this point
                path.lineTo(pointX, pointY);
                break;
            default:
                return false;
        }

        postInvalidate(); // Indicate view should be redrawn
        return true; // Indicate we've consumed the touch
    }

    public void changeColor(){
        paintChanges += 1;
        paintColor = currentColors[paintChanges % 4];
        drawPaint.setColor(paintColor);
    }

    public void switchColorSet(){
        if(currentColors.equals(colorOptions)){
            currentColors = colorOptions2;
        }
        else{
            currentColors = colorOptions;
        }
    }


    public void resetPath(){
        path = new Path();
    }

}
