package com.example.sw03_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import java.util.Date;

import androidx.fragment.app.Fragment;

import com.example.sw03_app.dto.Board;
import com.example.sw03_app.retrofit.RetroService;
import com.example.sw03_app.retrofit.RetrofitClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BoardFragment extends Fragment {

    private RecyclerView community_recyclerView;
    private PostAdapter postAdapter;

    // PostDetailActivity에게 현재 인스턴스를 전달하는 메서드
    public static BoardFragment newInstance() {
        return new BoardFragment();
    }

    ArrayList<Board> items = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.view_community, container, false);

        // 버튼 클릭 시 ViewSeat 액티비티로 이동
        FloatingActionButton postButton = view.findViewById(R.id.postButton);
        Button search = view.findViewById(R.id.search);
        Button menu = view.findViewById(R.id.menu);

        community_recyclerView = view.findViewById(R.id.community_recyclerView);
        community_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        postAdapter = new PostAdapter(items, this);

        Retrofit retrofit = RetrofitClient.getClient();
        RetroService inquiryRetrofit = retrofit.create(RetroService.class);
        Call<ArrayList<Board>> inquiry = inquiryRetrofit.getBoards();
        inquiry.enqueue(new Callback<ArrayList<Board>>() {
            @Override
            public void onResponse(Call<ArrayList<Board>> call, Response<ArrayList<Board>> response) {
                if(response.isSuccessful()) {
                    ArrayList<Board> listResponse = response.body();
                    items = listResponse;
                } else {
                    System.out.println("FAIL!@!@!");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Board>> call, Throwable t) {
                System.out.println("t = " + t);
            }
        });

        for (Board item : items) {
            System.out.println("item = " + item.getContent());
        }
        // postAdapter = new PostAdapter(items);
        community_recyclerView.setAdapter(postAdapter);
        postAdapter.notifyDataSetChanged();


        //글올리기 버튼 누를때(postButton)
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WritePost.class);
                startActivity(intent);
            }
        });

        //돋보기 버튼 누를때(search)
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //글검색하는 기능넣기
            }
        });

        //메뉴 버튼 누를때(menu) - 카테고리 별로 글 보여줄 예정
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //글 카테고리 별로 나눠서 보는 기능 넣기
            }
        });

        return view;
    }
}
