package com.example.sw03_app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class YesReservation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yes_reservation);

        Button extendBtn, cancleBtn;

        extendBtn = (Button) findViewById(R.id.extendBtn);
        cancleBtn = (Button) findViewById(R.id.cancleBtn);

        /* remainingTime, seatNumber 이라는 이름의 TextView에 데이터에서 불러온 값을 넣어줘야함.
        remainingTime은 남은 시간으로, 종료시간까지 남은 시간이 들어간다.
        seatNumber는 좌석 번호로, 사용자가 사용중인 좌석번호가 들어간다.
         */
        TextView remainingTime = findViewById(R.id.remainingTime); //받아온 남은 시간 데이터를 remainingTime에 넣으면 데이터가 출력된다.
        remainingTime.setText((CharSequence) remainingTime);

        TextView seatNumber = findViewById(R.id.seatNumber);//받아온 좌석번호 데이터를 remainingTime에 넣으면 데이터가 출력된다.
        seatNumber.setText((CharSequence) seatNumber);




        //자리 연장 버튼 클릭
        extendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*데이터베이스에서 사용자의 좌석 정보를 가져오고, 한시간 연장하기*/
            }
        });

        //자리 연장 버튼 클릭
        cancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*데이터베이스에서 사용자의 좌석 정보를 가져오고, 자리 취소하기*/
            }
        });
    }
}
