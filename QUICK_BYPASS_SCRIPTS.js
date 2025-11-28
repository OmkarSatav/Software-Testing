// ============================================
// QUICK BYPASS SCRIPTS FOR TA DEMONSTRATION
// Copy and paste these into Browser Console
// ============================================

// ============================================
// REGISTRATION FORM BYPASS
// ============================================
// Run this on: http://localhost:8080/register.html

(function() {
    console.log('Starting Registration Bypass...');
    
    // Override JavaScript validation functions
    window.validateEmail = function() { return true; };
    window.validatePassword = function() { return true; };
    
    // Remove all HTML5 validation attributes
    const emailField = document.getElementById('email');
    const phoneField = document.getElementById('phoneNumber');
    const passwordField = document.getElementById('password');
    const firstNameField = document.getElementById('firstName');
    const lastNameField = document.getElementById('lastName');
    
    // Remove required attributes
    [emailField, phoneField, passwordField, firstNameField, lastNameField].forEach(field => {
        if (field) {
            field.removeAttribute('required');
            field.removeAttribute('pattern');
            field.removeAttribute('minlength');
        }
    });
    
    // Change email type to bypass HTML5 email validation
    emailField.type = 'text';
    
    // Set invalid test values
    emailField.value = 'not-an-email';
    phoneField.value = '123';
    passwordField.value = '123';
    firstNameField.value = '';
    lastNameField.value = '';
    
    console.log('Registration bypass complete!');
    console.log('Form is now ready - click Submit to register with invalid data!');
    console.log('Check Network tab to see the successful API call');
})();

// ============================================
// BOOKING FORM BYPASS
// ============================================
// Run this on: http://localhost:8080/pages/dashboard.html

(function() {
    console.log('Starting Booking Bypass...');
    
    // Remove pickup time minimum constraint
    const pickupTimeField = document.getElementById('pickupTime');
    pickupTimeField.removeAttribute('min');
    pickupTimeField.value = '2020-01-01T10:00'; // Past date
    
    // Remove passenger limits
    const passengersField = document.getElementById('passengers');
    passengersField.removeAttribute('max');
    passengersField.removeAttribute('min');
    passengersField.value = '25'; // Excessive passengers
    
    // Remove required attributes
    document.getElementById('vehicleType').removeAttribute('required');
    document.getElementById('paymentMode').removeAttribute('required');
    
    // Disable HTML5 form validation
    document.getElementById('bookingForm').setAttribute('novalidate', 'true');
    
    // Override JavaScript validation
    window.validateBookingForm = function() { return true; };
    
    console.log('Booking bypass complete!');
    console.log('Form now accepts:');
    console.log('   - Past pickup dates (2020-01-01)');
    console.log('   - 25 passengers (exceeds limit of 4)');
    console.log('   - Any invalid data');
    console.log('Click "Book Now" - it will work!');
})();

// ============================================
// TEST NEGATIVE PASSENGERS
// ============================================
// Run this after booking bypass to test negative values

document.getElementById('passengers').value = '-5';
console.log('Set passengers to -5 (negative value)');

// ============================================
// TEST ZERO PASSENGERS
// ============================================
// Run this after booking bypass to test zero

document.getElementById('passengers').value = '0';
console.log('Set passengers to 0 (zero value)');

// ============================================
// DIRECT API CALL BYPASS (No Form Needed)
// ============================================
// Registration bypass via direct API call

fetch('/api/auth/register', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
        firstName: '',
        lastName: '',
        email: 'completely-invalid-email-format',
        phoneNumber: '1',  // Less than 10 digits
        password: '123'     // Less than 6 characters
    })
})
.then(response => response.json())
.then(data => {
    console.log('Registration API Response:', data);
    if (data.success) {
        console.log('SUCCESS! Invalid data was accepted by server!');
    }
})
.catch(error => console.error('Error:', error));

// ============================================
// BOOKING API BYPASS (Direct Call)
// ============================================
// Note: You need to be logged in first (session required)

fetch('/api/bookings/create', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include',  // Include session cookie
    body: JSON.stringify({
        pickupLatitude: 19.0760,
        pickupLongitude: 72.8777,
        dropLatitude: 28.6139,  // Delhi (outside service area)
        dropLongitude: 77.2090,
        pickupTime: '2020-01-01T10:00:00',  // Past date
        passengers: 25,  // Excessive
        vehicleType: 'SEDAN',
        paymentMode: 'CASH'
    })
})
.then(response => response.json())
.then(data => {
    console.log('Booking API Response:', data);
    if (data.success) {
        console.log('SUCCESS! Invalid booking data was accepted!');
        console.log('   - Past date accepted');
        console.log('   - 25 passengers accepted');
        console.log('   - Outside service area accepted');
    }
})
.catch(error => console.error('Error:', error));

// ============================================
// VERIFICATION: Check What Was Accepted
// ============================================
// After submitting, verify the data was stored

console.log('To verify bypass worked:');
console.log('1. Check Network tab → See 200 OK response');
console.log('2. Check Application tab → See stored session data');
console.log('3. Try logging in with the invalid email you registered');
console.log('4. Check database to see the corrupted data was stored');

