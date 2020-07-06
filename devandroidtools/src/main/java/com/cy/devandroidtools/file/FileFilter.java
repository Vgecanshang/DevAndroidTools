package com.cy.devandroidtools.file;


import androidx.fragment.app.FragmentActivity;
import androidx.loader.app.LoaderManager;

import com.cy.devandroidtools.file.bean.ImageFile;
import com.cy.devandroidtools.file.callback.FileLoaderCallbacks;
import com.cy.devandroidtools.file.callback.FilterResultCallback;


/**
 * 手机文件加载器
 * @author cy
 */
public class FileFilter {

    /**
     * 加载图片文件
     * @param activity
     * @param callback
     */
    public static void getImages(FragmentActivity activity, FilterResultCallback<ImageFile> callback) {
        LoaderManager.getInstance(activity).initLoader(0, null, new FileLoaderCallbacks(activity, callback, FileLoaderCallbacks.TYPE_IMAGE));
    }

}
