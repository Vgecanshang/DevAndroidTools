package com.cy.devandroidtools.toast;

import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cy.devandroidtools.R;


public class ToastUtil {
    /**
     * Don't let anyone instantiate this class.
     */
    private ToastUtil() {
        throw new Error("Do not need instantiate!");
    }

    /***
     * 带图片的提示消息
     *
     * @param context
     * @param imageID 图片id，不需要设置为0
     * @param content 提示内容
     */
    public static void showToast(Context context, int imageID, String content) {
        if(CustomToast.isNotificationEnabled(context)){
            if (imageID == 0) {
                showToast(context, content);
                return;
            }

            Toast localToast = Toast.makeText(context.getApplicationContext(), content, Toast.LENGTH_SHORT);
            LinearLayout localLinearLayout = (LinearLayout) localToast.getView();
            ImageView localImageView = new ImageView(context.getApplicationContext());
            localImageView.setImageResource(imageID);
            localLinearLayout.addView(localImageView, 0);
            localToast.show();

        }else{
            CustomToast.initStyle(new ToastBlackStyle());
            CustomToast.show(content);
        }
    }

    /***
     * toast提示
     *
     * @param context
     * @param content 提示内容
     */
    public static void showToast(Context context, String content) {
        if(CustomToast.isNotificationEnabled(context)){
            Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
        }else{
            CustomToast.initStyle(new ToastBlackStyle());
            CustomToast.show(content);
        }

    }

    /***
     * toast提示
     *
     * @param context
     * @param id      提示文字id，也就是string.xml里面的文字ID
     */
    public static void showToast(Context context, int id) {
        String content = context.getResources().getString(id);
        if(TextUtils.isEmpty(content)){
            return;
        }
        if(CustomToast.isNotificationEnabled(context)){
            Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
        }else{
            CustomToast.initStyle(new ToastBlackStyle());
            CustomToast.show(content);
        }
    }

    /**
     * 在屏幕顶部显示Toast
     *
     * @param context
     * @param id
     */
    public static void showToastAtTop(Context context, int id) {

        String content = context.getResources().getString(id);
        if(TextUtils.isEmpty(content)){
            return;
        }
        if(CustomToast.isNotificationEnabled(context)){
            boolean bool = false;
            if (Looper.myLooper() == null) {
                bool = true;
                Looper.prepare();
            }

            View toastRoot = LayoutInflater.from(context).inflate(R.layout.toast_layout, null);
            Toast toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 150);
            toast.setView(toastRoot);
            TextView tv = (TextView) toastRoot.findViewById(R.id.TextViewInfo);
            tv.setText(content);
            toast.show();
            if (bool) {
                Looper.loop();// 进入loop中的循环，查看消息队列
            }
        }else{
            CustomToast.initStyle(new ToastTopStyle());
            CustomToast.show(content);
        }

    }

    /**
     * 在屏幕顶部显示Toast
     *
     * @param context
     * @param content
     */
    public static void showToastAtTop(Context context, CharSequence content) {
        if (context == null) {
            return;
        }

        if(CustomToast.isNotificationEnabled(context)){
            boolean bool = false;
            if (Looper.myLooper() == null) {
                bool = true;
                Looper.prepare();
            }
            View toastRoot = LayoutInflater.from(context).inflate(R.layout.toast_layout, null);
            Toast toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 150);
            toast.setView(toastRoot);
            TextView tv = (TextView) toastRoot.findViewById(R.id.TextViewInfo);
            tv.setText(content);
            toast.show();
            if (bool) {
                Looper.loop();// 进入loop中的循环，查看消息队列
            }
        }else{
            CustomToast.initStyle(new ToastTopStyle());
            CustomToast.show(content);
        }

    }
}
