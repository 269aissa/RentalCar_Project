package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    // Inscription
    public User registerUser(String username, String password, User.Role role) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Ce nom d'utilisateur est déjà pris");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        return userRepository.save(user);
    }

    // Trouver par id
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    // Trouver par username
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Lister tous les users (admin)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    // Modifier un user (ex : profil)
    public User updateUser(Long id, String username, String password) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        user.setUsername(username);
        if (password != null && !password.isBlank()) {
            user.setPassword(passwordEncoder.encode(password));
        }
        return userRepository.save(user);
    }

    // Supprimer un user
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
