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
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    Fragment homeFragment, boardFragment,spotFragment,settingFragment;
    BottomNavigationView bottomNavigationView;
    private static final int PERMISSION_REQUEST_CODE = 1001;
    private UserLocationManager userLocationManager;
    private boolean prevChecked = false;
    private RetrofitService retrofitService;
    private static final long ONE_HOUR_IN_MILLIS = 60 * 60 * 1000;
    private static final long ONE_MIN_IN_MILLIS = 600 * 1000;
    private Long snsId;
    private CountDownTimer countDownTimer;

    private int selectedItemId = R.id.home; /// 초기값은 home으로 설정

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

        getHashKey();

        // 선택된 아이템을 가져와서 설정
        int selectedItemId = getIntent().getIntExtra("selectedItemId", R.id.home);
        // BottomNavigationView에서 기본으로 선택할 아이템을 설정
        bottomNavigationView.setSelectedItemId(selectedItemId);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();


                if (itemId == R.id.home) {
                    switchFragment(homeFragment);
                    return true;
                } else if (itemId == R.id.board) {
                    switchFragment(boardFragment);
                    return true;
                } else if (itemId == R.id.setting) {
                    switchFragment(settingFragment);
                    return true;
                }
                return false;
            }
        });///
    }

    private void getHashKey() {
        String keyhash = Utility.INSTANCE.getKeyHash(this);
        System.out.println("Key Hash: " + keyhash);
    }

    private void initLayout() {
        /* 하단 바 레이아웃 관련 코드들 */
        homeFragment = new HomeFragment();
        boardFragment = new BoardFragment();
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
                    switchFragment(boardFragment);
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



}