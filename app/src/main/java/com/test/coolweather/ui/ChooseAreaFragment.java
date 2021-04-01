package com.test.coolweather.ui;


import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.test.coolweather.MyApplication;
import com.test.coolweather.R;
import com.test.coolweather.dataBase.AppDataBase;
import com.test.coolweather.dataBase.entity.CityEntity;
import com.test.coolweather.dataBase.entity.CountyEntity;
import com.test.coolweather.dataBase.entity.ProvinceEntity;
import com.test.coolweather.network.Api;
import com.test.coolweather.network.model.CaiYunWeatherInfo;
import com.test.coolweather.network.model.CityResponse;
import com.test.coolweather.network.model.CountyResponse;
import com.test.coolweather.network.model.ProvinceResponse;
import com.test.coolweather.ui.adapter.AreaListAdapter;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChooseAreaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChooseAreaFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "ChooseAreaFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int LEVEL_PROVINCE = 0;
    private static final int LEVEL_CITY = 1;
    private static final int LEVEL_COUNTY = 2;
    private int currentLevel;
    private CommonTitleBar commonTitleBar;
    private RecyclerView recyclerView;
    private AreaListAdapter areaListAdapter;
    private List<String> dataList = new ArrayList<>();


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public ChooseAreaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChooseAreaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChooseAreaFragment newInstance(String param1, String param2) {
        ChooseAreaFragment fragment = new ChooseAreaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area, container, false);
        commonTitleBar = view.findViewById(R.id.commontitle_bar);
        recyclerView = view.findViewById(R.id.areas_recycler_view);
        areaListAdapter = new AreaListAdapter();
        areaListAdapter.setAreaList(dataList);
        recyclerView.setAdapter(areaListAdapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button button = view.findViewById(R.id.get_package);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GetPackageInfoActivity.class);
                startActivity(intent);
            }
        });
        areaListAdapter.notifyDataSetChanged();
        commonTitleBar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                if (action == CommonTitleBar.ACTION_LEFT_BUTTON) {
                    if (currentLevel == LEVEL_COUNTY) {
                        String belongCity = commonTitleBar.getCenterTextView().getText().toString();
                        int belongProvinceId = AppDataBase.getAppDatabase(MyApplication.getAppContext()).cityDAO().getCityByName(belongCity).getProvinceId();
                        String belongProvinceName = AppDataBase.getAppDatabase(MyApplication.getAppContext()).provinceDAO().getProvinceName(belongProvinceId);
                        queryAreaList(LEVEL_CITY, belongProvinceName);
                    } else if (currentLevel == LEVEL_CITY) {
                        queryAreaList(LEVEL_PROVINCE, "中国");
                    }
                }
            }
        });
        getActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (currentLevel == LEVEL_COUNTY) {
                    String belongCity = commonTitleBar.getCenterTextView().getText().toString();
                    int belongProvinceId = AppDataBase.getAppDatabase(MyApplication.getAppContext()).cityDAO().getCityByName(belongCity).getProvinceId();
                    String belongProvinceName = AppDataBase.getAppDatabase(MyApplication.getAppContext()).provinceDAO().getProvinceName(belongProvinceId);
                    queryAreaList(LEVEL_CITY, belongProvinceName);
                } else if (currentLevel == LEVEL_CITY) {
                    queryAreaList(LEVEL_PROVINCE, "中国");
                } else if (currentLevel == LEVEL_PROVINCE) {
                    getActivity().finish();
                }
            }
        });
        queryAreaList(LEVEL_PROVINCE, "中国");
        areaListAdapter.setOnItemClickListener(new AreaListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String area) {
                if (currentLevel == LEVEL_PROVINCE) {
                    queryAreaList(LEVEL_CITY, area);
                } else if (currentLevel == LEVEL_CITY) {
                    queryAreaList(LEVEL_COUNTY, area);
                } else if (currentLevel == LEVEL_COUNTY) {
                    Api.getApiService(null).getCaiYunWeather("118.628", "32.059").enqueue(new Callback<CaiYunWeatherInfo>() {
                        @Override
                        public void onResponse(Call<CaiYunWeatherInfo> call, Response<CaiYunWeatherInfo> response) {
                            if (response.body() != null) {
                                CaiYunWeatherInfo caiYunWeatherInfo = response.body();
                                Log.d(TAG, "onResponse: 彩云天气信息已接收！");
                            }
                            Log.d(TAG, "onResponse: !!!!!!!");
                        }

                        @Override
                        public void onFailure(Call<CaiYunWeatherInfo> call, Throwable t) {
                            Log.d(TAG, "onFailure: 彩云天气信息接收失败！" + t.getMessage());
                        }
                    });
                }
                Toast.makeText(getActivity(), "点击了" + area, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void queryAreaList(int level, String belongArea) {
        commonTitleBar.getCenterTextView().setText(belongArea);
        dataList.clear();
        if (level == LEVEL_PROVINCE) {
            commonTitleBar.getLeftImageButton().setVisibility(View.INVISIBLE);
            List<String> dataFromDb = AppDataBase.getAppDatabase(MyApplication.getAppContext()).provinceDAO().getAllProvinceNames();
            if (dataFromDb != null && !dataFromDb.isEmpty()) {
                dataList.addAll(dataFromDb);
                areaListAdapter.notifyDataSetChanged();
            }
            Api.getApiService("http://guolin.tech/api/").getProvince().enqueue(new Callback<List<ProvinceResponse>>() {
                @Override
                public void onResponse(Call<List<ProvinceResponse>> call, Response<List<ProvinceResponse>> responseList) {
                    if (responseList.body() != null) {
                        final List<ProvinceEntity> provinceEntities = new ArrayList<>();
                        for (ProvinceResponse provinceResponse : responseList.body()) {
                            if (!dataList.contains(provinceResponse.getName())) {
                                dataList.add(provinceResponse.getName());
                            }
                            ProvinceEntity provinceEntity = new ProvinceEntity(provinceResponse.getName(), provinceResponse.getId());
                            provinceEntities.add(provinceEntity);
                        }
                        Executors.newSingleThreadExecutor().submit(new Runnable() {
                            @Override
                            public void run() {
                                AppDataBase.getAppDatabase(MyApplication.getAppContext()).provinceDAO().insert(provinceEntities);
                            }
                        });
                    }
                    areaListAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<List<ProvinceResponse>> call, Throwable t) {
                    Log.d(TAG, "onFailure: get provinces failed!" + t.getMessage());
                }
            });
            currentLevel = LEVEL_PROVINCE;
        } else if (level == LEVEL_CITY) {
            commonTitleBar.getLeftImageButton().setVisibility(View.VISIBLE);
            final int belongProvinceId = AppDataBase.getAppDatabase(MyApplication.getAppContext()).provinceDAO().getProvinceId(belongArea);
            List<String> dataFromDb = AppDataBase.getAppDatabase(MyApplication.getAppContext()).cityDAO().getCityNamesByProvinceId(belongProvinceId);
            if (dataFromDb != null && !dataFromDb.isEmpty()) {
                dataList.addAll(dataFromDb);
                areaListAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(0);
            }
            Api.getApiService("http://guolin.tech/api/").getCity(String.valueOf(belongProvinceId)).enqueue(new Callback<List<CityResponse>>() {
                @Override
                public void onResponse(Call<List<CityResponse>> call, Response<List<CityResponse>> response) {
                    if (response.body() != null) {
                        final List<CityEntity> cityEntities = new ArrayList<>();
                        for (CityResponse cityResponse : response.body()) {
                            if (!dataList.contains(cityResponse.getName())) {
                                dataList.add(cityResponse.getName());
                            }
                            CityEntity city = new CityEntity(cityResponse.getName(), cityResponse.getId(), belongProvinceId);
                            cityEntities.add(city);
                        }
                        Executors.newSingleThreadExecutor().submit(new Runnable() {
                            @Override
                            public void run() {
                                AppDataBase.getAppDatabase(MyApplication.getAppContext()).cityDAO().insert(cityEntities);
                            }
                        });
                    }
                    areaListAdapter.notifyDataSetChanged();
                    recyclerView.smoothScrollToPosition(0);
                }

                @Override
                public void onFailure(Call<List<CityResponse>> call, Throwable t) {
                    Log.d(TAG, "onFailure: get cities failed!" + t.getMessage());
                }
            });
            currentLevel = LEVEL_CITY;
        } else if (level == LEVEL_COUNTY) {
            commonTitleBar.getLeftImageButton().setVisibility(View.VISIBLE);
            final CityEntity city = AppDataBase.getAppDatabase(MyApplication.getAppContext()).cityDAO().getCityByName(belongArea);
            final String belongCityId = String.valueOf(city.getCityCode());
            final String belongProvinceId = String.valueOf(city.getProvinceId());
            List<String> dataFromDb = AppDataBase.getAppDatabase(MyApplication.getAppContext()).countyDAO().getCountyNamesByCityId(city.getCityCode());
            if (dataFromDb != null && !dataFromDb.isEmpty()) {
                dataList.addAll(dataFromDb);
                areaListAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(0);
            }
            Api.getApiService("http://guolin.tech/api/").getCounty(belongProvinceId, belongCityId).enqueue(new Callback<List<CountyResponse>>() {
                @Override
                public void onResponse(Call<List<CountyResponse>> call, Response<List<CountyResponse>> response) {
                    if (response.body() != null) {
                        final List<CountyEntity> countyEntities = new ArrayList<>();
                        for (CountyResponse countyResponse : response.body()) {
                            if (!dataList.contains(countyResponse.getName())) {
                                dataList.add(countyResponse.getName());
                            }
                            CountyEntity countyEntity = new CountyEntity(countyResponse.getName(), countyResponse.getId(), countyResponse.getWeather_id(), city.getCityCode());
                            countyEntities.add(countyEntity);
                        }
                        Executors.newSingleThreadExecutor().submit(new Runnable() {
                            @Override
                            public void run() {
                                AppDataBase.getAppDatabase(MyApplication.getAppContext()).countyDAO().insert(countyEntities);
                            }
                        });
                    }
                    areaListAdapter.notifyDataSetChanged();
                    recyclerView.smoothScrollToPosition(0);
                }

                @Override
                public void onFailure(Call<List<CountyResponse>> call, Throwable t) {
                    Log.d(TAG, "onFailure: get counties failed!" + t.getMessage());
                }
            });
            currentLevel = LEVEL_COUNTY;
        }
    }


}
