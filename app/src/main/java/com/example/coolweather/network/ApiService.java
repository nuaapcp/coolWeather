package com.example.coolweather.network;

import com.example.coolweather.network.model.ProvinceResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("province")
    Call<BaseResponse<List<ProvinceResponse>>> getProvince();
}
