const messageDiv = document.getElementById('message');

function showMessage(message, isError = false) {
    messageDiv.textContent = message;
    messageDiv.className = isError ? 'message error' : 'message success';
    messageDiv.style.display = 'block';
}

function showTab(tabName) {
    document.querySelectorAll('.tab-content').forEach(tab => {
        tab.classList.remove('active');
    });
    document.querySelectorAll('.tab-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    
    document.getElementById(tabName).classList.add('active');
    event.target.classList.add('active');
    
    if (tabName === 'users') loadUsers();
    else if (tabName === 'bookings') loadBookings();
    else if (tabName === 'drivers') loadDrivers();
}

async function loadUsers() {
    try {
        const response = await fetch('/api/admin/users');
        const users = await response.json();
        
        let html = '<table><thead><tr><th>ID</th><th>Name</th><th>Email</th><th>Role</th><th>Status</th><th>Actions</th></tr></thead><tbody>';
        
        users.forEach(user => {
            html += `
                <tr>
                    <td>${user.id}</td>
                    <td>${user.firstName} ${user.lastName}</td>
                    <td>${user.email}</td>
                    <td>${user.role}</td>
                    <td>${user.active ? 'Active' : 'Inactive'}</td>
                    <td><button onclick="toggleUserStatus(${user.id})">${user.active ? 'Deactivate' : 'Activate'}</button></td>
                </tr>
            `;
        });
        
        html += '</tbody></table>';
        document.getElementById('users').innerHTML = html;
    } catch (error) {
        showMessage('Error loading users', true);
    }
}

async function loadBookings() {
    try {
        const response = await fetch('/api/admin/bookings');
        const bookings = await response.json();
        
        let html = '<table><thead><tr><th>ID</th><th>User ID</th><th>Driver ID</th><th>Distance</th><th>Fare</th><th>Status</th><th>Created</th></tr></thead><tbody>';
        
        bookings.forEach(booking => {
            html += `
                <tr>
                    <td>${booking.id}</td>
                    <td>${booking.userId}</td>
                    <td>${booking.driverId || 'N/A'}</td>
                    <td>${booking.distance.toFixed(2)} km</td>
                    <td>₹${booking.fare.toFixed(2)}</td>
                    <td>${booking.status}</td>
                    <td>${new Date(booking.createdAt).toLocaleString()}</td>
                </tr>
            `;
        });
        
        html += '</tbody></table>';
        document.getElementById('bookings').innerHTML = html;
    } catch (error) {
        showMessage('Error loading bookings', true);
    }
}

async function loadDrivers() {
    try {
        const response = await fetch('/api/admin/drivers');
        const drivers = await response.json();
        
        let html = '<table><thead><tr><th>ID</th><th>Name</th><th>Vehicle Type</th><th>License Plate</th><th>Available</th><th>Rating</th></tr></thead><tbody>';
        
        drivers.forEach(driver => {
            html += `
                <tr>
                    <td>${driver.id}</td>
                    <td>${driver.name}</td>
                    <td>${driver.vehicleType}</td>
                    <td>${driver.licensePlate}</td>
                    <td>${driver.available ? 'Yes' : 'No'}</td>
                    <td>${driver.rating.toFixed(1)}</td>
                </tr>
            `;
        });
        
        html += '</tbody></table>';
        document.getElementById('drivers').innerHTML = html;
    } catch (error) {
        showMessage('Error loading drivers', true);
    }
}

async function toggleUserStatus(userId) {
    try {
        const response = await fetch(`/api/admin/users/${userId}/toggle-status`, {
            method: 'POST'
        });
        
        const data = await response.json();
        
        if (data.success) {
            showMessage('User status updated');
            loadUsers();
        } else {
            showMessage('Error updating user status', true);
        }
    } catch (error) {
        showMessage('Network error', true);
    }
}

document.getElementById('logoutBtn').addEventListener('click', function(e) {
    e.preventDefault();
    fetch('/api/auth/logout', { method: 'POST' })
        .then(() => window.location.href = '/index.html');
});

loadUsers();
