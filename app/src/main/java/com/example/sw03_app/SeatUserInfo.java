package com.example.sw03_app;

import com.kakao.sdk.user.model.User;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class SeatUserInfo {

    private Long seatId;
    private boolean able;

    private List<User> userList;

    public boolean isAble() {
        return able;
    }



    public SeatUserInfo(Long seatId, boolean able, List<User> userList) {
        this.seatId = seatId;
        this.able = able;
        this.userList = userList;
    }
}
