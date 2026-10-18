# Schema Design – Smart Clinic Management System

The system uses **MySQL** for structured, relational data that needs integrity
(patients, doctors, appointments, admins) and **MongoDB** for flexible,
document-style data (prescriptions).

## MySQL Database Design

### Table: patients
- id: INT, Primary Key, Auto Increment
- name: VARCHAR(100), Not Null
- email: VARCHAR(100), Not Null, Unique
- password: VARCHAR(255), Not Null
- phone: VARCHAR(10), Not Null, Unique
- address: VARCHAR(255), Not Null

### Table: doctors
- id: INT, Primary Key, Auto Increment
- name: VARCHAR(100), Not Null
- specialty: VARCHAR(50), Not Null
- email: VARCHAR(100), Not Null, Unique
- password: VARCHAR(255), Not Null
- phone: VARCHAR(10), Not Null

### Table: doctor_available_times
- doctor_id: INT, Foreign Key → doctors(id), Not Null
- available_times: VARCHAR(20), Not Null (e.g. `09:00-10:00`)

### Table: appointments
- id: INT, Primary Key, Auto Increment
- doctor_id: INT, Foreign Key → doctors(id), Not Null
- patient_id: INT, Foreign Key → patients(id), Not Null
- appointment_time: DATETIME, Not Null (must be in the future)
- status: INT, Not Null (0 = Scheduled, 1 = Completed, 2 = Cancelled)

### Table: admin
- id: INT, Primary Key, Auto Increment
- username: VARCHAR(50), Not Null, Unique
- password: VARCHAR(255), Not Null

### Design notes
- **Deleting a patient**: appointments reference patients, so deletion is
  blocked (or the appointments are deleted first) to avoid orphan rows.
  History is preserved by default.
- **Overlapping appointments**: the service layer rejects a booking when the
  same doctor already has an appointment at that `appointment_time`.
- **Validation**: email format, 10-digit phone numbers and future-only
  appointment times are enforced by Bean Validation in the models; uniqueness
  is enforced by the database.
- **Passwords** are write-only in JSON (never returned by the API).

## MongoDB Collection Design

### Collection: prescriptions

```json
{
  "_id": "ObjectId('64abc123456')",
  "patientName": "John Smith",
  "appointmentId": 51,
  "medication": "Paracetamol",
  "dosage": "500mg",
  "doctorNotes": "Take 1 tablet every 6 hours.",
  "refillCount": 2,
  "pharmacy": {
    "name": "Walgreens SF",
    "location": "Market Street"
  },
  "tags": ["fever", "short-term"]
}
```

Prescriptions live in MongoDB because their shape varies (optional notes,
refill info, pharmacy details, tags) and they are read as a whole document,
which suits a document store better than fixed relational columns.
`appointmentId` links each prescription back to the MySQL `appointments` table.
