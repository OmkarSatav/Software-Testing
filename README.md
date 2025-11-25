# Taxi Booking System - Bypass Testing Project

## CSE 731: Software Testing - Term I 2025-26

**Team Members:**
- MT2024106 Omkar Dhananjay Satav
- MT2024107 Shivam Padaliya

---

## Project Overview

This project implements a **Client-Side Web Application Bypass Testing** system as specified in the CSE731 Software Testing course requirements. The core objective is to demonstrate how client-side validations can be bypassed and to validate that proper server-side validation is essential for web application security.

### Key Concept

> **Client-side validation cannot be trusted. All rules enforced in the browser must be revalidated and protected on the server.**

This project intentionally includes rich client-side validation logic (HTML5 constraints, JavaScript validations, disabled fields, hidden inputs) and then systematically tests whether these can be bypassed to send corrupted data to the backend.

---

## System Architecture

### Backend (Java Spring Boot)
- **Framework:** Spring Boot 4.0.0
- **Database:** H2 (in-memory for testing)
- **ORM:** JPA/Hibernate
- **Build Tool:** Maven

### Frontend (HTML/CSS/JavaScript)
- Pure HTML5 with client-side validation
- Vanilla JavaScript for form validation and API calls
- CSS3 for modern, responsive UI

### Testing Stack
- **JUnit 5:** Backend validation tests
- **Postman/Newman:** API bypass testing
- **Selenium WebDriver:** UI manipulation tests
- **Stryker (optional):** JavaScript mutation testing

---

## Features & Client-Side Validations

### 1. User Authentication
**Client-Side Validations:**
- Email format validation (regex pattern)
- Password minimum length (6 characters)
- Required field validation
- Phone number format (10 digits)

**Bypass Opportunities:**
- Login with empty password
- Login with malformed email
- Register with invalid phone format

### 2. Taxi Booking System
**Client-Side Validations:**
- Pickup time must be in future (min attribute)
- Passengers: 1-4 only (min/max attributes)
- Fare field is disabled (readonly)
- Vehicle type selection mandatory (required)
- Distance calculation hidden from user
- One active booking limit (UI button disabled)

**Bypass Opportunities:**
- Send past pickup time via API
- Send passengers = 25 or 0 or negative
- Manipulate fare to ₹1
- Send invalid vehicle type
- Create multiple active bookings
- Modify hidden distance field

### 3. File Upload (ID Verification)
**Client-Side Validations:**
- File type: .jpg, .png only (accept attribute)
- Max size: 1MB (JavaScript check)
- File required

**Bypass Opportunities:**
- Upload .exe renamed as .jpg
- Upload 10MB file via Postman
- Send invalid MIME type

### 4. Admin Dashboard
**Client-Side Validations:**
- Role-based UI access
- Admin-only sections hidden for regular users

