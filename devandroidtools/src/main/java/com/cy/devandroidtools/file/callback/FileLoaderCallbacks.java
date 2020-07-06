package com.cy.devandroidtools.file.callback;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;


import com.cy.devandroidtools.file.bean.Directory;
import com.cy.devandroidtools.file.bean.ImageFile;
import com.cy.devandroidtools.file.loader.CursorImageLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_ID;
import static android.provider.MediaStore.Images.ImageColumns.ORIENTATION;
import static android.provider.MediaStore.MediaColumns.DATA;
import static android.provider.MediaStore.MediaColumns.DATE_ADDED;
import static android.provider.MediaStore.MediaColumns.SIZE;
import static android.provider.MediaStore.MediaColumns.TITLE;


public class FileLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final int TYPE_IMAGE = 0;
    public static final int TYPE_FILE = 3;

    private WeakReference<Context> context;
    private FilterResultCallback resultCallback;

    private int mType = TYPE_IMAGE;
    private String[] mSuffixArgs;
    private CursorLoader mLoader;

    public FileLoaderCallbacks(Context context, FilterResultCallback resultCallback, int type) {
        this(context, resultCallback, type, null);
    }

    public FileLoaderCallbacks(Context context, FilterResultCallback resultCallback, int type, String[] suffixArgs) {
        this.context = new WeakReference<>(context);
        this.resultCallback = resultCallback;
        this.mType = type;
        this.mSuffixArgs = suffixArgs;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (mType) {
            case TYPE_IMAGE:
                mLoader = new CursorImageLoader(context.get());
                break;
        }

        return mLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null) return;
        switch (mType) {
            case TYPE_IMAGE:
                onImageResult(data);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @SuppressWarnings("unchecked")
    private void onImageResult(Cursor data) {
        List<Directory<ImageFile>> directories = new ArrayList<>();

        if (data.getPosition() != -1) {
            data.moveToPosition(-1);
        }

        while (data.moveToNext()) {
            //Create a File instance
            ImageFile img = new ImageFile();
            long _id = data.getLong(data.getColumnIndexOrThrow(_ID));
            img.setId(_id);
            img.setName(data.getString(data.getColumnIndexOrThrow(TITLE)));
            img.setPath(data.getString(data.getColumnIndexOrThrow(DATA)));
            img.setSize(data.getLong(data.getColumnIndexOrThrow(SIZE)));
            img.setBucketId(data.getString(data.getColumnIndexOrThrow(BUCKET_ID)));
            img.setBucketName(data.getString(data.getColumnIndexOrThrow(BUCKET_DISPLAY_NAME)));
            img.setDate(data.getLong(data.getColumnIndexOrThrow(DATE_ADDED)));
            img.setOrientation(data.getInt(data.getColumnIndexOrThrow(ORIENTATION)));

            img.setUri(ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, _id));

            //Create a Directory
            Directory<ImageFile> directory = new Directory<>();
            directory.setId(img.getBucketId());
            directory.setName(img.getBucketName());
            directory.setPath(extractDirectory(img.getPath()));

            if (!directories.contains(directory)) {
                if(img.getSize()>0){
                    directory.addFile(img);
                }
                directories.add(directory);
            } else {
                if(img.getSize()>0){
                    directories.get(directories.indexOf(directory)).addFile(img);
                }
            }
        }

        if (resultCallback != null) {
            resultCallback.onResult(directories);
        }
    }


    private static final String[] thumbColumns = new String[]{
            MediaStore.Video.Thumbnails.DATA,
            MediaStore.Video.Thumbnails.VIDEO_ID
    };

    private String saveBitmap(Bitmap bitmap, String pathName) {
        if (bitmap == null) {
            return "";
        }

        String path = "";
//        String pathName = context.get().getExternalCacheDir().getAbsolutePath() + "/" + String.valueOf(System.currentTimeMillis()) + ".png";
        File f = new File(pathName);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            path = pathName;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return path;
    }

    private String extractDirectory(String path) {
        return path.substring(0, path.lastIndexOf("/"));
    }

    private String extractName(String path) {
        return path.substring(path.lastIndexOf("/") + 1);
    }

    private boolean contains(String[] types, String path) {
        for (String string : types) {
            if (path.endsWith(string)) return true;
        }
        return false;
    }
}
