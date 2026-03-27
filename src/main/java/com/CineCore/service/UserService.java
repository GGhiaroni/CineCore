package com.CineCore.service;

import com.CineCore.entity.User;
import com.CineCore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User saveUser(User user){
        return userRepository.save(user);
    }
}
