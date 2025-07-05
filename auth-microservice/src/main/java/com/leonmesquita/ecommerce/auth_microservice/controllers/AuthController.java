package com.leonmesquita.ecommerce.auth_microservice.controllers;

import com.leonmesquita.ecommerce.auth_microservice.dtos.LoginDTO;
import com.leonmesquita.ecommerce.auth_microservice.dtos.TokenResponseDTO;
import com.leonmesquita.ecommerce.auth_microservice.dtos.UserDTO;
import com.leonmesquita.ecommerce.auth_microservice.models.UserModel;
import com.leonmesquita.ecommerce.auth_microservice.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserModel> registerUser(@RequestBody @Valid UserDTO body) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(body));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody LoginDTO loginDTO) {
        return ResponseEntity.ok(authService.login(loginDTO));
    }

    @GetMapping("/user")
    public ResponseEntity<UserModel> getUserByEmail(@RequestParam  String email) {
        return ResponseEntity.ok(authService.findByEmail(email));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserModel> getUserById(@PathVariable  Long id) {
        return ResponseEntity.ok(authService.findById(id));
    }
}
