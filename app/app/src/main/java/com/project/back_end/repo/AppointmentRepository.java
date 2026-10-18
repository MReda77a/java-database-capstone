package com.project.back_end.repo;

import com.project.back_end.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Appointments of one doctor inside a time window (e.g. start and end of a day)
    List<Appointment> findByDoctorIdAndAppointmentTimeBetween(Long doctorId,
                                                              LocalDateTime start,
                                                              LocalDateTime end);

    // All appointments of one doctor, soonest first
    List<Appointment> findByDoctorIdOrderByAppointmentTimeAsc(Long doctorId);

    List<Appointment> findByPatientId(Long patientId);
}
