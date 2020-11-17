package com.test.coolweather.service;

import android.text.TextUtils;
import android.util.Log;

import com.huawei.hms.push.HmsMessageService;

public class DemoHmsMessageService extends HmsMessageService {
    private static final String TAG = "DemoHmsMessageService";

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        // send the token to your app server.
        if (!TextUtils.isEmpty(token)) {
            sendRegTokenToServer(token);
        }
    }

    private void sendRegTokenToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }
}
