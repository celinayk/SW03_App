package com.example.sw03_app.dto;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Comment implements Parcelable {

    @SerializedName("commentId")
    private Integer commentId;

    @SerializedName("content")
    private String content;

    @SerializedName("date")
    private String date;

    @SerializedName("writer")
    private String writer;

    @SerializedName("boardId")
    private Integer boardId;

    protected Comment(Parcel in) {
        if (in.readByte() == 0) {
            commentId = null;
        } else {
            commentId = in.readInt();
        }
        content = in.readString();
        date = in.readString();
        writer = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (commentId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(commentId);
        }
        dest.writeString(content);
        dest.writeString(date);
        dest.writeString(writer);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    @NonNull
    @Override
    public String toString() {
        return "comment { commentId : " + commentId + ", content : " + content + ", date : " + date + ", writer : " + writer;
    }

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public Integer getBoardId() {
        return boardId;
    }

    public void setBoardId(Integer boardId) {
        this.boardId = boardId;
    }
}
