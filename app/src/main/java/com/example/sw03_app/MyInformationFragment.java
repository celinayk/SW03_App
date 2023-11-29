package com.example.sw03_app;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.sw03_app.client.Client;
import com.example.sw03_app.retrofit.RetroService;
import com.example.sw03_app.retrofit.RetrofitClient;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MyInformationFragment extends Fragment {

    private View loginButton, logoutButton;

    public View getLoginButton(){
        return loginButton;
    }

    public View getLogoutButton(){
        return logoutButton;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.my_information, container, false);

        loginButton = view.findViewById(R.id.login);
        logoutButton = view.findViewById(R.id.logout);

        if (loginButton == null || logoutButton == null) {
            loginButton = view.findViewById(R.id.login);
            logoutButton = view.findViewById(R.id.logout);
        }

        Function2<OAuthToken, Throwable, Unit> callback = new Function2<OAuthToken, Throwable, Unit>() {
            @Override
            public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                if (oAuthToken != null) {
                    System.out.println("oAuthToken : " + oAuthToken.getAccessToken());
                }
                if (throwable != null) {
                    // TBO
                }
                // 카카오 로그인 상태에 따라 UI 업데이트
                updateKakaoLoginStatus(UserApiClient.getInstance().isKakaoTalkLoginAvailable(requireContext()));
                return null;
            }
        };

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(requireContext())) {
                    UserApiClient.getInstance().loginWithKakaoTalk(requireActivity(), callback);
                } else {
                    UserApiClient.getInstance().loginWithKakaoAccount(requireActivity(), callback);
                }
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserApiClient.getInstance().logout(new Function1<Throwable, Unit>() {
                    @Override
                    public Unit invoke(Throwable throwable) {
                        // 로그아웃 후 UI 업데이트
                        updateKakaoLoginStatus(false);
                        return null;
                    }
                });
            }
        });

        kakaoLogin();

        return view;
    }

    private void updateKakaoLoginStatus(boolean isLoggedIn) {
        if (loginButton != null && logoutButton != null) {
            if (isLoggedIn) {
                loginButton.setVisibility(View.GONE);
                logoutButton.setVisibility(View.VISIBLE);
            } else {
                loginButton.setVisibility(View.VISIBLE);
                logoutButton.setVisibility(View.GONE);
            }
        }
    }

    private void kakaoLogin() {
        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
            @Override
            public Unit invoke(User user, Throwable throwable) {
                if (user != null) {

                    Client.setSns_id(user.getId()); // sns_id 저장

                    /* 서버에 유저 등록 요청 */
                    Retrofit retrofit = RetrofitClient.getClient();
                    RetroService inquiryRetrofit = retrofit.create(RetroService.class);
                    Call<String> inquiry = inquiryRetrofit.addUser(user.getId(), user.getKakaoAccount().getProfile().getNickname());
                    System.out.println("zz = " + user.getKakaoAccount().getProfile().getNickname());
                    inquiry.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if(response.isSuccessful()) {
                                String body = response.body();
                                System.out.println("UserPost Success");
                            } else {
                                System.out.println("UserPost fail");
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            System.out.println("t = " + t.getMessage());
                        }
                    });


                    Log.i(TAG, "invoke : id = " + user.getId());
                    Log.i(TAG, "invoke : id = " + user.getKakaoAccount().getProfile());

                    loginButton.setVisibility(View.GONE);
                    logoutButton.setVisibility(View.VISIBLE);
                } else {
                    loginButton.setVisibility(View.VISIBLE);
                    logoutButton.setVisibility(View.GONE);
                }
                return Unit.INSTANCE;
            }
        });
    }

}
