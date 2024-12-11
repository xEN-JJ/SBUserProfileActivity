package org.example.sbuserprofileactivity.student.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.sql.Blob;

@Entity
@Table(name = "student")
public class Student {
    @Id
    @SequenceGenerator(
            name = "student_sequence",
            sequenceName = "student_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "student_sequence"
    )
    private Long id;
    private String name;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String gender;
    @Transient
    private Integer age;
    private String address;
    private LocalDate birthdate;

    @Lob
    @Column(name = "profile_image")
    private Blob profileImage;
    @Transient
    private String profileImageBase64;

    public Student() {
    }

    public Student(Long id, String firstName, String lastName, String email, String password, String gender, String address, LocalDate birthdate, Blob profileImage) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.name = firstName + " " + lastName; // Concatenate firstName and lastName
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.address = address;
        this.birthdate = birthdate;
        this.profileImage = profileImage;
    }

    public Student(String firstName, String lastName, String email, String password, String gender, String address, LocalDate birthdate, Blob profileImage) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.name = firstName + " " + lastName; // Concatenate firstName and lastName
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.address = address;
        this.birthdate = birthdate;
        this.profileImage = profileImage;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        this.setName(this.firstName + " " + this.lastName); // Update name
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        this.setName(this.firstName + " " + this.lastName); // Update name
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return Period.between(this.birthdate, LocalDate.now()).getYears();
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public Blob getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Blob profileImage) {
        this.profileImage = profileImage;
    }

    public String getProfileImageBase64() {
        return profileImageBase64;
    }

    public void setProfileImageBase64(String profileImageBase64) {
        this.profileImageBase64 = profileImageBase64;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", address='" + address + '\'' +
                ", birthdate=" + birthdate +
                ", profileImage='" + profileImage + '\'' +
                '}';
    }
}