package com.example.sw03_app;

import static android.app.PendingIntent.getActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sw03_app.dto.Board;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;

public class PostDetailActivity extends AppCompatActivity {

    // BoardFragment에서 전달받은 데이터 리스트
    private ArrayList<Board> items = new ArrayList<>();

    private TextView titleTextView;
    private TextView contentTextView;
    private TextView writerTextView;
    private TextView dateTextView;

    // 게시글 상세조회 페이지에서 댓글 리사이클러뷰를 보여줘야 하니까
    private RecyclerView comment_recyclerView;

    // 댓글 보여줄거니까?
    private CommentAdapter commentAdapter;

    // 댓글 엔터티 형식으로 댓글 가져와야하니까 ?
    //ArrayList<CommentInfo> items = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_post);

        FloatingActionButton redoButton = findViewById(R.id.redoButton);

        // UI 요소 초기화
        titleTextView = findViewById(R.id.title);
        contentTextView = findViewById(R.id.text);
        writerTextView = findViewById(R.id.user_name);
        dateTextView = findViewById(R.id.view_post_time);

        // BoardFragment의 인스턴스를 얻어옴
        BoardFragment boardFragment = BoardFragment.newInstance();

        // intent에서 게시글 id값과 데이터 리스트를 받아온다
        Intent intent = getIntent();
        if (intent != null) {
            Integer boardId = intent.getIntExtra("boardId", -1); // 기본값은 -1

            // items 멤버 변수에 전달받은 목록 설정
            items = intent.getParcelableArrayListExtra("items");
            //ArrayList<PostInfo> items = (ArrayList<PostInfo>) intent.getParcelableArrayListExtra("items");

            if (boardId!= -1 && items != null) {
                // 게시글 id를 사용하여 게시글 정보를 조회
                Board post = getPostsByBoardId(boardId, items);

                if(post != null) {
                    System.out.println("post = " + post.getContent());
                    titleTextView.setText(post.getTitle());
                    contentTextView.setText(post.getContent());
                    writerTextView.setText("작성자: " + post.getBoardId().toString());
                    //dateTextView.setText("작성일: " + post.getDate().toString());

                }


                // 상세 게시글에서 뒤로가기 버튼을 누르면 다시 게시글 목록으로 돌아간다
                redoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        // MainActivity로 이동할 때 선택된 아이템을 전달
                        intent.putExtra("selectedItemId", R.id.board);
                        startActivity(intent);
                        finish();

                    }
                }

                );
            }

                }
            }




    private Board getPostsByBoardId(Integer boardId,ArrayList<Board> items ) {
        for (Board post : items) {
            System.out.println("외부 : boardId = " + boardId + "getBoardId = " + post.getBoardId());
            if (post.getBoardId().toString().equals(boardId.toString())) {
                System.out.println("내부 : boardId = " + boardId + "getBoardId = " + post.getBoardId());
                return post;
            }
        }
        return null; // 해당 ID에 맞는 게시글이 없을 경우

    }

}
