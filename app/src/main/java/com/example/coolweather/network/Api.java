package com.example.coolweather.network;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import java.util.Date;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {
    private static ApiService apiService;

    public static ApiService getApiService(String httpAddress) {
        if (apiService == null) {
            Retrofit.Builder builder = new Retrofit.Builder();
            Retrofit retrofit = builder.baseUrl(httpAddress)
                    .client(new OkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create(getGson()))
                    .build();
            apiService = retrofit.create(ApiService.class);
        }
        return apiService;
    }

    private static Gson getGson() {
        return new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
    }
}
