package com.example.sw03_app;

import android.content.Intent;
import android.os.Bundle;
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
        /*필요한 기능 = seat_view에서 모든 좌석의 데이터 정보를 불러온 뒤, 좌석에 이용자 정보가 있는 경우 예약이 불가하게 해야한다.
        그 경우, 좌석 색깔이 빨간색으로 변경되어야 하고, click이 불가능 하게 enable되어야 한다.*/

        int seatCount = 98; // 좌석버튼 개수
        seatBtns = new Button[seatCount]; // 버튼을 담을 배열

        // Retrofit 인스턴스 생성
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

        FloatingActionButton out_button; //x버튼
        out_button = (FloatingActionButton) findViewById(R.id.outButton);//x버튼

        //좌석버튼 리스너 반복문으로 만드는 코드
        for (int i = 1; i <= seatCount; i++) {
            int buttonId = getResources().getIdentifier("seat" + i, "id", getPackageName());
            seatBtns[i - 1] = findViewById(buttonId);

            seatBtns[i - 1].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), NoReservation.class);
                    startActivity(intent);

                    /* 서버 - 좌석을 누르면 회원의 아이디로 회원의 예약좌석 정보를 조회해야한다.
                    조회하고 좌석예약정보가 이미 존재하면, yesReservation실행
                                       존재하지 않으면, noReservation실행
                    원래는 위에 처럼 동작해야하는데, 일단 잘 돌아가는지 보여주기 위해서 NoReservation과 연결해뒀습니다.
                     */
                }
            });
        }

        //x 버튼 클릭할때, 홈으로 나가는 코드
        out_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });


    }

    private void updateSeatButtons() {for (int i = 1; i <= seatBtns.length; i++) {
        int buttonId = getResources().getIdentifier("seat" + i, "id", getPackageName());
        seatBtns[i - 1] = findViewById(buttonId);

        // 서버에서 받아온 정보를 기반으로 예약 버튼 표시 여부 결정
        boolean isReservationPossible = seatList.get(i - 1).isAble(); // able이 1이면 예약 가능

        if (isReservationPossible) {
            seatBtns[i - 1].setVisibility(View.VISIBLE); // 예약 가능한 좌석이면 버튼을 보이게 설정
        } else {
            seatBtns[i - 1].setVisibility(View.GONE); // 예약 불가능한 좌석이면 버튼을 숨기게 설정
        }

        seatBtns[i - 1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 여기에 좌석을 클릭했을 때의 동작 추가
                // 예약 가능한 좌석인지 여부를 확인하여 처리
            }
        });
    }
    }
}