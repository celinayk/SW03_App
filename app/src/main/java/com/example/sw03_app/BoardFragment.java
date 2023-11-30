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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BoardFragment extends Fragment {

    private RecyclerView community_recyclerView;
    private PostAdapter postAdapter;
    private ArrayList<PostInfo> items = new ArrayList<>();

    // PostDetailActivity에게 현재 인스턴스를 전달하는 메서드
    public static BoardFragment newInstance() {
        return new BoardFragment();
    }

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
        community_recyclerView.setAdapter(postAdapter);


        // 임의의 데이터 추가 (테스트 목적)
        items.add(new PostInfo(1, "안녕1", 1, new Date(System.currentTimeMillis()), "내용1"));
        items.add(new PostInfo(2, "제목2", 2, new Date(System.currentTimeMillis()), "내용2"));
        items.add(new PostInfo(3, "제목3", 1, new Date(System.currentTimeMillis()), "내용3"));
        items.add(new PostInfo(4, "정윤4", 1, new Date(System.currentTimeMillis()), "내용4"));
        items.add(new PostInfo(5, "제목5", 2, new Date(System.currentTimeMillis()), "내용5"));
        items.add(new PostInfo(6, "제목6", 1, new Date(System.currentTimeMillis()), "내용6"));
        items.add(new PostInfo(7, "제목7", 1, new Date(System.currentTimeMillis()), "내용7"));
        items.add(new PostInfo(8, "제목8", 2, new Date(System.currentTimeMillis()), "내용8"));
        items.add(new PostInfo(9, "제목9", 1, new Date(System.currentTimeMillis()), "내용9"));

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
