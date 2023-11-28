package com.example.sw03_app;

import java.sql.Date;

// 이 클래스 파일을 만든 이유는 디비에서 게시글정보를 가져와서 화면에 보여줄때 Postadapter클래스를 거쳐서 커뮤니티 adapter들을 보여주는데요
// adater에서 private ArrayList<Board> localDataSet; 이렇게 게시글을 가져와서 보여줄때 게시글엔터티형식으로 받아와야하는데 지금 여기 앱에는
// 엔터티를 정의해놓은 클래스가 없잖아요 그래서 따로 정의를 해줄 필요가 있어서 정의를 해놨습니다
public class PostInfo {

    private Integer boardId;

    private String title;
    private Integer category;

    private java.util.Date date;  // java.util.Date를 사용

    private String content;

    public PostInfo(Integer boardId, String title, Integer category, java.util.Date date, String content) {
        this.boardId = boardId;
        this.title = title;
        this.category = category;
        this.date = date;
        this.content = content;
    }

    public Integer getBoardId() {
        return boardId;
    }

    public void setBoardId(Integer boardId) {
        this.boardId = boardId;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public java.util.Date getDate() {
        return date;
    }

    public void setDate(java.util.Date date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }



}