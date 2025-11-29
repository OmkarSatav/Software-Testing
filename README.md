# Taxi Booking System – Client-Side Bypass Testing

## CSE 731: Software Testing – Term I 2025–26

**Team Members**
- MT2024106 Omkar Dhananjay Satav
- MT2024107 Shivam Padaliya

---

## 1. Project Overview

This project implements **Client-Side Web Application Bypass Testing** for a taxi booking web application.

The main objective is to show that any validation enforced only in the browser (HTML or JavaScript) can be bypassed by an attacker, and therefore **must not be trusted for security**.

We do this in two ways:

1. **Manual bypass testing** using browser DevTools
2. **Automated bypass testing** using Selenium WebDriver (`BookingBypassUITest`)

There are no API-level Postman tests and no separate backend JUnit suites in the current version. The focus is strictly on **client-side bypass testing**, as required by the project topic.

---

## 2. Core Idea

> Client-side validation improves user experience but is not a security control.  
> Any input rule implemented only in the browser can be bypassed and must be revalidated on the server.

The front-end for this project intentionally includes:

- HTML5 attributes such as `type="email"`, `required`, `min`, `max`, `readonly`, etc.
- Form-level validation behavior
- Disabled and hidden fields

Our tests then **break or disable these protections** and submit invalid data.

---

## 3. System Architecture (High Level)

### 3.1 Backend

- Java Spring Boot application that exposes endpoints for:
    - User registration (`/api/auth/register`)
    - User login (`/api/auth/login`)
    - Creating bookings (`/api/bookings`)
    - Viewing bookings (`/api/bookings/user`)
- Uses standard Spring Boot test support with `@SpringBootTest(webEnvironment = RANDOM_PORT)` so that Selenium can talk to a real HTTP server.

### 3.2 Frontend

- Static HTML pages under `src/main/resources/static`:

    - `index.html` – Login page
    - `register.html` – Registration page
    - `pages/dashboard.html` – Booking screen
    - `pages/bookings.html` – List of bookings
    - `pages/upload.html`, `pages/admin.html` – other flows (not exercised by Selenium in this version)

- CSS for styling (`auth.css`, `dashboard.css`)
- Small JavaScript files for invoking backend APIs and basic runtime behavior

### 3.3 Testing Stack

- **JUnit 5** – Test framework
- **Selenium WebDriver** – Browser automation and DOM manipulation
- **ChromeDriver** – Headless Chrome browser used from tests
- **Spring Boot Test** – To start the backend automatically during tests

There is a single automated test class:

- `src/test/java/com/taxiapp/selenium/BookingBypassUITest.java`

---

## 4. Client-Side Validations Implemented

### 4.1 Authentication Pages (`index.html`, `register.html`)

- Email field with `type="email"`
- `required` attributes for all critical fields
- Password with `minlength="6"`
- Phone number field with `pattern="[0-9]{10}"`

Examples of values that normal users cannot submit without bypass:

- Email: `seleniumtestcom` (missing `@`)
- Phone: `12345` (less than 10 digits)
- Empty password

### 4.2 Booking Page (`pages/dashboard.html`)

Typical client-side restrictions include:

- `pickupTime` field with a `min` attribute for future time only
- `passengers` input with `min` and `max` for allowed range (e.g., 1–4)
- `estimatedFare` field marked as `readonly`
- `vehicleType` marked as `required`
- Hidden fields for latitude/longitude and derived distance

These are all enforced on the client side but can be changed at runtime.

---

## 5. Project Structure

```text
RideWithMe/
├── src/
│   ├── main/
│   │   ├── java/com/taxiapp/...
│   │   └── resources/
│   │       └── static/
│   │           ├── index.html
│   │           ├── register.html
│   │           └── pages/
│   │               ├── dashboard.html
│   │               ├── bookings.html
│   │               ├── upload.html
│   │               └── admin.html
│   └── test/
│       └── java/com/taxiapp/selenium/
│           └── BookingBypassUITest.java
└── pom.xml
