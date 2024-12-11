// Function to fetch student data from the backend API
async function fetchStudentProfiles() {
    try {
        const response = await fetch('http://localhost:8080/api/v1/student/profiles');
        console.log(`Status Code: ${response.status}`);

        if (!response.ok) {
            console.error(`Error: ${response.status} - ${response.statusText}`);
            return;
        }

        const students = await response.json();

        const container = document.getElementById('profile-cards-container');

        // Loop through each student and create a profile card dynamically
        students.forEach(student => {
            const card = document.createElement('div');
            card.classList.add('col-md-4');
            card.innerHTML = `
                <div class="card profile-card shadow-sm" onclick="redirectToUpdateProfile('${student.id}')">
                    <img src="${student.profileImageBase64}" class="card-img-top" alt="${student.name}">
                    <div class="card-body text-center">
                        <h5 class="card-title">${student.name}</h5>
                        <p class="card-text text-muted">${student.email}</p>
                    </div>
                </div>
            `;
            container.appendChild(card);
        });

        console.log('Student profiles loaded successfully.');
    } catch (error) {
        console.error('Error fetching student profiles:', error);
    }
}

// Function to redirect to the update-profile page with the student ID as a query parameter
function redirectToUpdateProfile(studentId) {
    window.location.href = `/update-profile?studentId=${studentId}`;
}

// Call the function to fetch profiles when the page loads
window.onload = fetchStudentProfiles;

// Function to handle the Add button click
function addProfile() {
    window.location.href = '/create-profile';
}