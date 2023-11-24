package com.example.sw03_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class YesReservation extends AppCompatActivity {

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
                /*데이터베이스에서 사용자의 좌석 정보를 가져오고, 한시간 연장하기*/
            }
        });

        //자리 취소 버튼 클릭
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*데이터베이스에서 사용자의 좌석 정보를 가져오고, 자리 취소하기*/
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
}
