package com.practice.hellomvc.connection.propagation;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Entity
@Getter
@NoArgsConstructor
@ToString(of = {"username"})
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;

    @Builder
    public Member( String username) {
        this.username = username;
    }
}
