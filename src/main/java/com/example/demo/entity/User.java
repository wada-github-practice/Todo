package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "usertable")
@Getter
@Setter
public class User {

    /** ID */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    /** 名 */
    @Column(name = "username")
    private String username;
    /** password */
    @Column(name = "pass")
    private String pass;

    /** roll */
    @Column(name = "roll")
    private Integer roll;

}
