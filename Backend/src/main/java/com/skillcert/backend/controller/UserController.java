package com.skillcert.backend.controller;

import com.skillcert.backend.entity.User;
import com.skillcert.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;

@RestController
@RequestMapping("/api/students")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    public List<User> getStudentsByRole(@RequestParam String role) {
        return userRepository.findByRole(User.Role.valueOf(role));
    }
}
