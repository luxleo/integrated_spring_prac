package com.practice.hellomvc.connection.transaction;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Member {
    private String memberId;
    private Integer money;

    public Member(String memberId, Integer money) {
        this.memberId = memberId;
        this.money = money;
    }
}
