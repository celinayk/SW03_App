package com.example.sw03_app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommunityActivity extends AppCompatActivity {
    private ApiService apiService;
    private Button deleteButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        apiService = retrofit.create(ApiService.class);
//        deleteRequest(1);
        //일단은 이렇게 만듦
//        setupDeleteButton();
    }


    private void setupDeleteButton() {
        deleteButton = findViewById(R.id.delete_board);
        deleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(CommunityActivity.this, "게시물 삭제 시도", Toast.LENGTH_SHORT).show();

                //클라이언트 내부 권한 확인 로직

                //게시물 id 가져와서 요청 보내기
                deleteRequest(1);
            }
        });

    }

    public void deleteRequest(Integer boardIdToDelete) {
        // DELETE API 호출
        Call<Void> call = apiService.deleteBoard(boardIdToDelete);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // 성공적으로 삭제됨
//                  커뮤니티 페이지로 이동시키기
//                    Intent intent = new Intent(CommunityActivity.this, DestinationActivity.class);
//                    startActivity(intent);
                } else {
                    //클라이언트에서 서버로 요청을 보내는걸 실패했다? 그럼 어떡해요
                    Toast.makeText(CommunityActivity.this, "게시물 삭제에 실패하였습니다", Toast.LENGTH_SHORT).show();
                    // 요청 실패 처리

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }

        });
    }


}
