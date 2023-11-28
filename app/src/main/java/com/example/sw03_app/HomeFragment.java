package com.example.sw03_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.home, container, false);

        // 버튼 클릭 시 ViewSeat 액티비티로 이동
        Button seatViewBtn = view.findViewById(R.id.seatViewBtn);
        seatViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ViewSeat.class);
                startActivity(intent);
            }
        });
        return view;
    }

}
