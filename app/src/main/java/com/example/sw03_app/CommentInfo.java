package com.example.sw03_app;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Date;


// 이 클래스 파일을 만든 이유는 디비에서 댓글정보를 가져와서 화면에 보여줄때 Commentadapter클래스를 거쳐서 댓글 adapter들을 보여주는데요
// adater에서 private ArrayList<Comment> localDataSet; 이렇게 댓글을 가져와서 보여줄때 댓글엔터티형식으로 받아와야하는데 지금 여기 앱에는
// 엔터티를 정의해놓은 클래스가 없잖아요 그래서 따로 정의를 해줄 필요가 있어서 정의를 해놨습니다
public class CommentInfo implements Parcelable {

    private Integer commentId;

    private String content;

    private java.util.Date date;

    private String writer;


    private Integer boardId; // FK 게시글 아이디 추가

    protected CommentInfo(Parcel in) {
        if (in.readByte() == 0) {
            commentId = null;
        } else {
            commentId = in.readInt();
        }
        content = in.readString();
        writer = in.readString();
        if (in.readByte() == 0) {
            boardId = null;
        } else {
            boardId = in.readInt();
        }
    }

    public static final Creator<CommentInfo> CREATOR = new Creator<CommentInfo>() {
        @Override
        public CommentInfo createFromParcel(Parcel in) {
            return new CommentInfo(in);
        }

        @Override
        public CommentInfo[] newArray(int size) {
            return new CommentInfo[size];
        }
    };

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public java.util.Date getDate() {
        return date;
    }

    public void setDate(java.util.Date date) {
        this.date = date;
    }

    public Integer getBoardId() {
        return boardId;
    }

    public void setBoardId(Integer boardId) {
        this.boardId = boardId;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public CommentInfo(Integer commentId, String content, java.util.Date date, Integer boardId, String writer) {
        this.commentId = commentId;
        this.content = content;
        this.date = date;
        this.boardId = boardId;
        this.writer = writer;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        if (commentId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(commentId);
        }
        dest.writeString(content);
        dest.writeString(writer);
        if (boardId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(boardId);
        }
    }
}