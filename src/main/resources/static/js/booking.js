const bookingForm = document.getElementById('bookingForm');
const messageDiv = document.getElementById('message');
const pickupTimeInput = document.getElementById('pickupTime');
const passengersInput = document.getElementById('passengers');
const estimatedFareInput = document.getElementById('estimatedFare');
const bookBtn = document.getElementById('bookBtn');

const EARTH_RADIUS = 6371;
const BASE_FARE = 50;
const RATE_PER_KM = 12;
const PEAK_MULTIPLIER = 1.5;
const MAX_PASSENGERS = 4;
const MAX_SERVICE_RADIUS = 50;

function showMessage(message, isError = false) {
    messageDiv.textContent = message;
    messageDiv.className = isError ? 'message error' : 'message success';
    messageDiv.style.display = 'block';
}

function setMinDateTime() {
    const now = new Date();
    now.setMinutes(now.getMinutes() + 30);
    const minDateTime = now.toISOString().slice(0, 16);
    pickupTimeInput.setAttribute('min', minDateTime);
    pickupTimeInput.value = minDateTime;
}

function calculateDistance(lat1, lon1, lat2, lon2) {
    const dLat = (lat2 - lat1) * Math.PI / 180;
    const dLon = (lon2 - lon1) * Math.PI / 180;
    
    const a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
              Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
              Math.sin(dLon / 2) * Math.sin(dLon / 2);
    
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return EARTH_RADIUS * c;
}

function isPeakHour(dateTime) {
    const hour = dateTime.getHours();
    return (hour >= 8 && hour <= 10) || (hour >= 17 && hour <= 19);
}

function calculateFare() {
    const pickupLat = parseFloat(document.getElementById('pickupLat').value);
    const pickupLon = parseFloat(document.getElementById('pickupLon').value);
    const dropLat = parseFloat(document.getElementById('dropLat').value);
    const dropLon = parseFloat(document.getElementById('dropLon').value);
    const pickupTime = new Date(pickupTimeInput.value);
    
    if (isNaN(pickupLat) || isNaN(pickupLon) || isNaN(dropLat) || isNaN(dropLon)) {
        estimatedFareInput.value = 'Enter valid coordinates';
        return;
    }
    
    const distance = calculateDistance(pickupLat, pickupLon, dropLat, dropLon);
    
    if (distance > MAX_SERVICE_RADIUS) {
        estimatedFareInput.value = 'Outside service area';
        bookBtn.disabled = true;
        return;
    }
    
    let fare = BASE_FARE + (distance * RATE_PER_KM);
    
    if (isPeakHour(pickupTime)) {
        fare *= PEAK_MULTIPLIER;
    }
    
    estimatedFareInput.value = `₹${fare.toFixed(2)} (${distance.toFixed(2)} km)`;
    bookBtn.disabled = false;
}

function validateBookingForm() {
    const pickupTime = new Date(pickupTimeInput.value);
    const now = new Date();
    
    if (pickupTime <= now) {
        showMessage('Pickup time must be in the future', true);
        return false;
    }
    
    const passengers = parseInt(passengersInput.value);
    if (passengers < 1 || passengers > MAX_PASSENGERS) {
        showMessage(`Passengers must be between 1 and ${MAX_PASSENGERS}`, true);
        return false;
    }
    
    const vehicleType = document.getElementById('vehicleType').value;
    if (!vehicleType) {
        showMessage('Please select a vehicle type', true);
        return false;
    }
    
    const paymentMode = document.getElementById('paymentMode').value;
    if (!paymentMode) {
        showMessage('Please select a payment mode', true);
        return false;
    }
    
    return true;
}

setMinDateTime();

document.querySelectorAll('#pickupLat, #pickupLon, #dropLat, #dropLon, #pickupTime').forEach(input => {
    input.addEventListener('input', calculateFare);
});

passengersInput.addEventListener('input', function() {
    const value = parseInt(this.value);
    if (value > MAX_PASSENGERS) {
        this.value = MAX_PASSENGERS;
        showMessage(`Maximum ${MAX_PASSENGERS} passengers allowed`, true);
    } else if (value < 1) {
        this.value = 1;
    }
});

document.getElementById('logoutBtn').addEventListener('click', function(e) {
    e.preventDefault();
    fetch('/api/auth/logout', { method: 'POST' })
        .then(() => window.location.href = '/index.html');
});
bookingForm.addEventListener('submit', async function(e) {
    e.preventDefault();
    
    if (!validateBookingForm()) {
        return;
    }
    
    const bookingData = {
        pickupLatitude: parseFloat(document.getElementById('pickupLat').value),
        pickupLongitude: parseFloat(document.getElementById('pickupLon').value),
        dropLatitude: parseFloat(document.getElementById('dropLat').value),
        dropLongitude: parseFloat(document.getElementById('dropLon').value),
        pickupTime: new Date(pickupTimeInput.value).toISOString(),
        passengers: parseInt(passengersInput.value),
        vehicleType: document.getElementById('vehicleType').value,
        paymentMode: document.getElementById('paymentMode').value
    };
    
    bookBtn.disabled = true;
    bookBtn.textContent = 'Booking...';
    
    try {
        const response = await fetch('/api/bookings/create', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(bookingData)
        });
        
        const data = await response.json();
        
        if (data.success) {
                showMessage(`Booking successful! Booking ID: ${data.bookingId}, Fare: ₹${data.fare}`);
                setTimeout(() => {
                    window.location.href = '/pages/bookings.html';
                }, 2000);
        } else {
            showMessage(data.message || 'Booking failed', true);
            bookBtn.disabled = false;
            bookBtn.textContent = 'Book Now';
        }
    } catch (error) {
        showMessage('Network error. Please try again.', true);
        bookBtn.disabled = false;
        bookBtn.textContent = 'Book Now';
    }
});

calculateFare();

