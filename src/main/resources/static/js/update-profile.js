// Function to fetch student details by ID and populate the form
async function fetchAndPopulateStudentProfile() {
    const urlParams = new URLSearchParams(window.location.search);
    const studentId = urlParams.get('studentId');

    if (!studentId) {
        console.error('No student ID provided.');
        return;
    }

    try {
        const response = await fetch(`http://localhost:8080/api/v1/student/${studentId}`);
        console.log(`Status Code: ${response.status}`);

        if (response.ok) {
            console.log('HTTP GET request successful.');

            const student = await response.json();

            // Populate form fields with fetched data
            document.getElementById('firstName').value = student.firstName;
            document.getElementById('lastName').value = student.lastName;
            document.getElementById('email').value = student.email;
            document.getElementById('password').value = student.password;
            document.getElementById('gender').value = student.gender;
            document.getElementById('birthdate').value = student.birthdate;
            document.getElementById('address').value = student.address;

            // Display the profile image preview if available
            if (student.profileImageBase64) {
                const imagePreview = document.getElementById('profileImagePreview');
                imagePreview.src = student.profileImageBase64;
                imagePreview.classList.remove('d-none');
            }

            console.log('Student profile loaded successfully.');
        } else {
            console.error(`HTTP GET request failed with status ${response.status}: ${response.statusText}`);
            alert(`Failed to load student profile. Status: ${response.status} - ${response.statusText}`);
        }
    } catch (error) {
        console.error('Error loading student profile:', error);
        alert(`An error occurred while loading the student profile: ${error.message}`);
    }
}

// Function to update student profile using PUT request
async function updateStudentProfile(event) {
    event.preventDefault(); // Prevent the default form submission behavior

    const urlParams = new URLSearchParams(window.location.search);
    const studentId = urlParams.get('studentId');

    if (!studentId) {
        console.error('No student ID provided.');
        return;
    }

    const formData = new FormData();
    formData.append('firstName', document.getElementById('firstName').value);
    formData.append('lastName', document.getElementById('lastName').value);
    formData.append('email', document.getElementById('email').value);
    formData.append('password', document.getElementById('password').value);
    formData.append('gender', document.getElementById('gender').value);
    formData.append('birthdate', document.getElementById('birthdate').value);
    formData.append('address', document.getElementById('address').value);
    const profileImageFile = document.getElementById('profileImage').files[0];
    if (profileImageFile) {
        formData.append('profileImage', profileImageFile);
    }

    try {
        const response = await fetch(`http://localhost:8080/api/v1/student/${studentId}`, {
            method: 'PUT',
            body: formData
        });

        console.log(`Status Code: ${response.status}`);

        if (response.ok) {
            console.log('Profile updated successfully.');
            alert('Profile updated successfully!');
            window.location.href = '/student/profile'; // Redirect after successful update
        } else {
            console.error(`HTTP PUT request failed with status ${response.status}: ${response.statusText}`);
            alert(`Failed to update profile. Status: ${response.status} - ${response.statusText}`);
        }
    } catch (error) {
        console.error('Error updating student profile:', error);
        alert(`An error occurred while updating the profile: ${error.message}`);
    }
}

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

// Ensure the function is triggered on file input change
if (document.getElementById('profileImage')) {
    document.getElementById('profileImage').addEventListener('change', previewProfileImage);
}

// Call the function to fetch profile details when the page loads
window.onload = fetchAndPopulateStudentProfile;