package com.project.back_end.config;

import com.project.back_end.models.Admin;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Inserts demo data on first start (only when the tables are empty) so the portals
 * can be tried immediately.
 *
 * Demo logins:
 *   admin   -> admin / admin123
 *   doctor  -> hana@test.com / doctor123
 *   patient -> ahmed@test.com / patient123
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;

    public DataSeeder(AdminRepository adminRepository,
                      DoctorRepository doctorRepository,
                      PatientRepository patientRepository,
                      AppointmentRepository appointmentRepository) {
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public void run(String... args) {
        if (adminRepository.count() == 0) {
            Admin admin = new Admin();
            admin.setUsername("admin");
            admin.setPassword("admin123");
            adminRepository.save(admin);
        }

        if (doctorRepository.count() == 0) {
            doctorRepository.save(doctor("Dr. Hana Mahmoud", "Cardiology", "hana@test.com", "0111111111",
                    List.of("09:00-10:00", "10:00-11:00", "14:00-15:00")));
            doctorRepository.save(doctor("Dr. Karim Nasser", "Neurology", "karim@test.com", "0122222222",
                    List.of("09:00-10:00", "13:00-14:00", "15:00-16:00")));
            doctorRepository.save(doctor("Dr. Laila Samy", "Dermatology", "laila@test.com", "0133333333",
                    List.of("10:00-11:00", "11:00-12:00", "16:00-17:00")));
        }

        if (patientRepository.count() == 0) {
            patientRepository.save(patient("Ahmed Ali", "ahmed@test.com", "0100000001", "Cairo"));
            patientRepository.save(patient("Sara Hassan", "sara@test.com", "0100000002", "Giza"));
            patientRepository.save(patient("Omar Khaled", "omar@test.com", "0100000003", "Alexandria"));
            patientRepository.save(patient("Mona Samir", "mona@test.com", "0100000004", "Tanta"));
            patientRepository.save(patient("Youssef Adel", "youssef@test.com", "0100000005", "Mansoura"));
        }

        if (appointmentRepository.count() == 0) {
            Doctor hana = doctorRepository.findByEmail("hana@test.com");
            Doctor karim = doctorRepository.findByEmail("karim@test.com");
            LocalDateTime tomorrow = LocalDateTime.now().plusDays(1).withMinute(0).withSecond(0).withNano(0);

            appointmentRepository.save(appointment(hana, patientRepository.findByEmail("ahmed@test.com"),
                    tomorrow.withHour(9)));
            appointmentRepository.save(appointment(hana, patientRepository.findByEmail("sara@test.com"),
                    tomorrow.withHour(10)));
            appointmentRepository.save(appointment(hana, patientRepository.findByEmail("omar@test.com"),
                    tomorrow.plusDays(1).withHour(14)));
            appointmentRepository.save(appointment(karim, patientRepository.findByEmail("mona@test.com"),
                    tomorrow.withHour(13)));
        }
    }

    private Doctor doctor(String name, String specialty, String email, String phone, List<String> times) {
        Doctor d = new Doctor();
        d.setName(name);
        d.setSpecialty(specialty);
        d.setEmail(email);
        d.setPassword("doctor123");
        d.setPhone(phone);
        d.setAvailableTimes(new java.util.ArrayList<>(times));
        return d;
    }

    private Patient patient(String name, String email, String phone, String address) {
        Patient p = new Patient();
        p.setName(name);
        p.setEmail(email);
        p.setPassword("patient123");
        p.setPhone(phone);
        p.setAddress(address);
        return p;
    }

    private Appointment appointment(Doctor doctor, Patient patient, LocalDateTime time) {
        Appointment a = new Appointment();
        a.setDoctor(doctor);
        a.setPatient(patient);
        a.setAppointmentTime(time);
        a.setStatus(0);
        return a;
    }
}
