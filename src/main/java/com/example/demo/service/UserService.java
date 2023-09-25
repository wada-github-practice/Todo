package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    public User getByUsername(String username){
        return userRepository.findByUsername(username);
    }
    public List<User> getByUserList(){
        return userRepository.findAll();
    }
}
