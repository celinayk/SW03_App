package com.example.sw03_app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class NoReservation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.not_reservation);

        Button reservationBtn;

        reservationBtn = (Button) findViewById(R.id.reservationBtn);

        reservationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*회원에게 좌석 예약 데이터 추가하기*/
            }
        });


    }
}
