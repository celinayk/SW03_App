package com.example.sw03_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.kakao.sdk.common.util.Utility;

public class MainActivity extends AppCompatActivity {

    Fragment homeFragment, spotFragment, settingFragment;
    BottomNavigationView bottomNavigationView;
    Button seatViewBtn; //'좌석조회'버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initLayout();

        getHashKey();

        viewSeat(); //좌석조회관련 함수

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

    //좌석 조회 코드들. home에서 좌석 조회 버튼을 클릭하면, 좌석현황을 보여주는 화면으로 넘어간다.)
    private void viewSeat() {
        seatViewBtn = (Button)findViewById(R.id.seatViewBtn); //자리 조회 버튼 객체 생성
        //좌석 조회 버튼을 클릭하면,ViewSeat실행
        seatViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ViewSeat.class);
                startActivity(intent);
            }
        });


    }

}
