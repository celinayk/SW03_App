package com.example.sw03_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NoReservation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_reservation);

        Button reservationBtn; //예약버튼
        FloatingActionButton out_button, redo_button; //x버튼(홈으로),<버튼(뒤로)

        reservationBtn = (Button) findViewById(R.id.reservationBtn);
        out_button = (FloatingActionButton)findViewById(R.id.outButton);
        redo_button = (FloatingActionButton)findViewById(R.id.redoButton);


        reservationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*회원에게 좌석 예약 데이터 추가하기*/
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