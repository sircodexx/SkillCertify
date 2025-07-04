package com.skillcert.backend.service;

import com.skillcert.backend.dto.AuthResponse;
import com.skillcert.backend.dto.LoginRequest;
import com.skillcert.backend.dto.RegisterRequest;
import com.skillcert.backend.entity.User;
import com.skillcert.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El correo ya está en uso");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setCompany(request.getCompany());
        user.setRole(User.Role.valueOf(request.getRole() != null ? request.getRole().toUpperCase() : "NORMAL"));
        user.setStatus(User.Status.ACTIVE);

        userRepository.save(user);

        String token = jwtService.generateToken(user);
        return new AuthResponse(token, null, "Bearer", user.getName(), user.getEmail(), user.getRole().name());
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        String token = jwtService.generateToken(user);
        return new AuthResponse(token, null, "Bearer", user.getName(), user.getEmail(), user.getRole().name());
    }
}
