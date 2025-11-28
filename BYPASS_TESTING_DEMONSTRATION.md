# Manual Bypass Testing Demonstration Guide

## For TA Demonstration: How to Bypass Client-Side Validation

This guide shows step-by-step how to manually bypass client-side validation to demonstrate the security weakness.

---

## **Registration Form Bypass Testing**

### **Step 1: Open the Registration Page**
1. Navigate to: `http://localhost:8080/register.html`
2. Open Browser DevTools (Press `F12` or `Right-click → Inspect`)

### **Step 2: Identify Client-Side Validations**

The registration form has these client-side validations:
- **Email**: HTML5 `type="email"` + `pattern` attribute + JavaScript `validateEmail()` function
- **Phone**: HTML5 `pattern="[0-9]{10}"` + JavaScript regex check
- **Password**: HTML5 `minlength="6"` + JavaScript `validatePassword()` function
- **Required fields**: HTML5 `required` attribute

### **Step 3: Bypass Methods (Choose One)**

#### **Method A: Using Browser Console (Recommended for Demo)**

1. **Open Console Tab** in DevTools
2. **Disable JavaScript Validation:**
   ```javascript
   // Override the validation functions
   window.validateEmail = function() { return true; };
   window.validatePassword = function() { return true; };
   ```

3. **Remove HTML5 Validation Attributes:**
   ```javascript
   // Remove required attributes
   document.getElementById('email').removeAttribute('required');
   document.getElementById('phoneNumber').removeAttribute('required');
   document.getElementById('password').removeAttribute('required');
   document.getElementById('firstName').removeAttribute('required');
   document.getElementById('lastName').removeAttribute('required');
   
   // Remove pattern attributes
   document.getElementById('email').removeAttribute('pattern');
   document.getElementById('phoneNumber').removeAttribute('pattern');
   
   // Remove minlength
   document.getElementById('password').removeAttribute('minlength');
   
   // Change email input type to text (bypasses HTML5 email validation)
   document.getElementById('email').type = 'text';
   ```

4. **Now Fill the Form with Invalid Data:**
   - **Email**: `invalid-email` (no @ symbol)
   - **Phone**: `123` (less than 10 digits)
   - **Password**: `123` (less than 6 characters)
   - **First Name**: (can be empty)
   - **Last Name**: (can be empty)

5. **Submit the Form** - It should now submit successfully!

#### **Method B: Direct DOM Manipulation**

1. **In Console, directly set invalid values:**
   ```javascript
   document.getElementById('email').value = 'invalid-email';
   document.getElementById('phoneNumber').value = '123';
   document.getElementById('password').value = '123';
   document.getElementById('firstName').value = '';
   document.getElementById('lastName').value = '';
   ```

2. **Remove form validation:**
   ```javascript
   document.getElementById('registerForm').setAttribute('novalidate', 'true');
   ```

3. **Bypass JavaScript validation by directly calling the fetch:**
   ```javascript
   fetch('/api/auth/register', {
       method: 'POST',
       headers: { 'Content-Type': 'application/json' },
       body: JSON.stringify({ 
           firstName: '', 
           lastName: '', 
           email: 'invalid-email', 
           phoneNumber: '123', 
           password: '123' 
       })
   }).then(r => r.json()).then(console.log);
   ```

#### **Method C: Using Elements Tab (Visual Demo)**

1. **Open Elements Tab** in DevTools
2. **Find the email input field** and double-click on it
3. **Remove attributes** by editing the HTML:
   - Remove `required`
   - Remove `pattern`
   - Change `type="email"` to `type="text"`
4. **Repeat for other fields** (phone, password)
5. **Fill form with invalid data** and submit

---

## **Booking Form Bypass Testing**

### **Step 1: Navigate to Dashboard**
1. Login first (or use bypass to register)
2. Go to: `http://localhost:8080/pages/dashboard.html`
3. Open DevTools

### **Step 2: Bypass Booking Validations**

**In Console, execute:**

