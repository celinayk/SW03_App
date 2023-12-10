package com.example.sw03_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kakao.sdk.user.UserApiClient;
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

public class YesReservation extends AppCompatActivity {

    private static final String BASE_URL = "http://10.0.2.2:8080/";
    private RetrofitService retrofitService;
    private long snsId;
    private long seatId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yes_reservation);

        Button extendBtn, cancelBtn; //연장버튼, 취소버튼
        FloatingActionButton out_button, redo_button; //x버튼(홈으로),<버튼(뒤로)

        extendBtn = (Button) findViewById(R.id.extendBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        out_button = (FloatingActionButton)findViewById(R.id.outButton);
        redo_button = (FloatingActionButton)findViewById(R.id.redoButton);

        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
            @Override
            public Unit invoke(User user, Throwable throwable) {
                snsId = user.getId();
                return null;
            }
        });


        /* remainingTime, seatNumber 이라는 이름의 TextView에 mysql에서 불러온 값을 넣어줘야함.
        remainingTime은 남은 시간으로, 종료시간까지 남은 시간이 들어간다.
        seatNumber는 좌석 번호로, 사용자가 사용중인 좌석번호가 들어간다.

        TextView remainingTime = findViewById(R.id.remainingTime); //받아온 남은 시간 데이터를 remainingTime에 넣으면 데이터가 출력된다.
        remainingTime.setText((CharSequence) remainingTime);

        TextView seatNumber = findViewById(R.id.seatNumber);//받아온 좌석번호 데이터를 remainingTime에 넣으면 데이터가 출력된다.
        seatNumber.setText((CharSequence) seatNumber);
           */



        //자리 연장 버튼 클릭
        extendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = getIntent();
                if (intent != null) {
                    seatId = intent.getLongExtra("seatId", -1);
                    // 로그를 추가하여 seatId 값 확인
                    Log.d("YesReservation-Extend", "Received seatId: " + seatId);
                    Log.d("YesReservation-Extend", "Received snsId : " + snsId);
                }

                Gson gson = new GsonBuilder().setLenient().create();
                Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .client(new OkHttpClient())
                        .build();
                retrofitService = retrofit.create(RetrofitService.class);

                Call<Void> call = retrofitService.extendSeat(seatId);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            // API 호출 성공
                            Toast.makeText(getApplicationContext(), "좌석이 연장되었습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            // API 호출 실패
                            Toast.makeText(getApplicationContext(), "좌석 연장에 실패했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "연장은 한 번만 가능합니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                exitCurrentPage();
            }
        });

        //자리 취소 버튼 클릭
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = getIntent();
                if (intent != null) {
                    seatId = intent.getLongExtra("seatId", -1);
                    // 로그를 추가하여 seatId 값 확인
                    Log.d("YesReservation-Cancel", "Received seatId: " + seatId);
                    Log.d("YesReservation-cancel", "Received snsId : " + snsId);
                }

                Gson gson = new GsonBuilder().setLenient().create();
                Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .client(new OkHttpClient())
                        .build();
                retrofitService = retrofit.create(RetrofitService.class);
                Call<Void> call = retrofitService.cancelSeat(snsId);

                // 비동기적으로 실행하기 위해 enqueue 메소드 호출
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        // 요청이 성공적으로 처리됐을 때의 동작
                        if (response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "좌석이 취소되었습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "좌석이 취소에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                exitCurrentPage();
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

    private void exitCurrentPage() {
        finish();
    }
}