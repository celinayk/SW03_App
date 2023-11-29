package com.example.sw03_app;

import com.kakao.sdk.user.model.User;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class SeatUserInfo {

    private Long seatId;
    private boolean able;

    private LocalDateTime startTime;

    private List<User> userList;

    public Long getSeatId() {
        return seatId;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }

    public boolean isAble() {
        return able;
    }

    public void setAble(boolean able) {
        this.able = able;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public SeatUserInfo(Long seatId, boolean able, LocalDateTime startTime, List<User> userList) {
        this.seatId = seatId;
        this.able = able;
        this.startTime = startTime;
        this.userList = userList;
    }
}
