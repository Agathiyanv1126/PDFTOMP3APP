package com.example.app.api;

import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://bl2fz1wm-8000.inc1.devtunnels.ms/api/") // Base URL
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create())) // Gson Converter
                    .build();
        }
        return retrofit;
    }
}
