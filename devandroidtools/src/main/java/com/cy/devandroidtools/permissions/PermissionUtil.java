package com.cy.devandroidtools.permissions;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;


import androidx.fragment.app.FragmentActivity;


import com.cy.devandroidtools.permissions.base.Permission;
import com.cy.devandroidtools.permissions.base.RxPermissions;
import com.cy.devandroidtools.log.CLog;
import com.cy.devandroidtools.toast.ToastUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 动态权限配置类
 * @author cy
 * @date 2019/6/25
 */
public class PermissionUtil {

    private static HashMap<String,String> permissionMap = new HashMap<>();
    static{
        permissionMap.put(Manifest.permission.CAMERA , "相机");
        permissionMap.put(Manifest.permission.ACCESS_FINE_LOCATION , "定位");
        permissionMap.put(Manifest.permission.WRITE_EXTERNAL_STORAGE , "读取");
        permissionMap.put(Manifest.permission.READ_EXTERNAL_STORAGE , "写入");
        permissionMap.put(Manifest.permission.READ_CONTACTS , "联系人");
        permissionMap.put(Manifest.permission.READ_PHONE_STATE , "手机状态");
    }

    /**
     * 申请成功
     */
    public static final int STATUS_PERMISSION_RESULT_SUCCESS = 0;
    /**
     * 申请失败(用户拒绝了该权限，没有选中“不再询问”)
     */
    public static final int STATUS_PERMISSION_RESULT_FAIL = 1;
    /**
     * 申请失败（用户拒绝了该权限，且选中了“不再询问”）
     */
    public static final int STATUS_PERMISSION_RESULT_FAIL_NEVER = 2;

    private static RxPermissions rxPermissions;
    private static boolean isShowPermissionAlert ;
    /**
     * 申请权限的结果
     */
    private static int resultStatus = STATUS_PERMISSION_RESULT_SUCCESS;

    private static WeakReference<Activity> weakReference = null;

