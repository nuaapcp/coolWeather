package com.example.coolweather.network;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.net.ConnectException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

public abstract class BaseCallBack<T> implements Callback<BaseResponse<T>> {
    private static final String TAG = "BaseCallback";
    private Gson gson = new Gson();

    @Override
    public void onResponse(Call<BaseResponse<T>> call, Response<BaseResponse<T>> response) {
        BaseResponse<T> body = null;
        if (response.isSuccessful()) {
            body = response.body();
        } else {
            try {
                body = gson.fromJson(response.errorBody().string(), BaseResponse.class);
            } catch (Exception e) {
                Log.e(TAG, "BaseResponse解析异常: ", e);
            }
        }
        if (body == null) {
            onFailure(call, 0, "无返回结果");
            Log.w(TAG, "接口返回body数据为空");
            return;
        }
        if (body.isSuccessful()) {
            onResponse(call, body.getData());
        } else {
            if (body.getData() != null) {
                onFailure(call, body.getCode(), body.getData().toString());
            } else {
                onFailure(call, body.getCode(), body.getMessage());
            }
        }
    }

    @Override
    public void onFailure(Call<BaseResponse<T>> call, Throwable t) {
        if (t instanceof ConnectException) {
            onFailure(call, 0, "网络连接状态似乎不佳");
            return;
        }
        if (t instanceof HttpException) {
            HttpException httpException = (HttpException) t;
            Object body = httpException.response().body();
            if (body == null) {
                onFailure(call, 0, t.getMessage());
                return;
            }
            BaseResponse response = gson.fromJson(body.toString(), BaseResponse.class);
            if (response != null) {
                if (response.getData() != null) {
                    onFailure(call, response.getCode(), response.getData().toString());
                } else {
                    onFailure(call, response.getCode(), response.getMessage());
                }
            } else {
                onFailure(call, 0, t.getMessage());
            }
        } else if (t instanceof JsonSyntaxException) {
            Log.e(TAG, "JSON解析异常: ", t);
            onFailure(call, 0, "返回数据解析异常");
        } else {
            Log.e(TAG, "onFailure: ", t);
            //本地异常
            onFailure(call, 0, "后台系统异常,请稍后再试");
        }
    }

    /**
     * 失败回调
     *
     * @param call
     * @param code         错误码
     * @param errorMessage 错误信息
     */
    public abstract void onFailure(Call<BaseResponse<T>> call, int code, String errorMessage);

    /**
     * 网络请求成功的回调
     *
     * @param call
     * @param response 请求的结果
     */
    public abstract void onResponse(Call<BaseResponse<T>> call, T response);
}
