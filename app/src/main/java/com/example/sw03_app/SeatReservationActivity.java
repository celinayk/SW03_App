package com.example.sw03_app;

import android.content.Context;
import android.content.DialogInterface;
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
import android.Manifest;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.logging.LogRecord;

public class SeatReservationActivity extends AppCompatActivity {
    private static final long ONE_HOUR_IN_MILLIS = 60 * 60 * 1000;

    //6초
    private static final long ONE_MIN_IN_MILLIS = 6 * 1000;

    private static final int PERMISSION_REQUEST_CODE = 1001;

    private boolean isReturnRequested = false;
    private Button reserveButton;
    private UserLocationManager userLocationManager;

    private ApiService apiService;

    private void setupReserveButton() {
        userLocationManager = new UserLocationManager(this, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // 위치가 변경될 때마다 실행되는 부분
            }
        });
        reserveButton = findViewById(R.id.reserve_button);
        reserveButton.setOnClickListener(new View.OnClickListener() {
            boolean prevChecked = false;

            @Override
            public void onClick(View v) {
                //todo 변수 값 수정해야함
                //10분마다 위치 확인하고
                new CountDownTimer(ONE_HOUR_IN_MILLIS, ONE_MIN_IN_MILLIS) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        Location libraryLocation = getLibraryLocation();
                        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        Location currentLocation = userLocationManager.getLastKnownLocation(SeatReservationActivity.this);

                        System.out.println(currentLocation.getLatitude() + "," + currentLocation.getLongitude());
                        if (currentLocation != null) {
                            float distance = currentLocation.distanceTo(libraryLocation);
                            //오차범위 포함 + 10미터
                            if (distance > 110) {
                                if(prevChecked){
                                    Toast.makeText(SeatReservationActivity.this, "도서관에서 100m 멀어졌어요! 좌석이 취소됩니다", Toast.LENGTH_SHORT).show();
                                    cancelRequest();
                                    return;
                                }else{
                                    AlertDialog alertDialog = getAlertDialog("도서관에서 100m 멀어졌어요! 10분 안에 복귀해주세요.", "확인");
                                    alertDialog.show();
                                    cancelRequest();
                                }
                                prevChecked = true;
                            } else {
                                Toast.makeText(SeatReservationActivity.this, "디버깅 용 도서관에서 100미터 이내입니다.", Toast.LENGTH_SHORT).show();
                                prevChecked = false;
                            }
                        } else {
                            if(prevChecked == false){
                                Toast.makeText(SeatReservationActivity.this, "위치 정보를 확인할 수 없습니다 위치 정보를 확인해주세요", Toast.LENGTH_SHORT).show();
                                prevChecked = true;
                            }else{
                                Toast.makeText(SeatReservationActivity.this, "위치 정보를 확인할 수 없습니다 좌석이 취소됩니다", Toast.LENGTH_SHORT).show();
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
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        // 위치 권한 확인 및 요청
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        } else {
            // 위치 권한이 이미 허용된 경우에는 예약 버튼을 설정합니다.
            setupReserveButton();
        }

    }

    @NotNull
    private AlertDialog getAlertDialog(String message, String check) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SeatReservationActivity.this);
        alertDialogBuilder.setMessage(message)
                .setPositiveButton(check, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        return alertDialog;
    }


    private Location getLibraryLocation() {
        Location libraryLocation = new Location("library");
        libraryLocation.setLatitude(35.246440); // Library's latitude
        libraryLocation.setLongitude(128.690815); // Library's longitude
        return libraryLocation;
    }

    private void cancelRequest() {
        Toast.makeText(SeatReservationActivity.this, "장작", Toast.LENGTH_SHORT).show();

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
}
