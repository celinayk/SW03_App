package com.example.sw03_app;

import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Comment;

import java.util.ArrayList;

// 서버 comment(디비)데이터랑  view_post.xml 연결 ?
// RecyclerView에 데이터를 제공하고 표시하기 위한 뷰를 생성하는 파일
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.commentViewHolder> {

    private ArrayList<CommentInfo> localDataSet;

    // 생성자 통해 데이터를 전달받고 이걸 localDataSet에 저장한다
    public CommentAdapter (ArrayList<CommentInfo> dataSet) {
        this.localDataSet = dataSet;
    }

    // viewholer 생성 밑 뷰 연결
    // 뷰의 콘텐츠를 채우지는 않는다, xml파일을 inclate하여 새로윤 뷰 생성
    // 생성된 뷰 담고 잇는 뷰 홀더 객체 생성후 반환
    @NonNull
    @Override
    public commentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_adapter, parent, false);
        commentViewHolder commentViewHolder = new commentViewHolder(view);

        return commentViewHolder;
    }

    // 데이터 바인딩
    // position을 통해 햔재 아이템 위치 가져와 해당 위치 데이터를 얻고,
    // 얻은 데이터를 뷰 홀더의 텍스트뷰에 저장
    // 뷰 홀더와 데이터를 연결
    // 뷰 홀더가 재활용될 때 사용되는 메소드
    @Override
    public void onBindViewHolder(@NonNull commentViewHolder holder, int position) {

        CommentInfo comment = localDataSet.get(position);
        holder.commentId.setText(comment.getCommentId());
        holder.content.setText(comment.getContent());

        // Date를 문자열로 변환하여 TextView에 설정
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = dateFormat.format(comment.getDate());
        holder.date.setText(dateString);

    }

    // 전체 데이터의 갯수 리턴
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    // 뷰홀더 클래스, 뷰홀더에 필요한 데이터들
    public static class commentViewHolder extends RecyclerView.ViewHolder {

        // 뷰 홀더에 필요한 데이터들
        private TextView commentId;
        private TextView content;
        private TextView date;

        public commentViewHolder(@NonNull View itemView) {
            super(itemView);
            this.commentId = itemView.findViewById(R.id.commentWriterView);
            this.content = itemView.findViewById(R.id.commentView);
            this.date = itemView.findViewById(R.id.commentDateView);
        }

    }
}