package com.example.sw03_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.kakao.sdk.auth.AuthApiClient;
import com.kakao.sdk.common.model.KakaoSdkError;
import com.kakao.sdk.user.UserApi;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.AccessTokenInfo;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home, container, false);

        // 버튼 클릭 시 ViewSeat 액티비티로 이동
        Button seatViewBtn = view.findViewById(R.id.seatViewBtn);
        seatViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AuthApiClient.getInstance().hasToken()){
                    UserApiClient.getInstance().accessTokenInfo(new Function2<AccessTokenInfo, Throwable, Unit>() {
                        @Override
                        public Unit invoke(AccessTokenInfo accessTokenInfo, Throwable throwable) {
                            if (throwable != null) {
                                if (throwable instanceof KakaoSdkError && ((KakaoSdkError) throwable).isInvalidTokenError()) {
                                } else {
                                }
                            } else {
                                Intent intent = new Intent(getActivity(), ViewSeat.class);
                                startActivity(intent);
                            }
                            return null;
                        }
                    });
                }   else {
                    showLoginAlert();
                }
            }
        });
        return view;
    }
    private void showLoginAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("로그인 먼저 해주세요!")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        moveToMyInformationFragment();
                    }
                })
                .show();
    }

    // MyInformationFragment로 이동하는 메서드
    private void moveToMyInformationFragment() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        MyInformationFragment myInformationFragment = new MyInformationFragment();

        fragmentTransaction.replace(R.id.fragment_container, myInformationFragment);

        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }


}