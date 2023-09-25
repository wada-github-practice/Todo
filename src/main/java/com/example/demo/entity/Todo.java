package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "todotable")
@Getter
@Setter
public class Todo {

    /** ID */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    /** タイトル */
    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;
    
    @Column(name = "category")
    private String category;
    
    @Column(name = "deadline")
    private String deadline;

    /** 作成日時 */
    @Column(name = "createtime")
    private LocalDateTime createtime;
    
    /** 更新日時 */
    @Column(name = "updatetime")
    private LocalDateTime updatetime;
    
    @Column(name = "username")
    private String username;
    
    @Column(name = "state")
    private String state;

}

