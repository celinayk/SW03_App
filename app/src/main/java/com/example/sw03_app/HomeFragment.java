package com.example.sw03_app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
                if (AuthApiClient.getInstance().hasToken()) {
                    UserApiClient.getInstance().accessTokenInfo(new Function2<AccessTokenInfo, Throwable, Unit>() {
                        @Override
                        public Unit invoke(AccessTokenInfo accessTokenInfo, Throwable throwable) {
                            if (throwable != null) {
                                if (throwable instanceof KakaoSdkError && ((KakaoSdkError) throwable).isInvalidTokenError()) {
                                    // 토큰이 유효하지 않은 경우
                                    showLoginAlert();
                                } else {
                                    // 기타 오류
                                }
                            } else {
                                if (hasReservedSeat()) {
                                    // 이미 예약한 좌석이 있을 때 동작 안함
                                    showAlreadyReservedAlert();
                                } else {
                                    // 예약한 좌석이 없으면 ViewSeat 액티비티로 이동
                                    Intent intent = new Intent(getActivity(), ViewSeat.class);
                                    startActivity(intent);
                                }
                            }
                            return null;
                        }
                    });
                } else {
                    showLoginAlert();
                }
            }
        });
        return view;
    }

    private boolean hasReservedSeat() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("isSeatReserved", Context.MODE_PRIVATE);
        boolean isSeatReserved = sharedPreferences.getBoolean("isSeatReserved", false);
        Log.d("HomeFragment", "hasReservedSeat: " + isSeatReserved);
        return false;
        //return sharedPreferences.getBoolean("isSeatReserved", false); //동적인 true, false값 필요 DB에서 불러오는 방법, 지금은 1회용 방법임
    }

    private void showAlreadyReservedAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("이미 좌석을 예약하셨습니다.")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
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