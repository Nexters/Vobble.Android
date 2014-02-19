package com.nexters.vobble.listener;


import android.graphics.PorterDuff;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class ImageViewTouchListener implements View.OnTouchListener {

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                ((ImageView) view).setColorFilter(0xaa111111, PorterDuff.Mode.SRC_ATOP);
                break;
            case MotionEvent.ACTION_UP:
                ((ImageView) view).setColorFilter(0x00000000, PorterDuff.Mode.SRC_OVER);
                break;
        }
        return false;
    }
}
