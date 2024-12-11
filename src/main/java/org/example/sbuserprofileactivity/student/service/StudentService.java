package org.example.sbuserprofileactivity.student.service;

import jakarta.transaction.Transactional;
import org.example.sbuserprofileactivity.student.model.Student;
import org.example.sbuserprofileactivity.student.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.sql.DataSource;
import javax.sql.rowset.serial.SerialBlob;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final DataSource dataSource;

    @Autowired
    public StudentService(StudentRepository studentRepository, DataSource dataSource) {
        this.studentRepository = studentRepository;
        this.dataSource = dataSource;
    }

    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    public Student addNewStudent(Student student) {
        Optional<Student> studentOptional = findStudentByEmail(student.getEmail());
        if (studentOptional.isPresent()) {
            throw new IllegalStateException("Student email already exists");
        }
        return studentRepository.save(student);
    }

    public Optional<Student> findStudentByEmail(String email) {
        return studentRepository.findStudentByEmail(email).stream().findFirst();
    }

    public Optional<Student> authenticateStudent(String email, String password) {
        Optional<Student> studentOptional = findStudentByEmail(email);
        if (studentOptional.isPresent() && studentOptional.get().getPassword().equals(password)) {
            return studentOptional;
        }
        return Optional.empty();
    }

    public void deleteStudent(Long studentId) {
        boolean idExists = studentRepository.existsById(studentId);
        if (!idExists) {
            throw new IllegalStateException("Student ID: " + studentId + " not found");
        }
        studentRepository.deleteById(studentId);
    }

    @Transactional
    public Optional<Student> getStudentWithImageById(Long studentId) {
        Optional<Student> studentOptional = studentRepository.findById(studentId);

        studentOptional.ifPresent(student -> {
            Blob blob = student.getProfileImage();
            if (blob != null) {
                try (InputStream inputStream = blob.getBinaryStream();
                     ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }

                    String profileImageBase64 = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(outputStream.toByteArray());
                    student.setProfileImageBase64(profileImageBase64);
                    student.setProfileImage(null); // Avoid sending the Blob directly

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return studentOptional;
    }

    @Transactional
    public void updateStudent(Long studentId,
                              String firstName,
                              String lastName,
                              String email,
                              String password,
                              String gender,
                              LocalDate birthdate,
                              String address,
                              MultipartFile profileImage) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalStateException("Student ID " + studentId + " not found"));

        if (firstName != null && !firstName.isEmpty()) {
            student.setFirstName(firstName);
        }

        if (lastName != null && !lastName.isEmpty()) {
            student.setLastName(lastName);
        }

        if (email != null && !email.isEmpty()) {
            Optional<Student> studentOptional = findStudentByEmail(email);
            if (studentOptional.isPresent() && !studentOptional.get().getId().equals(studentId)) {
                throw new IllegalStateException("Student email already exists");
            }
            student.setEmail(email);
        }

        if (password != null && !password.isEmpty()) {
            student.setPassword(password);
        }

        if (gender != null && !gender.isEmpty()) {
            student.setGender(gender);
        }

        if (birthdate != null) {
            student.setBirthdate(birthdate);
        }

        if (address != null && !address.isEmpty()) {
            student.setAddress(address);
        }

        // Handle profile image update
        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                Blob profileImageBlob = new SerialBlob(profileImage.getBytes());
                student.setProfileImage(profileImageBlob);
            } catch (Exception e) {
                throw new IllegalStateException("Failed to update profile image", e);
            }
        }
        // Update full name
        student.setName(student.getFirstName() + " " + student.getLastName());
    }

    public void saveProfileImage(Long studentId, byte[] imageData) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalStateException(
                        "Student ID " + studentId + " not found")
                );
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            Blob blob = connection.createBlob();
            blob.setBytes(1, imageData);
            student.setProfileImage(blob);
            studentRepository.save(student);
            connection.commit();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to save profile image", e);
        }
    }

    @Transactional
    public String getProfileImage(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalStateException(
                        "Student ID " + studentId + " not found")
                );

        Blob blob = student.getProfileImage();
        if (blob == null) {
            // Log a warning and return a default image or message
            System.out.println("Warning: Profile image not found for student ID " + studentId);
            return "data:image/png;base64," + getDefaultProfileImageBase64();
        }

        try (InputStream inputStream = blob.getBinaryStream();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Failed to retrieve profile image", e);
        }
    }

    private String getDefaultProfileImageBase64() {
        // Provide a default placeholder image as Base64
        // This is a simple transparent 1x1 PNG image encoded in Base64
        return "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAIAAACQd1PeAAAAEElEQVR42mP8z8DwHwAFgQIAhux6GgAAAABJRU5ErkJggg==";
    }
}