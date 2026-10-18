package com.project.back_end.controllers;

import com.project.back_end.dto.Login;
import com.project.back_end.models.Admin;
import com.project.back_end.repo.AdminRepository;
import com.project.back_end.services.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminRepository adminRepository;
    private final TokenService tokenService;

    public AdminController(AdminRepository adminRepository, TokenService tokenService) {
        this.adminRepository = adminRepository;
        this.tokenService = tokenService;
    }

    // POST /admin/login  {"identifier": "admin", "password": "admin123"}
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Login login) {
        Map<String, String> body = new HashMap<>();
        Admin admin = adminRepository.findByUsername(login.getIdentifier());

        if (admin == null || !admin.getPassword().equals(login.getPassword())) {
            body.put("message", "Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
        }
        body.put("token", tokenService.generateToken(admin.getUsername()));
        return ResponseEntity.ok(body);
    }
}
