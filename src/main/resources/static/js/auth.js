const loginForm = document.getElementById('loginForm');
const registerForm = document.getElementById('registerForm');
const messageDiv = document.getElementById('message');

function showMessage(message, isError = false) {
    messageDiv.textContent = message;
    messageDiv.className = isError ? 'message error' : 'message success';
    messageDiv.style.display = 'block';
}

if (loginForm) {
    loginForm.addEventListener('submit', async function(e) {
        e.preventDefault();
        
        const email = document.getElementById('email').value.trim();
        const password = document.getElementById('password').value;
        
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
                        window.location.href = '/pages/admin.html';
                    } else {
                        window.location.href = '/pages/dashboard.html';
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
                    window.location.href = '/index.html';
                }, 2000);
            } else {
                showMessage(data.message || 'Registration failed', true);
            }
        } catch (error) {
            showMessage('Network error. Please try again.', true);
        }
    });
}

