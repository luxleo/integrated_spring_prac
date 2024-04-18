package com.practice.hellomvc.connection.propagation;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Entity
@Getter @NoArgsConstructor
@ToString(of = {"message"})
public class Log {
    @Id @GeneratedValue
    private Long id;
    private String message;

    @Builder
    public Log(String message) {
        this.message = message;
    }
}
