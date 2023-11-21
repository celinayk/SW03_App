package com.example.sw03_app;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.DELETE;
import retrofit2.http.Path;

public interface ApiService {
    @DELETE("/api/seat/{seatUsernameToDelete}")
    Call<Void> deleteSeat(@Path("seatUsernameToDelete") String seatUsernameToDelete);

    @DELETE("/api/board/{boardIdToDelete}")
    Call<Void> deleteBoard(@Path("boardIdToDelete") Integer boardIdToDelete);
}