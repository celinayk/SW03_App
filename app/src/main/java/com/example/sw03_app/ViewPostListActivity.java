package com.example.sw03_app;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ViewPostListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_post_list);
    }

    //글쓰기 버튼 누르면 이벤트 처리
    public void onNextButtonClick(View view) {
        //새로운 엑티비티를 시작하기 위한 Intent 생성
        Intent intent = new Intent(this, WriteActivity.class);

        //새로운 엑티비티 시작
        startActivity(intent);
    }

}