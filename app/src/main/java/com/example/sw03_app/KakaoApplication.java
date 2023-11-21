package com.example.sw03_app;

import android.app.Application;

import com.kakao.sdk.common.KakaoSdk;

public class KakaoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //KakaoSdk.init(this, "4353b6cd76fe11134b7a89478be0e2df");

    }
}
