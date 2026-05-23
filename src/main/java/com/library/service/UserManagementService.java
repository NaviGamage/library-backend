package com.library.service;

import com.library.model.Role;
import com.library.model.User;
import com.library.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserManagementService {

    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void blacklistUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        if (user.getRole() == Role.LIBRARIAN) {
            throw new RuntimeException("Cannot blacklist a Librarian!");
        }

        if (user.isBlacklisted()) {
            throw new RuntimeException("User is already blacklisted!");
        }

        user.setBlacklisted(true);
        userRepository.save(user);
    }

    public void unblacklistUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        if (!user.isBlacklisted()) {
            throw new RuntimeException("User is not blacklisted!");
        }

        user.setBlacklisted(false);
        userRepository.save(user);
    }
}