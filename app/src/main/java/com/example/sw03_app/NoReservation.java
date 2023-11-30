package com.example.sw03_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kakao.sdk.auth.AuthApiClient;
import com.kakao.sdk.common.model.KakaoSdkError;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.AccessTokenInfo;
import com.kakao.sdk.user.model.User;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class NoReservation extends AppCompatActivity {

    private static final String BASE_URL = "http://10.0.2.2:8080/";
    private RetrofitService retrofitService;

    private long snsId;
    private long seatId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_reservation);

        Button reservationBtn; // 예약 버튼
        FloatingActionButton out_button, redo_button; // x 버튼(홈으로), < 버튼(뒤로)

        reservationBtn = findViewById(R.id.reservationBtn);
        out_button = findViewById(R.id.outButton);
        redo_button = findViewById(R.id.redoButton);

        reservationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // snsId를 받아오는 부분
                UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
                    @Override
                    public Unit invoke(User user, Throwable throwable) {
                        snsId = user.getId();

                        // 서버에 예약 요청 보내는 부분
                        sendReservationRequest();

                        return null;
                    }
                });
            }
        });

        // x 버튼 누르면 나가기, activity_main 보임
        out_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        // < 버튼 누르면 뒤로, 즉 seat_view가 보임
        redo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ViewSeat.class);
                startActivity(intent);
            }
        });
    }

    private void sendReservationRequest() {
        // 서버에 예약 요청을 보내는 부분

        Intent intent = getIntent();
        if (intent != null) {
            seatId = intent.getLongExtra("seatId", -1);
            // 로그를 추가하여 seatId 값 확인
            Log.d("NoReservation", "Received seatId: " + seatId);
        }

        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(new OkHttpClient())
                .build();
        retrofitService = retrofit.create(RetrofitService.class);
        Call<Void> call = retrofitService.reserveSeat(seatId, snsId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // 성공적으로 예약됨
                } else {
                    // 서버 응답이 실패일 때의 처리
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // 통신 실패 처리
            }
        });
    }
}