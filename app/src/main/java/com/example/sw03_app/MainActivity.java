package com.example.sw03_app;

import android.Manifest;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.kakao.sdk.common.util.Utility;
import com.kakao.sdk.user.model.User;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    Fragment homeFragment, spotFragment, settingFragment;
    BottomNavigationView bottomNavigationView;
    private static final int PERMISSION_REQUEST_CODE = 1001;
    private UserLocationManager userLocationManager;
    private boolean prevChecked = false;
    private RetrofitService retrofitService;
    private static final long ONE_HOUR_IN_MILLIS = 60 * 60 * 1000;
    private static final long ONE_MIN_IN_MILLIS = 6 * 1000;
    private Long snsId;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 권한이 부여되어 있지 않은 경우 권한 요청
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        } else {
            Log.d("Permission", "ACCESS_FINE_LOCATION 권한이 이미 부여되었습니다.");
        }

        initLayout();
        //startCount();
    }

    private void getHashKey() {
        String keyhash = Utility.INSTANCE.getKeyHash(this);
        System.out.println("Key Hash: " + keyhash);
    }

    private void initLayout() {
        /* 하단 바 레이아웃 관련 코드들 */
        homeFragment = new HomeFragment();
        spotFragment = new BoardFragment();
        settingFragment = new SettingFragment();
        switchFragment(homeFragment);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();
                if (itemId == R.id.home) {
                    switchFragment(homeFragment);

                    return true;
                } else if (itemId == R.id.board) {
                    switchFragment(spotFragment);
                    return true;
                } else if (itemId == R.id.setting) {
                    switchFragment(settingFragment);
                    return true;
                }
                return false;
            }
        });
        /* 하단 바 레이아웃 관련 코드들 끝 */
    }

    private void switchFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static Location getLibraryLocation() {
        Location libraryLocation = new Location("library");
        libraryLocation.setLatitude(35.246440); // Library's latitude
        libraryLocation.setLongitude(128.690815); // Library's longitude
        return libraryLocation;
    }

    private void startCount() {
        Log.d("Debug", "Inside startCount() function");

        userLocationManager = new UserLocationManager(MainActivity.this, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // 위치가 변경될 때마다 실행되는 부분
            }
        });

        if (userLocationManager != null) {
            Location currentLocation = userLocationManager.getLastKnownLocation(MainActivity.this);
            // 위치 정보 사용
        }

        System.out.println(userLocationManager);
        System.out.println(userLocationManager.getLastKnownLocation(MainActivity.this));

        // CountDownTimer 객체 생성
        countDownTimer = new CountDownTimer(ONE_HOUR_IN_MILLIS, ONE_MIN_IN_MILLIS) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d("Debug", "Inside onTick() function");

                Location libraryLocation = getLibraryLocation();
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Location currentLocation = userLocationManager.getLastKnownLocation(MainActivity.this);

                System.out.println(currentLocation.getLatitude() + "," + currentLocation.getLongitude());
                if (currentLocation != null) {
                    float distance = currentLocation.distanceTo(libraryLocation);
                    //오차범위 포함 + 10미터
                    if (distance > 110) {
                        if (prevChecked) {
                            Toast.makeText(MainActivity.this, "도서관에서 100m 멀어졌어요! 좌석이 취소됩니다", Toast.LENGTH_SHORT).show();
                            cancelRequest();
                            cancel(); // 타이머 취소
                            return;
                        } else {
                            AlertDialog alertDialog = getAlertDialog("도서관에서 100m 멀어졌어요! 10분 안에 복귀해주세요.", "확인");
                            alertDialog.show();
                            cancelRequest();
                            cancel(); // 타이머 취소
                        }
                        prevChecked = true;
                    } else {
                        Toast.makeText(MainActivity.this, "디버깅 용 도서관에서 100미터 이내입니다.", Toast.LENGTH_SHORT).show();
                        prevChecked = false;
                    }
                } else {
                    if (prevChecked == false) {
                        Toast.makeText(MainActivity.this, "위치 정보를 확인할 수 없습니다 위치 정보를 확인해주세요", Toast.LENGTH_SHORT).show();
                        prevChecked = true;
                    } else {
                        Toast.makeText(MainActivity.this, "위치 정보를 확인할 수 없습니다 좌석이 취소됩니다", Toast.LENGTH_SHORT).show();
                        cancelRequest();
                        cancel(); // 타이머 취소
                        return;
                    }
                }
            }

            @Override
            public void onFinish() {
                cancelRequest(); // 위치 벗어나면 delete API 호출
                cancel(); // 타이머 취소
            }
        };

        countDownTimer.start(); // 타이머 시작
    }


    @NotNull
    private AlertDialog getAlertDialog(String message, String check) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
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
        Toast.makeText(MainActivity.this, "장작", Toast.LENGTH_SHORT).show();

        // DELETE API 호출
        Call<Void> call = retrofitService.deleteSeat(snsId);
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