package org.example.sbuserprofileactivity.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudents() {
        return List.of(
                new Student(
                        1L,
                        "John Joseph Asoro",
                        "John Joseph",
                        "Asoro",
                        "johnjosephasoro215@gmail.com",
                        "PogiSiJJ",
                        "Male",
                        21,
                        "San Antonio, Bombon, Camarines Sur",
                        LocalDate.of(2024, Month.DECEMBER, 01),
                        "http://adfhiuef.com"
                )
        );
    }
}
