package com.test.coolweather.network;

import com.test.coolweather.network.model.CaiYunWeatherInfo;
import com.test.coolweather.network.model.CityResponse;
import com.test.coolweather.network.model.CountyResponse;
import com.test.coolweather.network.model.ProvinceResponse;
import com.test.coolweather.network.model.WeatherInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("china")
    Call<List<ProvinceResponse>> getProvince();

    @GET("china/{provinceId}")
    Call<List<CityResponse>> getCity(@Path("provinceId") String provinceId);

    @GET("china/{provinceId}/{cityId}")
    Call<List<CountyResponse>> getCounty(@Path("provinceId") String provinceId, @Path("cityId") String cityId);

    @GET("{weatherType}")
    Call<WeatherInfo> getWeather(@Path("weatherType") String weatherType, @Query("location") String location, @Query("key") String key);

    @GET("https://api.caiyunapp.com/v2.5/7lihHrYJOfapH8cJ/{lng},{lat}/realtime.json")
    Call<CaiYunWeatherInfo> getCaiYunWeather(@Path("lng") String lng, @Path("lat") String lat);
}
