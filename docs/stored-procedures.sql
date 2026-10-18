-- Run in MySQL:  USE cms;  then paste this file.
-- Table/column names follow the JPA entities (Hibernate default naming):
--   appointment(id, doctor_id, patient_id, appointment_time, status), doctor(id, name, ...)
-- If your tables are named differently (e.g. "appointments"), adjust the names below.

DELIMITER $$

DROP PROCEDURE IF EXISTS GetDailyAppointmentReportByDoctor$$
CREATE PROCEDURE GetDailyAppointmentReportByDoctor(IN report_date DATE)
BEGIN
    SELECT d.name AS doctor_name,
           a.appointment_time,
           a.status,
           p.name AS patient_name,
           p.phone AS patient_phone
    FROM appointment a
    JOIN doctor d  ON a.doctor_id = d.id
    JOIN patient p ON a.patient_id = p.id
    WHERE DATE(a.appointment_time) = report_date
    ORDER BY d.name, a.appointment_time;
END$$

DROP PROCEDURE IF EXISTS GetDoctorWithMostPatientsByMonth$$
CREATE PROCEDURE GetDoctorWithMostPatientsByMonth(IN input_month INT, IN input_year INT)
BEGIN
    SELECT doctor_id, COUNT(patient_id) AS patients_seen
    FROM appointment
    WHERE MONTH(appointment_time) = input_month
      AND YEAR(appointment_time)  = input_year
    GROUP BY doctor_id
    ORDER BY patients_seen DESC
    LIMIT 1;
END$$

DROP PROCEDURE IF EXISTS GetDoctorWithMostPatientsByYear$$
CREATE PROCEDURE GetDoctorWithMostPatientsByYear(IN input_year INT)
BEGIN
    SELECT doctor_id, COUNT(patient_id) AS patients_seen
    FROM appointment
    WHERE YEAR(appointment_time) = input_year
    GROUP BY doctor_id
    ORDER BY patients_seen DESC
    LIMIT 1;
END$$

DELIMITER ;

-- Usage:
-- CALL GetDailyAppointmentReportByDoctor('2026-11-02');
-- CALL GetDoctorWithMostPatientsByMonth(11, 2026);
-- CALL GetDoctorWithMostPatientsByYear(2026);
