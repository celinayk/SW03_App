package com.example.sw03_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kakao.sdk.auth.AuthApiClient;
import com.kakao.sdk.common.model.KakaoSdkError;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.AccessTokenInfo;

import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ViewSeat extends AppCompatActivity {

    private List<SeatUserInfo> seatList; // Seat 클래스 import
    private Button[] seatBtns;
    private RetrofitService retrofitService;
    private static final String BASE_URL = "http://10.0.2.2:8080/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seat_view); //seat_view보여줌. (좌석현황)

        int seatCount = 98; // 좌석버튼 개수
        seatBtns = new Button[seatCount]; // 버튼을 담을 배열

        // Retrofit 인스턴스 생성 -> 서버에서 예약가능 좌석 받아오기
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson)).client(new OkHttpClient()).build();
        retrofitService = retrofit.create(RetrofitService.class);

        Call<List<SeatUserInfo>> call = retrofitService.getSeats();
        call.enqueue(new Callback<List<SeatUserInfo>>() {
            @Override
            public void onResponse(Call<List<SeatUserInfo>> call, Response<List<SeatUserInfo>> response) {
                if (response.isSuccessful()){
                    seatList = response.body();
                    updateSeatButtons();  //좌석 정보를 받아온 후 UI 업데이트
                }
            }

            @Override
            public void onFailure(Call<List<SeatUserInfo>> call, Throwable t) {

            }
        });

        exitButton();


    }

    private void exitButton() {
        FloatingActionButton out_button; //x버튼
        out_button = (FloatingActionButton) findViewById(R.id.outButton);//x버튼

        //x 버튼 클릭할때, 홈으로 나가는 코드
        out_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updateSeatButtons() {
        for (int i = 1; i <= seatBtns.length; i++) {
            int buttonId = getResources().getIdentifier("seat" + i, "id", getPackageName());
            seatBtns[i - 1] = findViewById(buttonId);

            // 서버에서 받아온 정보를 기반으로 예약 버튼 표시 여부 결정
            boolean isReservationPossible = seatList.get(i - 1).isAble(); // able이 1이면 예약 가능

            if (isReservationPossible) {
                seatBtns[i - 1].setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.green)); // 예약 가능한 좌석의 배경색 초기화
                seatBtns[i - 1].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), NoReservation.class);
                        startActivity(intent);
                    }
                });
            } else {
                seatBtns[i - 1].setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.red));
                seatBtns[i - 1].setOnClickListener(null); // 예약 불가능한 좌석은 클릭 이벤트 제거
            }
        }
    }
}