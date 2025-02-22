package com.project.marathon.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Data
public class Message {
    private Long id;
    private String message;

    public Message(String message) {
        this.message = message;
    }
}