package com.test.coolweather.ui;


import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.push.HmsMessaging;
import com.test.coolweather.MyApplication;
import com.test.coolweather.R;
import com.test.coolweather.dataBase.AppDataBase;
import com.test.coolweather.dataBase.entity.LocationInfoCN;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private static final int PERMISSION_REQUEST = 1;
    String[] permissions = new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_SMS};
    List<String> mPermissionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPermission();
        init();
//        LocationInfoCN locationInfoCN = AppDataBase.getAppDatabase(MyApplication.getAppContext()).locationDAO().getLocation("南京","浦口");
//        Log.d(TAG, "onCreate: 南京浦口的经纬度是：lng:"+locationInfoCN.getLng()+" lat:"+locationInfoCN.getLat());
    }

    private void init() {
        try {
            InputStream is = getResources().getAssets().open("locationInfoCN.xls");
            Workbook workbook = Workbook.getWorkbook(is);
            Sheet sheet = workbook.getSheet(0);
            for (int i = 2; i < sheet.getRows(); i++) {
                String adcode = sheet.getCell(0, i).getContents();
                String formattedAddress = sheet.getCell(1, i).getContents();
                String cityName = sheet.getCell(2, i).getContents();
                String province = sheet.getCell(3, i).getContents();
                String city = sheet.getCell(4, i).getContents();
                String district = sheet.getCell(5, i).getContents();
                String lng = sheet.getCell(6, i).getContents();
                String lat = sheet.getCell(7, i).getContents();
                LocationInfoCN locationInfoCN = new LocationInfoCN(adcode, formattedAddress, cityName, province, city, district, lng, lat);
                AppDataBase.getAppDatabase(MyApplication.getAppContext()).locationDAO().insert(locationInfoCN);
            }
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
    }

    public void getToken() {
        new Thread() {
            @Override
            public void run() {
                try {
                    String getToken = HmsInstanceId.getInstance(MainActivity.this).getToken("102436051", HmsMessaging.DEFAULT_TOKEN_SCOPE);
                    if (!TextUtils.isEmpty(getToken)) {
                        Log.d(TAG, "run: getToken: " + getToken);
                        //TODO: Send token to your app server.
                    }
                } catch (Exception e) {
                    Log.e(TAG, "getToken failed.", e);
                }
            }
        }.start();
    }

    private void getContact() {
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI, null, null,
                null, null);
        int contactIdIndex = 0;
        int nameIndex = 0;

        if (cursor != null && cursor.getCount() > 0) {
            contactIdIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        }
        while (cursor != null && cursor.moveToNext()) {
            String contactId = cursor.getString(contactIdIndex);
            String name = cursor.getString(nameIndex);
            /*
             * 查找该联系人的phone信息
             */
            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId,
                    null, null);
            int phoneIndex = 0;
            if (phones != null && phones.getCount() > 0) {
                phoneIndex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            }
            /**
             * 一个联系人可能有多个电话号码
             */
            List<String> phoneNumbers = new ArrayList<>();
            String phoneNumber = "";
            while (phones != null && phones.moveToNext()) {
                phoneNumbers.add(phones.getString(phoneIndex));
                phoneNumber = phones.getString(phoneIndex);
                Log.d(TAG, "getContact: " + phoneNumber);
            }
            if (phones != null) {
                phones.close();
            }
            /*
             * 查找该联系人的email信息
             */
            Cursor emails = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=" + contactId,
                    null, null);
            int emailIndex = 0;
            if (emails.getCount() > 0) {
                emailIndex = emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);
            }
            List<String> emailList = new ArrayList<>();
            String email = "";
            while (emails.moveToNext()) {
                emailList.add(emails.getString(emailIndex));
                email = emails.getString(emailIndex);
            }
            emails.close();
            String info = name + ": " + phoneNumbers.toString() + " " + emailList.toString();
            Log.d(TAG, "getContact: " + info);
        }
        if (cursor != null) {
            cursor.close();
        }
    }

    private void initPermission() {
        mPermissionList.clear();
        /**
         * 判断哪些权限未授予
         */
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }
        /**
         * 判断是否为空
         */
        if (mPermissionList.isEmpty()) {//未授予的权限为空，表示都授予了
        } else {//请求权限方法
            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(MainActivity.this, permissions, PERMISSION_REQUEST);
        }
    }

    /**
     * 响应授权
     * 这里不管用户是否拒绝,不再重复申请权限
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST:
                break;
            default:
                break;
        }
    }

}
