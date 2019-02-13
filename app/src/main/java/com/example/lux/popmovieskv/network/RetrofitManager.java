package com.example.lux.popmovieskv.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static RetrofitManager instance;
    private TMDbApi service;

    private RetrofitManager(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(TMDbApi.class);
    }

    public static RetrofitManager getInstance(){
        if(instance == null){
            instance = new RetrofitManager();
        }
        return instance;
    }

    public TMDbApi service (){
        return service;
    }
}
