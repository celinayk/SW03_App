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

import java.util.logging.LogRecord;

public class SeatReservationActivity extends AppCompatActivity {
    private static final long ONE_HOUR_IN_MILLIS = 60 * 60 * 1000;


    //6초
    private static final long ONE_MIN_IN_MILLIS = 6 * 1000;

    private static final int PERMISSION_REQUEST_CODE = 1001;

    private boolean isReturnRequested = false;
    private Button reserveButton;
    private UserLocationManager userLocationManager;

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

                        if (currentLocation != null) {
                            Toast.makeText(SeatReservationActivity.this, currentLocation.getLatitude() + " , " + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();

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
    }
}
