package com.project.back_end.controllers;

import com.project.back_end.dto.Login;
import com.project.back_end.models.Doctor;
import com.project.back_end.services.DoctorService;
import com.project.back_end.services.TokenService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/doctor")
public class DoctorController {

    private final DoctorService doctorService;
    private final TokenService tokenService;

    public DoctorController(DoctorService doctorService, TokenService tokenService) {
        this.doctorService = doctorService;
        this.tokenService = tokenService;
    }

    // GET /doctor  -> list of all doctors
    @GetMapping
    public ResponseEntity<List<Doctor>> getDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    // GET /doctor/search?name=hana  -> doctors whose name contains the text
    @GetMapping("/search")
    public ResponseEntity<List<Doctor>> searchDoctors(@RequestParam(required = false) String name) {
        return ResponseEntity.ok(doctorService.searchByName(name));
    }

    // POST /doctor/login  -> doctor login, returns a JWT
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Login login) {
        return doctorService.validateDoctor(login);
    }

    // POST /doctor/{token}  -> only an authenticated admin can add a doctor
    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> addDoctor(@PathVariable String token,
                                                         @Valid @RequestBody Doctor doctor) {
        Map<String, String> body = new HashMap<>();

        if (!tokenService.validateToken(token, "admin")) {
            body.put("message", "Invalid or expired token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
        }
        if (doctorService.saveDoctor(doctor) == -1) {
            body.put("message", "A doctor with this email already exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
        }
        body.put("message", "Doctor added successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    // GET /doctor/availability/{doctorId}?date=2026-11-02&token=...
    // The caller's token must belong to a patient (or doctor) before availability is revealed.
    @GetMapping("/availability/{doctorId}")
    public ResponseEntity<Map<String, Object>> getAvailability(
            @PathVariable Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam String token) {

        Map<String, Object> body = new HashMap<>();

        boolean allowed = tokenService.validateToken(token, "patient")
                || tokenService.validateToken(token, "doctor");
        if (!allowed) {
            body.put("message", "Invalid or expired token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
        }

        body.put("availability", doctorService.getDoctorAvailability(doctorId, date));
        return ResponseEntity.ok(body);
    }

    // GET /doctor/filter?specialty=Cardiology&time=AM
    @GetMapping("/filter")
    public ResponseEntity<List<Doctor>> filterDoctors(
            @RequestParam(required = false) String specialty,
            @RequestParam(required = false) String time) {
        return ResponseEntity.ok(doctorService.filterDoctors(specialty, time));
    }
}
