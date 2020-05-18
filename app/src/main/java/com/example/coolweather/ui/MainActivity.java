package com.example.coolweather.ui;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.coolweather.R;
import com.example.coolweather.network.Api;
import com.example.coolweather.network.BaseCallBack;
import com.example.coolweather.network.BaseResponse;
import com.example.coolweather.network.model.ProvinceResponse;

import java.util.List;

import retrofit2.Call;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView textView = findViewById(R.id.text1);
        textView.setText("获取省份");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Api.getApiService("https://mock.yonyoucloud.com/mock/6716/").getProvince()
                        .enqueue(new BaseCallBack<List<ProvinceResponse>>() {
                            @Override
                            public void onFailure(Call<BaseResponse<List<ProvinceResponse>>> call, int code, String errorMessage) {
                                Log.d(TAG, "onFailure: get provinces failed!");
                            }

                            @Override
                            public void onResponse(Call<BaseResponse<List<ProvinceResponse>>> call, List<ProvinceResponse> response) {
                                textView.setText(response.get(0).getName());
                            }
                        });
            }
        });
    }
}
