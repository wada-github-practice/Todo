package com.example.demo.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class editTodoDto {
    /** タイトル */
    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;
    
    @Column(name = "category")
    private String category;
    
    @Column(name = "deadline")
    private String deadline;
    
    @Column(name = "username")
    private String username;
    
    @Column(name = "state")
    private String state;
}
