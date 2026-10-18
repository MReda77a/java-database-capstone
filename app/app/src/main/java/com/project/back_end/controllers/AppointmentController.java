package com.project.back_end.controllers;

import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.services.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final TokenService tokenService;

    public AppointmentController(AppointmentRepository appointmentRepository,
                                 DoctorRepository doctorRepository,
                                 TokenService tokenService) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.tokenService = tokenService;
    }

    // GET /appointment/doctor/{token}  -> every appointment of the logged-in doctor
    @GetMapping("/doctor/{token}")
    public ResponseEntity<?> getDoctorAppointments(@PathVariable String token) {
        if (!tokenService.validateToken(token, "doctor")) {
            Map<String, String> body = new LinkedHashMap<>();
            body.put("message", "Invalid or expired token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
        }

        Doctor doctor = doctorRepository.findByEmail(tokenService.extractIdentifier(token));
        List<Map<String, Object>> result = new ArrayList<>();

        for (Appointment a : appointmentRepository.findByDoctorIdOrderByAppointmentTimeAsc(doctor.getId())) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("id", a.getId());
            row.put("patientName", a.getPatient().getName());
            row.put("patientPhone", a.getPatient().getPhone());
            row.put("appointmentTime", a.getAppointmentTime().toString());
            row.put("status", a.getStatus() == 0 ? "Scheduled" : "Completed");
            result.add(row);
        }
        return ResponseEntity.ok(result);
    }
}
