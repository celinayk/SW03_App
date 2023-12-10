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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sw03_app.dto.Board;
import com.example.sw03_app.dto.Comment;
import com.example.sw03_app.retrofit.RetroService;
import com.example.sw03_app.retrofit.RetrofitClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PostDetailActivity extends AppCompatActivity {

    // BoardFragment에서 전달받은 데이터 리스트
    private ArrayList<Board> items = new ArrayList<>();

    private TextView titleTextView;
    private TextView contentTextView;
    private TextView writerTextView;
    private TextView dateTextView;
    private TextView commentTitleView;


    // 댓글 관련
    private RecyclerView comment_recyclerView;
    private CommentAdapter commentAdapter;
    private ArrayList<Comment> citems = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_post);

        FloatingActionButton redoButton = findViewById(R.id.redoButton);
        Button doneButton = findViewById(R.id.comment_done_button);

        // UI 요소 초기화
        titleTextView = findViewById(R.id.title);
        contentTextView = findViewById(R.id.text);
        writerTextView = findViewById(R.id.user_name);
        dateTextView = findViewById(R.id.view_post_time);
        commentTitleView = findViewById(R.id.comment);

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
                System.out.println("게시글아이디 확인 테스트용 ");
                System.out.println(post.getBoardId());

                if(post != null) {
                    System.out.println("post = " + post.getContent());
                    titleTextView.setText(post.getTitle());
                    contentTextView.setText(post.getContent());
                    writerTextView.setText("작성자: " + post.getBoardId().toString());
                    //dateTextView.setText("작성일: " + post.getDate().toString());



                   // System.out.println("테스트 boardId: " + boardId);
                    //System.out.println("테스트 citems: " + citems);

                    // 현재 상세 게시글의 boardId값을 가지고옴
                    //int postBoardId = post.getBoardId();


                    // 댓글 리사이클러뷰 초기화
                    comment_recyclerView = findViewById(R.id.comment_recyclerView);
                    comment_recyclerView.setLayoutManager(new LinearLayoutManager(this));

                    // 댓글 어댑터 초기화
                    commentAdapter = new CommentAdapter(citems);
                    comment_recyclerView.setAdapter(commentAdapter);

                }

                //View view = inflater.inflate(R.layout.comment_recyclerView, )
                // 댓글 리사이클러뷰 게시글 상세정보 밑에 보여준다
                //setContentView(comment_recyclerView);



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
                });


                // 댓글 완료 버튼을 누르면 이 페이지를 다시 보여준다
                doneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // titleEditText(댓글작성칸 id)에 쓴 댓글이 디비에 올라가고
                        // 페이지는 여전히 이 페이지를 보여준다

                    }

                });


            }

                }
            }


    private Board getPostsByBoardId(Integer boardId,ArrayList<Board> items ) {
        for (Board post : items) {
            System.out.println("외부 : boardId = " + boardId + "getBoardId = " + post.getBoardId());
            if (post.getBoardId().toString().equals(boardId.toString())) {
                getComments(boardId);
                System.out.println("내부 : boardId = " + boardId + "getBoardId = " + post.getBoardId());
                return post;
            }
        }
        return null; // 해당 ID에 맞는 게시글이 없을 경우

    }
/*
    // 게시글 id에 해당하는 댓글들만 반환하는 메서드 추가
    private ArrayList<CommentInfo> getCommentByBoardId(int boardId, ArrayList<CommentInfo> allComments) {
        ArrayList<CommentInfo> comments = new ArrayList<>();
        for(CommentInfo comment : allComments) {
            System.out.println("Comment: " + comment.getBoardId());
            if(boardId == comment.getBoardId()) {
                comments.add(comment);
            }
        }
        return comments;
    }
*/

    public void getComments(Integer board_id) {

        System.out.println("board_id 댓글 요청 = " + board_id);
        Retrofit retrofit = RetrofitClient.getClient();
        RetroService inquiryRetrofit = retrofit.create(RetroService.class);
        Call<ArrayList<Comment>> inquiry = inquiryRetrofit.getComments(board_id);
        inquiry.enqueue(new Callback<ArrayList<Comment>>() {
            @Override
            public void onResponse(Call<ArrayList<Comment>> call, Response<ArrayList<Comment>> response) {
                if(response.isSuccessful()) {
                    ArrayList<Comment> commentsArrayList = response.body();
                    citems = commentsArrayList;

                    for (Comment comment : commentsArrayList) {
                        System.out.println(comment);
                    }
                } else {
                    System.out.println("FAIL!@!@!");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Comment>> call, Throwable t) {
                System.out.println("t = " + t);
            }
        });
    }


}
