package com.cy.devandroidtools.toast;

import android.view.Gravity;

/**
 * Created by cy on 2019/6/11.
 */
public class ToastTopStyle implements IToastStyle {

    @Override
    public int getGravity() {
        return Gravity.TOP;
    }

    @Override
    public int getXOffset() {
        return 0;
    }

    @Override
    public int getYOffset() {
        return 60;
    }

    @Override
    public int getZ() {
        return 30;
    }

    @Override
    public int getCornerRadius() {
        return 0;
    }

    @Override
    public int getBackgroundColor() {
        return 0X88000000;
    }

    @Override
    public int getTextColor() {
        return 0XEEFFFFFF;
    }

    @Override
    public float getTextSize() {
        return 14;
    }

    @Override
    public int getMaxLines() {
        return 3;
    }

    @Override
    public int getPaddingLeft() {
        return 20;
    }

    @Override
    public int getPaddingTop() {
        return 12;
    }

    @Override
    public int getPaddingRight() {
        return getPaddingLeft();
    }

    @Override
    public int getPaddingBottom() {
        return getPaddingTop();
    }
}
