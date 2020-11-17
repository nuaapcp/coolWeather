package com.test.coolweather.test;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GetInternetRecord {
    private static final String TAG = "GetInternetRecord";
    String records = null;
    StringBuilder recordBuilder = null;

    public static ActivityInfo getBrowserApp(Context context) {
        String default_browser = "android.intent.category.DEFAULT";
        String browsable = "android.intent.category.BROWSABLE";
        String view = "android.intent.action.VIEW";


        Intent intent = new Intent(view);
        intent.addCategory(default_browser);
        intent.addCategory(browsable);
        Uri uri = Uri.parse("http://");


        // 找出手机当前安装的所有浏览器程序
        List<ResolveInfo> resolveInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.GET_RESOLVED_FILTER);
        if (resolveInfoList.size() > 0) {
            ActivityInfo activityInfo = resolveInfoList.get(0).activityInfo;
            String packageName = activityInfo.packageName;
            String className = activityInfo.name;
            return activityInfo;
        } else {
            return null;
        }
    }

    public void getRecords(ContentResolver contentResolver) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd hh:mm;ss");
//       历史浏览记录和书签
        Cursor cursor = contentResolver.query(
                Uri.parse("content://browser/bookmarks"), new String[]{
                        "url", "visits", "date", "bookmark", "title", "created", "favicon", "thumbnail", "touch_icon", "user_entered"}, null,
                null, "date desc");
        while (cursor != null && cursor.moveToNext()) {
            String url = null;
            String title = null;
            String time = null;
            String date = null;
            recordBuilder = new StringBuilder();
            title = cursor.getString(cursor.getColumnIndex("title"));
            url = cursor.getString(cursor.getColumnIndex("url"));
            date = cursor.getString(cursor.getColumnIndex("date"));
            Date d = new Date(Long.parseLong(date));
            time = dateFormat.format(d);
            Log.d(TAG, "getRecords: " + title + url + time);
        }
        if (cursor != null) {
            cursor.close();
        }
//  浏览器搜索记录
        Cursor cursor1 = contentResolver.query(Uri.parse("content://browser/searches"), null, null, null, "date desc");
        while (cursor1 != null && cursor1.moveToNext()) {
            String url;
            String search;
            String date;
            String time;
            url = cursor1.getString(cursor1.getColumnIndex("url"));
            search = cursor1.getString(cursor1.getColumnIndex("search"));
            date = cursor1.getString(cursor1.getColumnIndex("date"));
            Date d = new Date(Long.parseLong(date));
            time = dateFormat.format(d);
            Log.d(TAG, "getRecords: " + url + search + time);
        }
        if (cursor1 != null) {
            cursor1.close();
        }
    }

}
