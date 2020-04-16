package com.arcsoft.arcfacedemo.activity;

import android.app.Application;
import android.content.Context;

import com.yjx.sharelibrary.Share;

import java.io.File;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        String shareFilePath = getShareDir(this);
        Share.init("CACHE", 50 * 1024, shareFilePath.toString());
    }


    public static String getShareDir(Context mContext) {
        String dir = mContext.getApplicationContext().getFilesDir().getAbsolutePath() + File.separator + "sharefile";
        return checkDir(dir);
    }

    private static String checkDir(String dir) {
        File directory = new File(dir);
        if (!directory.exists() || !directory.isDirectory()) {
            directory.mkdirs();
        }
        return dir;
    }

}
