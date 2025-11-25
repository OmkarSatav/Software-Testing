const uploadForm = document.getElementById('uploadForm');
const fileInput = document.getElementById('fileInput');
const messageDiv = document.getElementById('message');

const MAX_FILE_SIZE = 1024 * 1024;
const ALLOWED_TYPES = ['image/jpeg', 'image/jpg', 'image/png'];

function showMessage(message, isError = false) {
    messageDiv.textContent = message;
    messageDiv.className = isError ? 'message error' : 'message success';
    messageDiv.style.display = 'block';
}

function validateFile(file) {
    if (!file) {
        showMessage('Please select a file', true);
        return false;
    }
    
    if (!ALLOWED_TYPES.includes(file.type)) {
        showMessage('Only JPG and PNG files are allowed', true);
        return false;
    }
    
    if (file.size > MAX_FILE_SIZE) {
        showMessage('File size must be less than 1MB', true);
        return false;
    }
    
    return true;
}

fileInput.addEventListener('change', function() {
    const file = this.files[0];
    if (file && !validateFile(file)) {
        this.value = '';
    }
});

document.getElementById('logoutBtn').addEventListener('click', function(e) {
    e.preventDefault();
    fetch('/api/auth/logout', { method: 'POST' })
        .then(() => window.location.href = '/static/index.html');
});

uploadForm.addEventListener('submit', async function(e) {
    e.preventDefault();
    
    const file = fileInput.files[0];
    
    if (!validateFile(file)) {
        return;
    }
    
    const formData = new FormData();
    formData.append('file', file);
    
    try {
        const response = await fetch('/api/upload/id-proof', {
            method: 'POST',
            body: formData
        });
        
        const data = await response.json();
        
        if (data.success) {
            showMessage(data.message || 'File uploaded successfully!');
            uploadForm.reset();
        } else {
            showMessage(data.message || 'Upload failed', true);
        }
    } catch (error) {
        showMessage('Network error. Please try again.', true);
    }
});
