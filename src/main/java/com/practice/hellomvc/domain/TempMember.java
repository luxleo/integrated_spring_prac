package com.practice.hellomvc.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TempMember {
    private Long id;
    private Integer age;
    private String name;

    @Builder
    public TempMember(Long id, Integer age, String name) {
        this.id = id;
        this.age = age;
        this.name = name;
    }
}
