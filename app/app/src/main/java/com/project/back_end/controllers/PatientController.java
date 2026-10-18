package com.project.back_end.controllers;

import com.project.back_end.dto.Login;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.PatientRepository;
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
@RequestMapping("/patient")
public class PatientController {

    private final PatientRepository patientRepository;
    private final TokenService tokenService;

    public PatientController(PatientRepository patientRepository, TokenService tokenService) {
        this.patientRepository = patientRepository;
        this.tokenService = tokenService;
    }

    // POST /patient/login  {"identifier": "ahmed@test.com", "password": "patient123"}
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Login login) {
        Map<String, String> body = new HashMap<>();
        Patient patient = patientRepository.findByEmail(login.getIdentifier());

        if (patient == null || !patient.getPassword().equals(login.getPassword())) {
            body.put("message", "Invalid email or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
        }
        body.put("token", tokenService.generateToken(patient.getEmail()));
        return ResponseEntity.ok(body);
    }
}
