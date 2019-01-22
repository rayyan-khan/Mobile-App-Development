package com.example.rkhan2019.lab11;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
//import android.widget.ImageView;


public class MyCustomView extends ImageView {

    private cvinterface mCallback;

    public MyCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mCallback = (cvinterface)context;
        this.setDrawingCacheEnabled(true);
    }

    public Bitmap getCanvasBitmap() {
        return this.getDrawingCache();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_UP:
                float pointX = event.getX();
                float pointY = event.getY();
                mCallback.doIt(pointX, pointY);
                break;
        }
        return true;
    }

    public interface cvinterface {
        void doIt(float x, float y);
    }
}