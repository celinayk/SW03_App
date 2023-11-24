package com.example.sw03_app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class ViewSeat extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seat_view);

        // 좌석버튼 개수
        int seatCount = 10;

        // 버튼을 담을 배열
        Button[] seatBtns = new Button[seatCount];

        for (int i = 1; i <= seatCount; i++) {
            int buttonId = getResources().getIdentifier("seat" + i, "id", getPackageName());

            seatBtns[i - 1] = findViewById(buttonId);

            seatBtns[i - 1].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    /* 서버 - 좌석을 누르면 회원의 아이디로 회원의 예약좌석 정보를 조회해야한다.
                    조회하고 좌석예약정보가 이미 존재하면, yesReservation실행
                                       존재하지 않으면, noReservation실행
                     */

                }
            });
        }
    }
}
