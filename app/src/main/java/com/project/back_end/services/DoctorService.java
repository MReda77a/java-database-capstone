package com.project.back_end.services;

import com.project.back_end.dto.Login;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;

    public DoctorService(DoctorRepository doctorRepository,
                         AppointmentRepository appointmentRepository,
                         TokenService tokenService) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    /** Saves a new doctor. Returns 1 on success, -1 when the email is already used. */
    public int saveDoctor(Doctor doctor) {
        if (doctorRepository.findByEmail(doctor.getEmail()) != null) {
            return -1;
        }
        doctorRepository.save(doctor);
        return 1;
    }

    /** Case-insensitive partial name search; a blank name returns every doctor. */
    public List<Doctor> searchByName(String name) {
        if (name == null || name.isBlank()) {
            return doctorRepository.findAll();
        }
        return doctorRepository.findByNameContainingIgnoreCase(name.trim());
    }

    /**
     * Available slots for a doctor on a date: the doctor's configured slots
     * (e.g. "09:00-10:00") minus the slots already booked that day.
     */
    public List<String> getDoctorAvailability(Long doctorId, LocalDate date) {
        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
        if (doctor == null || doctor.getAvailableTimes() == null) {
            return new ArrayList<>();
        }

        List<Appointment> booked = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                doctorId, date.atStartOfDay(), date.atTime(LocalTime.MAX));

        // Start hour of every booked appointment, formatted like the slot start ("09:00")
        List<String> bookedStarts = booked.stream()
                .map(a -> a.getAppointmentTime().toLocalTime().toString().substring(0, 5))
                .collect(Collectors.toList());

        return doctor.getAvailableTimes().stream()
                .filter(slot -> !bookedStarts.contains(slot.split("-")[0].trim()))
                .collect(Collectors.toList());
    }

    /** Validates a doctor's login and returns a JWT on success. */
    public ResponseEntity<Map<String, String>> validateDoctor(Login login) {
        Map<String, String> response = new HashMap<>();
        Doctor doctor = doctorRepository.findByEmail(login.getIdentifier());

        if (doctor == null || !doctor.getPassword().equals(login.getPassword())) {
            response.put("message", "Invalid email or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        response.put("token", tokenService.generateToken(doctor.getEmail()));
        return ResponseEntity.ok(response);
    }

    /** Filters doctors by specialty and by part of day ("AM" or "PM"); either filter may be null. */
    public List<Doctor> filterDoctors(String specialty, String period) {
        List<Doctor> doctors = (specialty == null || specialty.isBlank())
                ? doctorRepository.findAll()
                : doctorRepository.findBySpecialtyIgnoreCase(specialty);

        if (period == null || period.isBlank()) {
            return doctors;
        }
        boolean wantAm = period.equalsIgnoreCase("AM");

        return doctors.stream()
                .filter(d -> d.getAvailableTimes() != null && d.getAvailableTimes().stream().anyMatch(slot -> {
                    int startHour = Integer.parseInt(slot.split("-")[0].trim().split(":")[0]);
                    return wantAm ? startHour < 12 : startHour >= 12;
                }))
                .collect(Collectors.toList());
    }
}
