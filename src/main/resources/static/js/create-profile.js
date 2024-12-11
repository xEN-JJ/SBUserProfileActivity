// Function to preview the selected profile image
function previewProfileImage(event) {
    const reader = new FileReader();
    reader.onload = function() {
        const imageElement = document.getElementById('profileImagePreview');
        imageElement.src = reader.result;
        imageElement.classList.remove('d-none');
    };
    reader.readAsDataURL(event.target.files[0]);
}

// Function to handle form submission for creating a new profile
async function handleProfileFormSubmit(event) {
    event.preventDefault(); // Prevent the default form submission behavior

    // Get the image file
    const profileImage = document.getElementById('profileImage').files[0];

    const formData = new FormData();
    formData.append('firstName', document.getElementById('firstName').value);
    formData.append('lastName', document.getElementById('lastName').value);
    formData.append('email', document.getElementById('email').value);
    formData.append('password', document.getElementById('password').value);
    formData.append('gender', document.getElementById('gender').value);
    formData.append('birthdate', document.getElementById('birthdate').value);
    formData.append('address', document.getElementById('address').value);
    formData.append('profileImage', profileImage);

    try {
        const response = await fetch('/api/v1/student/register', {
            method: 'POST',
            body: formData
        });

        // Log the response status code
        console.log(`Status Code: ${response.status}`);

        if (response.ok) {
            console.log('Profile created successfully!');
            alert('Profile added successfully!'); // Show success alert
            window.location.href = '/student/profile'; // Redirect to profile.html
        } else {
            console.error(`Error: ${response.status} - ${response.statusText}`);
            alert(`Error creating new profile: ${response.statusText}`); // Show error alert with status message
        }
    } catch (error) {
        console.error('Error creating new profile:', error);
        alert('Error creating new profile.'); // Show error alert
    }
}

// Ensure the function is triggered on form submission
if (document.getElementById('profile-form')) {
    document.getElementById('profile-form').addEventListener('submit', handleProfileFormSubmit);
}

// Ensure the function is triggered on file input change
if (document.getElementById('profileImage')) {
    document.getElementById('profileImage').addEventListener('change', previewProfileImage);
}