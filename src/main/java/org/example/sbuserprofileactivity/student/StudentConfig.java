package org.example.sbuserprofileactivity.student;

import org.example.sbuserprofileactivity.student.model.Student;
import org.example.sbuserprofileactivity.student.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.rowset.serial.SerialBlob;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Configuration
public class StudentConfig {

    private static final Logger LOGGER = Logger.getLogger(StudentConfig.class.getName());

    @Bean
    CommandLineRunner commandLineRunner(StudentRepository repository) {
        return args -> {
            try {
                // Load images as byte arrays
                Path jjImagePath = Paths.get("src/main/resources/static/images/JJ.jpg");
                byte[] jjImageBytes = Files.readAllBytes(jjImagePath);
                SerialBlob jjImageBlob = new SerialBlob(jjImageBytes);

                Path giuseppeImagePath = Paths.get("src/main/resources/static/images/Giuseppe.jpg");
                byte[] giuseppeImageBytes = Files.readAllBytes(giuseppeImagePath);
                SerialBlob giuseppeImageBlob = new SerialBlob(giuseppeImageBytes);

                // Create student objects with image blobs
                Student JJ = new Student(
                        "John Joseph",
                        "Asoro",
                        "johnjosephasoro215@gmail.com",
                        "PogiSiJJ",
                        "Male",
                        "San Antonio, Bombon, Camarines Sur",
                        LocalDate.of(2003, Month.DECEMBER, 1),
                        jjImageBlob
                );

                Student Giuseppe = new Student(
                        "Giuseppe Marie",
                        "Archivido",
                        "giuseppearchivido12@gmail.com",
                        "PoginiSeppe",
                        "Male",
                        "San Felipe, Camaligan, Camarines Sur",
                        LocalDate.of(2004, Month.SEPTEMBER, 12),
                        giuseppeImageBlob
                );

                // Save students to repository
                repository.saveAll(List.of(JJ, Giuseppe));
                LOGGER.info("Students initialized successfully with profile images.");
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error initializing students with profile images", e);
            }
        };
    }
}