package com.example.sw03_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kakao.sdk.auth.AuthApiClient;
import com.kakao.sdk.common.model.KakaoSdkError;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.AccessTokenInfo;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class NoReservation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_reservation);

        Button reservationBtn; //예약버튼
        FloatingActionButton out_button, redo_button; //x버튼(홈으로),<버튼(뒤로)

        reservationBtn = (Button) findViewById(R.id.reservationBtn);
        out_button = (FloatingActionButton)findViewById(R.id.outButton);
        redo_button = (FloatingActionButton)findViewById(R.id.redoButton);


        reservationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AuthApiClient.getInstance().hasToken()){
                    UserApiClient.getInstance().accessTokenInfo(new Function2<AccessTokenInfo, Throwable, Unit>() {
                        @Override
                        public Unit invoke(AccessTokenInfo accessTokenInfo, Throwable throwable) {
                            if (throwable != null) {
                                if (throwable instanceof KakaoSdkError && ((KakaoSdkError) throwable).isInvalidTokenError()) {
                                    // 로그인 필요
                                    showLoginAlert();
                                } else {
                                    // 기타 에러
                                }
                            } else {
                                // 토큰 유효성 체크 성공(필요 시 토큰 갱신됨)
                            }
                            return null;
                        }
                    });
                }
            }
        });

        //x버튼 누르면 나가기, activity_main보임
        out_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        //<버튼 누르면 뒤로, 즉 seat_view가 보임
        redo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ViewSeat.class);
                startActivity(intent);
            }
        });


    }

    private void showLoginAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(NoReservation.this);
        builder.setMessage("로그인이 필요합니다. 로그인 하시겠습니까?")
                .setPositiveButton("로그인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // 여기에 로그인을 유도하는 코드를 추가
                        // 예를 들어, LoginActivity를 띄우는 등의 동작을 수행
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // 사용자가 취소한 경우의 동작을 추가 (예: 아무런 동작 없음)
                    }
                });
        builder.create().show();
    }
}