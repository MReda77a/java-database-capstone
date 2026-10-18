package com.project.back_end.services;

import com.project.back_end.models.Appointment;
import com.project.back_end.repositories.AppointmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    /** Books an appointment. Returns false when the doctor is already booked at that time. */
    @Transactional
    public boolean bookAppointment(Appointment appointment) {
        LocalDate day = appointment.getAppointmentTime().toLocalDate();
        List<Appointment> sameDay = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                appointment.getDoctor().getId(), day.atStartOfDay(), day.atTime(LocalTime.MAX));

        boolean taken = sameDay.stream()
                .anyMatch(a -> a.getAppointmentTime().equals(appointment.getAppointmentTime()));
        if (taken) {
            return false;
        }
        appointmentRepository.save(appointment);
        return true;
    }

    /** A doctor's appointments for one date. */
    public List<Appointment> getAppointmentsForDoctor(Long doctorId, LocalDate date) {
        return appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                doctorId, date.atStartOfDay(), date.atTime(LocalTime.MAX));
    }

    /** All appointments of a patient. */
    public List<Appointment> getAppointmentsForPatient(Long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }
}