```javascript
// Remove min attribute from pickup time (allows past dates)
document.getElementById('pickupTime').removeAttribute('min');
document.getElementById('pickupTime').value = '2020-01-01T10:00';

// Set excessive passengers (more than 4)
document.getElementById('passengers').removeAttribute('max');
document.getElementById('passengers').value = '25';

// Set negative passengers
document.getElementById('passengers').value = '-5';

// Set zero passengers
document.getElementById('passengers').value = '0';

// Remove required attributes
document.getElementById('vehicleType').removeAttribute('required');
document.getElementById('paymentMode').removeAttribute('required');

// Disable form validation
document.getElementById('bookingForm').setAttribute('novalidate', 'true');

// Override JavaScript validation function
window.validateBookingForm = function() { return true; };
```

**Now submit the booking form** - it should accept all invalid data!

---

## **Quick Demo Script for TA**

### **Registration Bypass (Copy-Paste in Console):**

```javascript
// Complete bypass script for registration
(function() {
    // Override validation functions
    window.validateEmail = () => true;
    window.validatePassword = () => true;
    
    // Remove all HTML5 validations
    const fields = ['email', 'phoneNumber', 'password', 'firstName', 'lastName'];
    fields.forEach(id => {
        const field = document.getElementById(id);
        if (field) {
            field.removeAttribute('required');
            field.removeAttribute('pattern');
            field.removeAttribute('minlength');
            if (id === 'email') field.type = 'text';
        }
    });
    
    // Set invalid values
    document.getElementById('email').value = 'not-an-email';
    document.getElementById('phoneNumber').value = '123';
    document.getElementById('password').value = '123';
    
    console.log('Bypass complete! Form will now accept invalid data.');
    console.log('Fill the form and click Submit - it will work!');
})();
```

### **Booking Bypass (Copy-Paste in Console):**

```javascript
// Complete bypass script for booking
(function() {
    // Remove time constraint
    document.getElementById('pickupTime').removeAttribute('min');
    document.getElementById('pickupTime').value = '2020-01-01T10:00';
    
    // Remove passenger limits
    document.getElementById('passengers').removeAttribute('max');
    document.getElementById('passengers').removeAttribute('min');
    document.getElementById('passengers').value = '25';
    
    // Disable form validation
    document.getElementById('bookingForm').setAttribute('novalidate', 'true');
    window.validateBookingForm = () => true;
    
    console.log('Booking bypass complete!');
    console.log('Submit booking with past date and 25 passengers - it will work!');
})();
```

---

## **What to Show Your TA**

### **Before Bypass:**
1. Try to register with invalid data:
   - Email: `invalid-email`
   - Phone: `123`
   - Password: `123`
2. **Show**: Form blocks submission, shows error messages

### **After Bypass:**
1. Open Console, run the bypass script
2. Fill form with the same invalid data
3. **Show**: Form submits successfully!
4. **Show**: Server accepts the data (check network tab or database)

### **Key Points to Explain:**
- Client-side validation is **easily bypassed** using DevTools
- **Server-side validation is essential** (but we removed it for this demo)
- **Never trust client-side validation** for security
- Always validate on the server

---

## **Alternative: Use Postman for API Bypass**

1. Open Postman
2. Create POST request to: `http://localhost:8080/api/auth/register`
3. Set headers: `Content-Type: application/json`
4. Send body with invalid data:
   ```json
   {
       "firstName": "",
       "lastName": "",
       "email": "not-an-email",
       "phoneNumber": "123",
       "password": "123"
   }
   ```
5. **Show**: Server accepts it! (200 OK response)

---

## **Troubleshooting**

If bypass doesn't work:
1. Make sure you're in the Console tab (not Elements)
2. Check that JavaScript is enabled
3. Try refreshing the page and running the script again
4. Check Network tab to see if the request is actually sent
5. Verify the server is running and accepts requests

---

## **Expected Results**

After bypass:
- Registration with invalid email format → **SUCCESS**
- Registration with short password → **SUCCESS**
- Registration with invalid phone → **SUCCESS**
- Booking with past date → **SUCCESS**
- Booking with 25 passengers → **SUCCESS**
- Booking with negative passengers → **SUCCESS**

This demonstrates that **client-side validation alone is insufficient** for security!

