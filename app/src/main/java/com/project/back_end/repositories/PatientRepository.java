package com.project.back_end.repositories;

import com.project.back_end.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    // Find a patient by exact email
    Patient findByEmail(String email);

    // Find a patient by email OR phone (used to detect duplicates at sign-up)
    Patient findByEmailOrPhone(String email, String phone);
}
