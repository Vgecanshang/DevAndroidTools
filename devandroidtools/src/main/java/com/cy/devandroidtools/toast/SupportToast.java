package com.cy.devandroidtools.toast;

import android.app.Application;

/**
 * Created by cy on 2019/6/11.
 */
final class SupportToast extends BaseToast {

    // 吐司弹窗显示辅助类
    private final ToastHelper mToastHelper;

    SupportToast(Application application) {
        super(application);
        mToastHelper = new ToastHelper(this, application);
    }

    @Override
    public void show() {
        // 显示吐司
        mToastHelper.show();
    }

    @Override
    public void cancel() {
        // 取消显示
        mToastHelper.cancel();
    }
}