package org.example.sbuserprofileactivity.student.controller;

import org.example.sbuserprofileactivity.student.service.StudentService;
import org.example.sbuserprofileactivity.student.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import javax.sql.rowset.serial.SerialBlob;

@RestController
@RequestMapping(path = "api/v1/student")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/profiles")
    public ResponseEntity<List<Student>> getStudentsWithProfiles() {
        List<Student> students = studentService.getStudents();
        students.forEach(student -> {
            try {
                String profileImageBase64 = studentService.getProfileImage(student.getId());
                student.setProfileImageBase64(profileImageBase64);
                student.setProfileImage(null); // Avoid sending the Blob directly
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return ResponseEntity.ok(students);
    }

    @PostMapping("/register")
    public ResponseEntity<Student> registerNewStudent(@RequestParam("firstName") String firstName,
                                                      @RequestParam("lastName") String lastName,
                                                      @RequestParam("email") String email,
                                                      @RequestParam("password") String password,
                                                      @RequestParam("gender") String gender,
                                                      @RequestParam("birthdate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthdate,
                                                      @RequestParam("address") String address,
                                                      @RequestParam("profileImage") MultipartFile profileImage) {
        try {
            byte[] profileImageBytes = profileImage.getBytes();
            Blob profileImageBlob = new SerialBlob(profileImageBytes);

            String name = firstName + " " + lastName;
            Student student = new Student();
            student.setFirstName(firstName);
            student.setLastName(lastName);
            student.setName(name);
            student.setEmail(email);
            student.setPassword(password);
            student.setGender(gender);
            student.setBirthdate(birthdate);
            student.setAddress(address);
            student.setProfileImage(profileImageBlob);

            Student savedStudent = studentService.addNewStudent(student);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedStudent);
        } catch (IOException | SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping(path = "{studentId}")
    public void deleteStudent(@PathVariable("studentId") Long studentId) {
        studentService.deleteStudent(studentId);
    }

    @PutMapping(path = "{studentId}")
    public ResponseEntity<?> updateStudent(
            @PathVariable("studentId") Long studentId,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthdate,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) MultipartFile profileImage) {
        try {
            studentService.updateStudent(studentId, firstName, lastName, email, password, gender, birthdate, address, profileImage);
            return ResponseEntity.ok("Student profile updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update student profile: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Student student) {
        Optional<Student> foundStudent = studentService.authenticateStudent(student.getEmail(), student.getPassword());

        if (foundStudent.isPresent()) {
            return ResponseEntity.ok().body("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long studentId) {
        Optional<Student> student = studentService.getStudentWithImageById(studentId);
        return student.map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

}

@Controller
class ViewController {

    @GetMapping("/login")
    public String loginPage() {
        return "structure/login"; // This maps to src/main/resources/templates/structure/login.html
    }

    @GetMapping("/create-profile")
    public String createProfilePage() {
        return "structure/create-profile"; // This maps to src/main/resources/templates/structure/create-profile.html
    }

    @GetMapping("/update-profile")
    public String updateProfilePage() {
        return "structure/update-profile"; // This maps to src/main/resources/templates/structure/update-profile.html
    }

    @GetMapping("/student/profile")
    public String profilePage() {
        return "structure/profile"; // This maps to src/main/resources/templates/structure/profile.html
    }
}
