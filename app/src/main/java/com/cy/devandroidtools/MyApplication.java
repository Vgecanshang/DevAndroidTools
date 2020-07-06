package com.cy.devandroidtools;

import android.app.Application;

import com.cy.devandroidtools.toast.CustomToast;

/**
 * class
 *
 * @author cy
 * @date 2020/5/28.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CustomToast.init(this);
    }
}
