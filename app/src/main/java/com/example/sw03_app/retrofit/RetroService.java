package com.example.sw03_app.retrofit;

import com.example.sw03_app.dto.Board;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface RetroService {

    // @GET (EndPoint - 자원위치 (URL)
    //@GET("posts/{post}")
    //Call<board> getPosts(@Path("post") Stirng post )


    @GET("/api/board")
    Call<ArrayList<Board>> getBoards();
}
