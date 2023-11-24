package com.example.sw03_app;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface RetrofitService {
    @POST("/api/kakao/save")
    Call<String> saveKakaoUserInfo(@Header("Authorization") String authorizationHeader, @Body KakaoUserInfo kakaoUserInfo);

    @POST("/api/seat/save")
    Call<String> saveSeatUserInfo(@Body SeatUserInfo seatUserInfo);
}