**Bypass Opportunities:**
- Access /api/admin/* without admin role
- Manipulate session cookie
- Spoof admin role in request

---

## Project Structure

```
RideWithMe/
├── src/
│   ├── main/
│   │   ├── java/com/taxiapp/
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── BookingController.java
│   │   │   │   ├── AdminController.java
│   │   │   │   ├── DriverController.java
│   │   │   │   └── FileUploadController.java
│   │   │   ├── service/
│   │   │   │   ├── AuthService.java
│   │   │   │   ├── BookingService.java
│   │   │   │   ├── FareService.java
│   │   │   │   ├── DriverService.java
│   │   │   │   └── AdminService.java
│   │   │   ├── model/
│   │   │   │   ├── User.java
│   │   │   │   ├── Booking.java
│   │   │   │   ├── Driver.java
│   │   │   │   └── FareConfig.java
│   │   │   ├── repository/
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── BookingRepository.java
│   │   │   │   ├── DriverRepository.java
│   │   │   │   └── FareConfigRepository.java
│   │   │   ├── util/
│   │   │   │   ├── DistanceCalculator.java
│   │   │   │   ├── FareCalculator.java
│   │   │   │   └── TimeValidator.java
│   │   │   └── TaxiBookingApplication.java
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── index.html
│   │       │   ├── register.html
│   │       │   ├── css/
│   │       │   │   ├── auth.css
│   │       │   │   └── dashboard.css
│   │       │   ├── js/
│   │       │   │   ├── auth.js
│   │       │   │   ├── booking.js
│   │       │   │   ├── upload.js
│   │       │   │   └── admin.js
│   │       │   └── pages/
│   │       │       ├── dashboard.html
│   │       │       ├── bookings.html
│   │       │       ├── upload.html
│   │       │       └── admin.html
│   │       └── application.properties
│   └── test/
│       └── java/com/taxiapp/
│           ├── BypassTestSuite.java
│           └── AuthServiceTest.java
├── tests/
│   ├── postman/
│   │   └── BypassTests.postman_collection.json
│   └── selenium/
│       └── BookingBypassUITest.java
├── pom.xml
└── README.md
```

---

## Bypass Testing Strategy

### 1. JUnit Backend Validation Tests
**File:** `src/test/java/com/taxiapp/BypassTestSuite.java`

Tests server-side validation logic:
- Past pickup time rejection
- Excessive/zero/negative passengers rejection
- Location outside service area rejection
- Multiple active bookings prevention
- Unauthorized booking cancellation
- Distance and passenger boundary tests

**Run Tests:**
```bash
mvn test
```

### 2. Postman API Bypass Tests
**File:** `tests/postman/BypassTests.postman_collection.json`

Sends manipulated HTTP requests directly to backend:
- Bypass 1: Past pickup time (2020-01-01)
- Bypass 2: 25 passengers
- Bypass 3: 0 passengers
- Bypass 4: Negative passengers (-5)
- Bypass 5: Location outside service area
- Bypass 6: Upload non-image file
- Bypass 7: Access admin API without role

**Run Tests:**
```bash
newman run tests/postman/BypassTests.postman_collection.json
```

### 3. Selenium UI Manipulation Tests
**File:** `tests/selenium/BookingBypassUITest.java`

Uses JavaScript execution to manipulate DOM:
- Remove `min` attribute from datetime input
- Change `max` attribute on passenger field
- Enable `readonly` fare field
- Remove `required` attributes
- Inject hidden fields
- Disable form validation (novalidate)

**Run Tests:**
```bash
mvn test -Dtest=BookingBypassUITest
```

---

## Setup & Installation

### Prerequisites
- Java 11 or higher
- Maven 3.6+
- Chrome browser (for Selenium tests)
- Postman or Newman (for API tests)


## Test Results Summary

### JUnit Tests (12 Tests)
- Past pickup time rejected
- Excessive passengers rejected
- Zero passengers rejected
- Negative passengers rejected
- Outside service area rejected
- Multiple active bookings prevented
- Valid booking accepted
- Passenger boundary validation
- Distance validation
- Cancel non-existent booking fails
- Cancel other user's booking fails
- Cancel already cancelled booking fails

### Postman Tests (7 Tests)
- Past time bypass blocked
- Excessive passengers bypass blocked
- Zero passengers bypass blocked
- Negative passengers bypass blocked
- Outside service area bypass blocked
- Invalid file upload blocked
- Admin access without role blocked

### Selenium Tests (10 Tests)
- DOM manipulation successful
- Attribute removal successful
- Hidden field injection successful
- Server still validates manipulated data

---

## Key Findings

### Client-Side Validation Weaknesses
1. **HTML5 Attributes:** Can be removed via DevTools
2. **JavaScript Validation:** Can be disabled
3. **Disabled Fields:** Can be enabled and modified
4. **Hidden Fields:** Can be injected or modified
5. **Min/Max Constraints:** Can be changed
6. **Required Fields:** Can be bypassed

### Server-Side Protection (Implemented)
1. Time validation (must be future)
2. Passenger count validation (1-4)
3. Distance validation (within service radius)
4. File type and size validation
5. Role-based access control
6. Session authentication
7. Business rule enforcement (one active booking)

---

## Technologies Used

### Backend
- Spring Boot 4.0.0
- Spring Data JPA
- H2 Database
- Lombok
- Bean Validation

### Frontend
- HTML5
- CSS3
- Vanilla JavaScript
- Fetch API

### Testing
- JUnit 5
- Mockito
- Selenium WebDriver 4.8.0
- Postman/Newman
- Spring Boot Test

---

## Individual Contributions

### MT2024106 Omkar Dhananjay Satav
- Backend service layer implementation
- Server-side validation logic
- JUnit test suite development
- Database schema design

### MT2024115 Shivam Padaliya
- Frontend UI/UX implementation
- Client-side validation logic
- Postman bypass test collection
- Selenium UI manipulation tests

---

## Demonstration for TA

### What to Show

1. **Normal Flow:**
   - Register and login
   - Try booking with invalid data (UI blocks it)
   - Show client-side validation messages

2. **Bypass Attempt:**
   - Open DevTools → Console
   - Execute: `document.getElementById('passengers').value = 25`
   - Execute: `document.getElementById('pickupTime').removeAttribute('min')`
   - Submit form
   - **Show:** Server rejects with error message

3. **Postman Bypass:**
   - Import collection
   - Run "BYPASS 2: Excessive Passengers (25)"
   - **Show:** 400 Bad Request with validation error

4. **JUnit Tests:**
   - Run: `mvn test`
   - **Show:** All 12 tests pass (server validation works)

---

## Conclusion

This project successfully demonstrates:

1. **Client-side validation is insufficient** for security
2. **Server-side validation is mandatory** for all inputs
3. **Bypass testing reveals vulnerabilities** in web applications
4. **Comprehensive testing strategy** includes unit, integration, and UI tests

The system proves that without proper server-side validation, attackers can easily bypass client-side restrictions and send malicious data to the backend.

---

## References

- CSE731 Software Testing Course Material
- OWASP Web Application Security Testing Guide
- Spring Boot Documentation
- Selenium WebDriver Documentation
- Postman API Testing Guide

---

## License

This project is created for educational purposes as part of CSE731 Software Testing coursework.
