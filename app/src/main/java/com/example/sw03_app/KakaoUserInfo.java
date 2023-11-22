package com.example.sw03_app;

public class KakaoUserInfo {
    private String kakaoUserName;
    private String kakaoUserProfile;
    private String oAuthToken;

    public KakaoUserInfo(String kakaoUserName, String kakaoUserProfile){
        this.kakaoUserName = kakaoUserName;
        this.kakaoUserProfile = kakaoUserProfile;
        //this.oAuthToken = oAuthToken;
    }
}
