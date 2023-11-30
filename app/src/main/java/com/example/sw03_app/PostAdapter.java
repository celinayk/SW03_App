package com.example.sw03_app;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

// RecyclerView에 데이터를 제공하고 표시하기 위한 뷰를 생성하는 파일
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.postViewHolder> {
    private ArrayList<PostInfo> localDataSet;
    private AdapterView.OnItemClickListener itemClickListener;

    // 생성자 통해 데이터를 전달받고 이걸 localDataSet에 저장한다
    public PostAdapter(ArrayList<PostInfo> dataSet, BoardFragment boardFragment) {
        this.localDataSet = dataSet;
    }

    // viewholer 생성 밑 뷰 연결
    // 뷰의 콘텐츠를 채우지는 않는다, xml파일을 inclate하여 새로윤 뷰 생성
    // 생성된 뷰 담고 잇는 뷰 홀더 객체 생성후 반환
    @NonNull
    @Override
    public postViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.community_adapter, parent, false);
        postViewHolder postViewHolder = new postViewHolder(view);

        return postViewHolder;
    }

    // 데이터 바인딩
    // position을 통해 햔재 아이템 위치 가져와 해당 위치 데이터를 얻고,
    // 얻은 데이터를 뷰 홀더의 텍스트뷰에 저장
    // 뷰 홀더와 데이터를 연결
    // 뷰 홀더가 재활용될 때 사용되는 메소드
    @Override
    public void onBindViewHolder(@NonNull postViewHolder holder, int position) {
        PostInfo post = localDataSet.get(position);

        // 이미지 설정 (이미지가 있다면 해당 이미지로 설정)
        // holder.imageView.setImageResource(post.getImageResource());

        holder.title.setText(post.getTitle());
        holder.postTime.setText(post.getDate().toString());  // 날짜를 문자열로 변환하여 설정
        holder.postWriter.setText(post.getBoardId().toString());  // 작성자 ID로 설정 (나중에 사용자 이름으로 변경하면 좋을 것 같습니다.)
        holder.textContentView.setText(post.getContent());
/*
        holder.itemClickListener = (v, position1) -> {
            Integer boardId = post.getBoardId();
            Intent intent = new Intent(v.getContext(), PostDetailActivity.class);
            intent.putExtra("boardId", boardId);
            v.getContext().startActivity(intent);
        };
*/
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),PostDetailActivity.class);
                intent.putExtra("boardId", post.getBoardId());
                intent.putParcelableArrayListExtra("items", new ArrayList<>(localDataSet));  // 전체 데이터를 전달

                v.getContext().startActivity(intent);
            }
        });

    }

    // BoardFragment에서 PostDetailActivity로 이동할 때 데이터 전달
    public void OnItemClickListener(View v, int position) {
        Integer boardId = localDataSet.get(position).getBoardId();

        // Intent를 생성하고 데이터를 put
        Intent intent = new Intent(v.getContext(), PostDetailActivity.class);
        intent.putExtra("boardId", boardId);
       // intent.putParcelableArrayListExtra("items", new ArrayList<PostInfo>(items));  // 전체 데이터를 전달

        // PostDetailActivity 시작
        v.getContext().startActivity(intent);
    }


    // 전체 데이터의 갯수 리턴
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }


    // 특정 게시글에 대한 댓글 필터링 메서드
  /*private ArrayList<CommentInfo> filterCommentsByPost(int boardId) {
        ArrayList<CommentInfo> filteredComments = new ArrayList<>();

        filteredComments.add(new CommentInfo(1, "댓글1", new Date(System.currentTimeMillis()), 1));
        filteredComments.add(new CommentInfo(2, "댓글2", new Date(System.currentTimeMillis()), 1));
        filteredComments.add(new CommentInfo(3, "댓글3", new Date(System.currentTimeMillis()), 2));

        return filteredComments;
    }
*/

        // 뷰홀더 클래스, 뷰홀더에 필요한 데이터들
        public static class postViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            OnItemClickListener itemClickListener;
            //private ImageView imageView;
            private TextView title;
            private TextView postTime;
            private TextView postWriter;
            private TextView textContentView;
           // private RecyclerView commentrecyclerView; //게시글밑에 있는 댓글리사이클러뷰

            public postViewHolder(@NonNull View itemView) {
                super(itemView);
                //this.imageView = itemView.findViewById(R.id.imageView);
                this.title = itemView.findViewById(R.id.titleTextView);
                this.postTime = itemView.findViewById(R.id.post_time);
                this.postWriter = itemView.findViewById(R.id.post_writer);
                this.textContentView = itemView.findViewById(R.id.textcontentView);
                //this.commentrecyclerView = itemView.findViewById(R.id.comment_recyclerView);

                // itemView에 클릭 리스너 설정
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                this.itemClickListener.onItemClickListener(v,getLayoutPosition());
            }
        }
    }