    public static void requestPermissionsWithTip(String tips, FragmentActivity activity, OnRequestPermissionListener listener, String... permissions){
        boolean isGrant = true;
        for (String permission: permissions) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                isGrant = activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
            }
        }
        if(!isGrant){
            AlertDialog alertDialog = new AlertDialog.Builder(activity)
                    .setTitle("温馨提示：")
                    .setMessage(tips)
                    .setNegativeButton("暂不", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .setPositiveButton("好的", (dialog, which) -> {
                        requestPermissions(activity , listener , permissions);
                    })
                    .create();
            alertDialog.show();
            //修改 确定取消 按钮的字体颜色
//        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(activity , R.color.color_1));
//        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(activity , R.color.gray_1));
        }
    }


    /**
     * 动态权限 申请
     * 两种情况：
     * 1、单个权限申请
     * 2、多个权限申请
     * 如果有一个权限没通过，则结果根据这个权限没通过的类型做toast提示或弹出框的提示
     *
     * @param refuseTips 当用户拒绝权限的toast提示语
     * @param settingTips 当用户拒绝权限且选中了“不在询问” 的AlertDialog提示语
     * @param permissions 需要申请的权限
     */
    @SuppressLint("CheckResult")
    public static  void requestPermissions(FragmentActivity activity , String refuseTips , String settingTips , OnRequestPermissionListener listener , String... permissions ) {
        if (permissions.length <= 0) {
            return;
        }
        isShowPermissionAlert = false;
        weakReference = new WeakReference<>(activity);
        resultStatus = STATUS_PERMISSION_RESULT_SUCCESS;
        CLog.println("rxPermissions JYActivity requestPermissions=" + permissions.toString());
        rxPermissions = new RxPermissions(activity);
        StringBuffer failPermissionBuffer = new StringBuffer();
        rxPermissions
                .requestEach(permissions)
                .subscribe(permission -> {
                            CLog.println("rxPermissions requestEach permission=" + permission.toString());
                            if (permission.granted) {
                                // 用户同意该权限

                            } else if (permission.shouldShowRequestPermissionRationale) {
                                // 用户拒绝了该权限 ，需要提示申请该权限的原因
                                if(resultStatus == STATUS_PERMISSION_RESULT_SUCCESS){
                                    resultStatus = STATUS_PERMISSION_RESULT_FAIL;
                                }
                                failPermissionBuffer.append(getPermissionChinaName(permission.name)+"、");
                            } else {
                                // 用户拒绝了该权限，且选中了“不再询问”
                                if(resultStatus == STATUS_PERMISSION_RESULT_SUCCESS){
                                    resultStatus = STATUS_PERMISSION_RESULT_FAIL_NEVER;
                                }
                                failPermissionBuffer.append(getPermissionChinaName(permission.name)+"、");
                            }
                        }, error -> {
                            CLog.println("subscribe onError--------------------->");
                            resultStatus = STATUS_PERMISSION_RESULT_FAIL;
                            handleResult(resultStatus , refuseTips , settingTips , failPermissionBuffer.toString() , listener);
                        },
                        () -> {
                            CLog.println("rxPermissions requestEach conCompleted resultStatus=" + resultStatus);
                            //在onCompleted中回调，防止申请多个权限时，多次回调，
                            // 保存多个权限有一个不通过，则认为申请权限失败，且只回调一次
                            handleResult(resultStatus , refuseTips , settingTips , failPermissionBuffer.toString(), listener);
                        });
    }

    /**
     * 动态权限 申请(精确模式)
     * 两种情况：
     * 1、单个权限申请
     * 2、多个权限申请(返回每个权限的授予结果)
     *
     * @param permissions 需要申请的权限
     */
    @SuppressLint("CheckResult")
    public static  void requestPermissionsAccurate(FragmentActivity activity , OnRequestMultiPermissionListener listener , String... permissions ) {
        if (permissions.length <= 0) {
            return;
        }
        isShowPermissionAlert = false;
        weakReference = new WeakReference<>(activity);
        resultStatus = STATUS_PERMISSION_RESULT_SUCCESS;
        CLog.println("rxPermissions JYActivity requestPermissionsAccurate=" + permissions.toString());
        rxPermissions = new RxPermissions(activity);
        StringBuffer failPermissionBuffer = new StringBuffer();

        List<Permission> resultList = new ArrayList<>();
        rxPermissions
                .requestEach(permissions)
                .subscribe(permission -> {
                            CLog.println("rxPermissions requestEach permission=" + permission.toString());
                            resultList.add(permission);
                            if (permission.granted) {
                                // 用户同意该权限

                            } else if (permission.shouldShowRequestPermissionRationale) {
                                // 用户拒绝了该权限 ，需要提示申请该权限的原因
                                if(resultStatus == STATUS_PERMISSION_RESULT_SUCCESS){
                                    resultStatus = STATUS_PERMISSION_RESULT_FAIL;
                                }
                                failPermissionBuffer.append(getPermissionChinaName(permission.name)+"、");
                            } else {
                                // 用户拒绝了该权限，且选中了“不再询问”
                                if(resultStatus == STATUS_PERMISSION_RESULT_SUCCESS){
                                    resultStatus = STATUS_PERMISSION_RESULT_FAIL_NEVER;
                                }
                                failPermissionBuffer.append(getPermissionChinaName(permission.name)+"、");
                            }
                        }, error -> {
                            CLog.println("subscribe onError--------------------->");
                        },
                        () -> {
                            CLog.println("rxPermissions requestEach conCompleted resultStatus=" + resultStatus);
                            listener.requestPermissionResult(resultList);
                        });
    }


    /**
     * 动态权限
     * @param refuseTips 拒绝权限的提示语
     * @param permissions 需要申请的权限
     */
    public static void requestPermissions(FragmentActivity activity , String refuseTips  , OnRequestPermissionListener listener , String... permissions ) {
        requestPermissions(activity , refuseTips , "" , listener , permissions);
    }

    /**
     * 动态权限
     * @param permissions 需要申请的权限
     */
    public static void requestPermissions(FragmentActivity activity  , OnRequestPermissionListener listener , String... permissions ) {
        requestPermissions(activity , "" , "" , listener , permissions);
    }

    /**
     * 权限处理结果
     * @param status
     * @param refuseTips
     * @param settingTips
     */
    private static void  handleResult(int status , String refuseTips , String settingTips ,String failPermissions ,  OnRequestPermissionListener listener){
        Activity activity = weakReference.get();
        switch (status){
            case STATUS_PERMISSION_RESULT_SUCCESS:
                if(listener != null){
                    listener.requestPermissionResult(true);
                }
                break;
            case STATUS_PERMISSION_RESULT_FAIL:
                if (TextUtils.isEmpty(refuseTips)) {
                    refuseTips = "获取权限失败！";
                }

                if(activity != null){
                    ToastUtil.showToastAtTop(activity, refuseTips);
                }
                if(listener != null){
                    listener.requestPermissionResult(false);
                }
                break;
            case STATUS_PERMISSION_RESULT_FAIL_NEVER:
                if (!isShowPermissionAlert) {
                    String tips = settingTips;
                    if (TextUtils.isEmpty(settingTips)) {
                        if(failPermissions.endsWith("、")){
                            failPermissions = failPermissions.substring(0 , failPermissions.length() - 1);
                        }
                        tips = "\n由于您此前禁止了"+failPermissions+"权限，导致无法使用部分功能，若要开启该权限请前往【权限】中手动开启相应权限。\n";
                    }
                    if(activity != null){
                        showTipsAlert(activity , tips , listener);
                    }
                }
                break;
            default:
                break;
        }
    }


    private static void showTipsAlert(Activity activity , String message , OnRequestPermissionListener listener){
        if(TextUtils.isEmpty(message)){
            return;
        }
        AlertDialog alertDialog = new AlertDialog.Builder(activity)
                .setTitle("温馨提示：")
                .setMessage(message)
                .setNegativeButton("暂不", (dialog, which) -> {
                    isShowPermissionAlert = false;
                    dialog.dismiss();
                    if(listener != null && activity != null){
                        listener.requestPermissionResult(false);
                    }
                })
                .setPositiveButton("好的", (dialog, which) -> {
                    isShowPermissionAlert = false;
                    if(listener != null && activity != null){
                        listener.requestPermissionResult(false);
                    }
                    goAppPermissionSetting();
                })
                .setOnCancelListener(dialog -> isShowPermissionAlert = false)
                .create();
        alertDialog.show();
               //修改 确定取消 按钮的字体颜色
//        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(activity , R.color.color_1));
//        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(activity , R.color.gray_1));
        isShowPermissionAlert = true;
    }


    /** 权限结果接口 */
    public interface OnRequestPermissionListener{
        void requestPermissionResult(boolean isGranted);
    }

    /** 多个权限申请结果 */
    public interface OnRequestMultiPermissionListener{
        void requestPermissionResult(List<Permission> permissions);
    }

    public static void clear(){
        weakReference.clear();
        rxPermissions = null;
        isShowPermissionAlert = false;
    }


    public static final String HUAWEI = "HUAWEI";
    public static final String MEIZU = "Meizu";
    public static final String XIAOMI = "Xiaomi";
    public static final String SONY = "Sony";
    public static final String OPPO = "OPPO";
    public static final String LG = "LG";
    public static final String VIVO = "vivo";
    public static final String SAMSUNG = "samsung";
    public static final String LETV = "Letv";
    public static final String ZTE = "ZTE";
    public static final String YULONG = "YuLong";
    public static final String LENOVO = "LENOVO";

    /**
     *
     * @return
     */
    private static boolean goAppPermissionSetting() {

        Activity activity = weakReference.get();
        if(activity == null){
            return false;
        }

        Intent mIntent = new Intent();
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (Build.VERSION.SDK_INT >= 9) {
            mIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            mIntent.setData(Uri.fromParts("package", activity.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            mIntent.setAction(Intent.ACTION_VIEW);
            mIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
            mIntent.putExtra("com.android.settings.ApplicationPkgName", activity.getPackageName());
        }
        activity.startActivity(mIntent);

        return true;

    }


    /**
     * 获取对应权限的中文名字
     * @param permission
     * @return
     */
    private static String getPermissionChinaName(String permission){
        if(TextUtils.isEmpty(permission) || permissionMap == null || permissionMap.size() <= 0){
            return "";
        }
        String name = permissionMap.get(permission);
        if(TextUtils.isEmpty(name)){
            return "";
        }else{
            return name;
        }
    }

}
