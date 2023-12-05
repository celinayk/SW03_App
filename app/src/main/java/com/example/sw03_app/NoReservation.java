package com.example.sw03_app;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.ActivityCompat;
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
import org.jetbrains.annotations.NotNull;
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


    private static final long ONE_HOUR_IN_MILLIS = 60 * 60 * 1000;

    //6초
    private static final long ONE_MIN_IN_MILLIS = 6 * 1000;

    private static final int PERMISSION_REQUEST_CODE = 1001;
    private UserLocationManager userLocationManager;
    private boolean prevChecked = false;

    private ApiService apiService;

    public static Location getLibraryLocation() {
        Location libraryLocation = new Location("library");
        libraryLocation.setLatitude(35.246440); // Library's latitude
        libraryLocation.setLongitude(128.690815); // Library's longitude
        return libraryLocation;
    }


    private void startCount(){
        userLocationManager = new UserLocationManager(NoReservation.this, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // 위치가 변경될 때마다 실행되는 부분
            }
        });

        System.out.println(userLocationManager);
        System.out.println(userLocationManager.getLastKnownLocation(NoReservation.this));

        new CountDownTimer(ONE_HOUR_IN_MILLIS, ONE_MIN_IN_MILLIS) {
            @Override
            public void onTick(long millisUntilFinished) {


                Location libraryLocation = getLibraryLocation();
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Location currentLocation = userLocationManager.getLastKnownLocation(NoReservation.this);

                System.out.println(currentLocation.getLatitude() + "," + currentLocation.getLongitude());
                if (currentLocation != null) {
                    float distance = currentLocation.distanceTo(libraryLocation);
                    //오차범위 포함 + 10미터
                    if (distance > 110) {
                        if(prevChecked){
                            Toast.makeText(NoReservation.this, "도서관에서 100m 멀어졌어요! 좌석이 취소됩니다", Toast.LENGTH_SHORT).show();
                            cancelRequest();
                            return;
                        }else{
                            AlertDialog alertDialog = getAlertDialog("도서관에서 100m 멀어졌어요! 10분 안에 복귀해주세요.", "확인");
                            alertDialog.show();
                            cancelRequest();
                        }
                        prevChecked = true;
                    } else {
                        Toast.makeText(NoReservation.this, "디버깅 용 도서관에서 100미터 이내입니다.", Toast.LENGTH_SHORT).show();
                        prevChecked = false;
                    }
                } else {
                    if(prevChecked == false){
                        Toast.makeText(NoReservation.this, "위치 정보를 확인할 수 없습니다 위치 정보를 확인해주세요", Toast.LENGTH_SHORT).show();
                        prevChecked = true;
                    }else{
                        Toast.makeText(NoReservation.this, "위치 정보를 확인할 수 없습니다 좌석이 취소됩니다", Toast.LENGTH_SHORT).show();
                        cancelRequest();
                        return;
                    }
                }
            }


            @Override
            public void onFinish() {
                cancelRequest();
            }

        }.start();

    }


    @NotNull
    private AlertDialog getAlertDialog(String message, String check) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NoReservation.this);
        alertDialogBuilder.setMessage(message)
                .setPositiveButton(check, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        return alertDialog;
    }


    private void cancelRequest() {
        Toast.makeText(NoReservation.this, "장작", Toast.LENGTH_SHORT).show();

        // DELETE API 호출
        String seatUsernameToDelete = "praisebak"; // 삭제할 좌석의 ID
        Call<Void> call = apiService.deleteSeat(seatUsernameToDelete);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // 성공적으로 삭제됨

                } else {
                    //클라이언트에서 서버로 요청을 보내는걸 실패했다? 그럼 어떡해요
                    // 요청 실패 처리
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }

        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_reservation);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }else{

        }


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
                        System.out.println("냠냠");

                        snsId = user.getId();
                        // 서버에 예약 요청 보내는 부분
                        startCount();

//                        sendReservationRequest();
//                        saveReservedSeatToSharedPreferences();
//                        exitCurrentPage();

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

    private void saveReservedSeatToSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("isSeatReserved", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isSeatReserved", true);
        editor.apply();

        Log.d("NoReserVation", "Saved isSeatReserved: true");
    }

    private void exitCurrentPage() {
        finish();
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