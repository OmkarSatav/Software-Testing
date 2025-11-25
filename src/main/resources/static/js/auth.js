const loginForm = document.getElementById('loginForm');
const registerForm = document.getElementById('registerForm');
const messageDiv = document.getElementById('message');

function showMessage(message, isError = false) {
    messageDiv.textContent = message;
    messageDiv.className = isError ? 'message error' : 'message success';
    messageDiv.style.display = 'block';
}

function validateEmail(email) {
    const re = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    return re.test(email);
}

function validatePassword(password) {
    return password && password.length >= 6;
}

if (loginForm) {
    loginForm.addEventListener('submit', async function(e) {
        e.preventDefault();
        
        const email = document.getElementById('email').value.trim();
        const password = document.getElementById('password').value;
        
        if (!validateEmail(email)) {
            showMessage('Please enter a valid email address', true);
            return;
        }
        
        if (!validatePassword(password)) {
            showMessage('Password must be at least 6 characters', true);
            return;
        }
        
        try {
            const response = await fetch('/api/auth/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email, password })
            });
            
            const data = await response.json();
            
            if (data.success) {
                showMessage('Login successful! Redirecting...');
                setTimeout(() => {
                    if (data.role === 'ADMIN') {
                        window.location.href = '/static/pages/admin.html';
                    } else {
                        window.location.href = '/static/pages/dashboard.html';
                    }
                }, 1000);
            } else {
                showMessage(data.message || 'Login failed', true);
            }
        } catch (error) {
            showMessage('Network error. Please try again.', true);
        }
    });
}

if (registerForm) {
    registerForm.addEventListener('submit', async function(e) {
        e.preventDefault();
        
        const firstName = document.getElementById('firstName').value.trim();
        const lastName = document.getElementById('lastName').value.trim();
        const email = document.getElementById('email').value.trim();
        const phoneNumber = document.getElementById('phoneNumber').value.trim();
        const password = document.getElementById('password').value;
        
        if (!firstName || !lastName) {
            showMessage('Please enter your full name', true);
            return;
        }
        
        if (!validateEmail(email)) {
            showMessage('Please enter a valid email address', true);
            return;
        }
        
        if (!/^[0-9]{10}$/.test(phoneNumber)) {
            showMessage('Phone number must be 10 digits', true);
            return;
        }
        
        if (!validatePassword(password)) {
            showMessage('Password must be at least 6 characters', true);
            return;
        }
        
        try {
            const response = await fetch('/api/auth/register', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ firstName, lastName, email, phoneNumber, password })
            });
            
            const data = await response.json();
            
            if (data.success) {
                showMessage('Registration successful! Redirecting to login...');
                setTimeout(() => {
                    window.location.href = '/static/index.html';
                }, 2000);
            } else {
                showMessage(data.message || 'Registration failed', true);
            }
        } catch (error) {
            showMessage('Network error. Please try again.', true);
        }
    });
}

