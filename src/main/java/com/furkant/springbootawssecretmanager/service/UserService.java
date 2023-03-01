package com.furkant.springbootawssecretmanager.service;

import com.furkant.springbootawssecretmanager.entity.User;
import com.furkant.springbootawssecretmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Value("${example.secret.message}")
    private String secretValue;
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getSecretValue() {
        System.out.println("Your secret value: " + secretValue);
        return secretValue;
    }


    public List<User> getUsers() {
        return userRepository.findAll();
    }
}
