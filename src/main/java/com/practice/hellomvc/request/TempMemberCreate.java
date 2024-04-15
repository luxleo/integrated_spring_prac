package com.practice.hellomvc.request;

import com.practice.hellomvc.domain.TempMember;
import lombok.Data;

@Data
public class TempMemberCreate {
    private String name;
    private Integer age;

    public TempMember toMember() {
        return null;
    }
}
