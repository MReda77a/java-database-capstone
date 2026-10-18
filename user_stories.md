# User Stories – Smart Clinic Management System

Format: **As a \<role\>, I want \<feature\>, so that \<benefit\>.**

## Admin

### Admin 1 – Log in
**Title:** Admin login
_As an admin, I want to log into the portal with my username and password, so that I can manage the platform securely._

**Acceptance Criteria:**
1. Valid credentials open the admin dashboard.
2. Invalid credentials show an error message.
3. A JWT token is issued for the session.

**Priority:** High  **Story Points:** 3
**Notes:** Passwords must never be returned by the API.

### Admin 2 – Log out
**Title:** Admin logout
_As an admin, I want to log out of the portal, so that I can protect system access._

**Acceptance Criteria:**
1. The session token is cleared on logout.
2. The user returns to the home page.

**Priority:** Medium  **Story Points:** 1

### Admin 3 – Add doctors
**Title:** Add doctor profile
_As an admin, I want to add doctors to the portal, so that patients can find and book them._

**Acceptance Criteria:**
1. Name, specialty, email, phone and password are required.
2. Duplicate emails are rejected.
3. The new doctor appears in the doctor list.

**Priority:** High  **Story Points:** 3

### Admin 4 – Delete doctor profile
**Title:** Delete doctor profile
_As an admin, I want to delete a doctor's profile from the portal, so that inactive doctors are no longer shown._

**Acceptance Criteria:**
1. Only an authenticated admin can delete.
2. The doctor is removed from the list.
3. Related data is handled without leaving orphan records.

**Priority:** Medium  **Story Points:** 2

### Admin 5 – Usage statistics
**Title:** Run usage report
_As an admin, I want to run a stored procedure in the MySQL CLI to get the number of appointments per month, so that I can track usage statistics._

**Acceptance Criteria:**
1. The procedure returns appointment counts grouped by month.
2. It can be executed from the MySQL command line.

**Priority:** Low  **Story Points:** 3

## Patient

### Patient 1 – View doctors without logging in
**Title:** Browse doctors
_As a patient, I want to view a list of doctors without logging in, so that I can explore options before registering._

**Acceptance Criteria:**
1. The doctor list is visible to anonymous visitors.
2. Booking requires login.

**Priority:** Medium  **Story Points:** 2

### Patient 2 – Sign up
**Title:** Patient registration
_As a patient, I want to sign up using my email and password, so that I can book appointments._

**Acceptance Criteria:**
1. Email, password, phone and address are validated.
2. Duplicate email or phone is rejected.

**Priority:** High  **Story Points:** 3

### Patient 3 – Log in
**Title:** Patient login
_As a patient, I want to log into the portal, so that I can manage my bookings._

**Acceptance Criteria:**
1. Valid credentials return a JWT token.
2. Invalid credentials show an error.

**Priority:** High  **Story Points:** 2

### Patient 4 – Log out
**Title:** Patient logout
_As a patient, I want to log out of the portal, so that I can secure my account._

**Acceptance Criteria:**
1. The token is cleared on logout.

**Priority:** Medium  **Story Points:** 1

### Patient 5 – Book an appointment
**Title:** Book one-hour appointment
_As a patient, I want to log in and book an hour-long appointment, so that I can consult a doctor._

**Acceptance Criteria:**
1. Only free time slots can be chosen.
2. The appointment time must be in the future.
3. A confirmation is shown after booking.

**Priority:** High  **Story Points:** 5

### Patient 6 – View upcoming appointments
**Title:** View my appointments
_As a patient, I want to view my upcoming appointments, so that I can prepare accordingly._

**Acceptance Criteria:**
1. Appointments are listed with doctor, date and time.
2. Only the logged-in patient's appointments are shown.

**Priority:** Medium  **Story Points:** 2

## Doctor

### Doctor 1 – Log in
**Title:** Doctor login
_As a doctor, I want to log into the portal, so that I can manage my appointments._

**Acceptance Criteria:**
1. Valid credentials return a JWT token.
2. Invalid credentials show an error.

**Priority:** High  **Story Points:** 2

### Doctor 2 – Log out
**Title:** Doctor logout
_As a doctor, I want to log out of the portal, so that my data stays protected._

**Acceptance Criteria:**
1. The token is cleared on logout.

**Priority:** Medium  **Story Points:** 1

### Doctor 3 – View appointment calendar
**Title:** View appointment calendar
_As a doctor, I want to view my appointment calendar, so that I can stay organized._

**Acceptance Criteria:**
1. Appointments are shown by date.
2. Only the doctor's own appointments are shown.

**Priority:** High  **Story Points:** 3

### Doctor 4 – Mark unavailability
**Title:** Mark unavailable times
_As a doctor, I want to mark my unavailability, so that patients only see the slots I can take._

**Acceptance Criteria:**
1. Unavailable slots are removed from the available times.
2. Patients can no longer book those slots.

**Priority:** Medium  **Story Points:** 3

### Doctor 5 – Update profile
**Title:** Update doctor profile
_As a doctor, I want to update my profile with specialization and contact information, so that patients have up-to-date information._

**Acceptance Criteria:**
1. Specialty, email and phone can be edited.
2. Changes are validated and saved.

**Priority:** Medium  **Story Points:** 2

### Doctor 6 – View patient details
**Title:** View patient details for upcoming appointments
_As a doctor, I want to view patient details for upcoming appointments, so that I can be prepared._

**Acceptance Criteria:**
1. Name, phone and appointment time are shown.
2. Only the doctor's own patients are visible.

**Priority:** Medium  **Story Points:** 2
