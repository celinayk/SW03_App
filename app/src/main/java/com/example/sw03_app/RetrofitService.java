package com.example.sw03_app;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RetrofitService {
    @POST("/api/user/{snsId}")
    Call<String> saveKakaoUserInfo(@Path("snsId") Long snsId,
                                   @Header("Authorization") String authorizationHeader,
                                   @Body String nickname);
    @GET("/api/seat")
    Call<List<SeatUserInfo>> getSeats();

    @POST("/api/seat/{seatId}/{snsId}")
    Call<Void> reserveSeat(@Path("seatId") Long seatId, @Path("snsId") Long snsId);

    @DELETE("/api/seat/{snsId}")
    Call<Void> deleteSeat(@Path("snsId") Long snsId);  //박찬양
    @DELETE("/api/seat/{snsId}")
    Call<Void> cancelSeat(@Path("snsId") Long seatId); //유승빈

    @PUT("/api/seat/{seatId}")
    Call<Void> extendSeat(@Path("seatId") Long seatId);

    @DELETE("/api/board/{boardIdToDelete}")
    Call<Void> deleteBoard(@Path("boardIdToDelete") Integer boardIdToDelete);
}

