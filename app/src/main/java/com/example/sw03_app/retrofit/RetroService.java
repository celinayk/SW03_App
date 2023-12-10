package com.example.sw03_app.retrofit;

import com.example.sw03_app.dto.Board;
import com.example.sw03_app.dto.BoardPost;
import com.example.sw03_app.dto.Comment;
import com.example.sw03_app.dto.CommentPost;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface RetroService {

    // @GET (EndPoint - 자원위치 (URL)
    //@GET("posts/{post}")
    //Call<board> getPosts(@Path("post") Stirng post )


    @GET("/api/board")
    Call<ArrayList<Board>> getBoards();

    @POST("/api/board/{sns_id}")
    Call<String> addBoard(@Path("sns_id") Long sns_id, @Body BoardPost boardPost);

    @POST("/api/user/{sns_id}")
    Call<String> addUser(@Path("sns_id") Long sns_id, @Body String nickname);

    @GET("/api/comment/{board_id}")
    Call<ArrayList<Comment>> getComments(@Path("board_id") Integer board_id);

    @POST("/api/comment/{board_id}/{sns_id}")
    Call<CommentPost> addComment(@Path("board_id") Integer board_id, @Path("sns_id") Long sns_id, @Body CommentPost commentPost);

    @POST("/api/user/{sns_id}")
    Call<String> saveKakaoUserInfo(@Path("sns_id") Long userId, @Header("Authorization") String authorizationHeader, @Body String kakaoUserName);
}
