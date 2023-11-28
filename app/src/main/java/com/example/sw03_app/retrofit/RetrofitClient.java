package com.example.sw03_app.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if(retrofit==null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://" + "10.0.2.2" + ":8080/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            return retrofit;
        }
        return retrofit;
    }
}
