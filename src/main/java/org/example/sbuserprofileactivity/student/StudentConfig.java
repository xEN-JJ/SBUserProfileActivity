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
                Path fireWriter = Paths.get("src/main/resources/static/images/FireWriting.jpg");
                byte[] fireWriterBytes = Files.readAllBytes(fireWriter);
                SerialBlob fireWriterImageBlob = new SerialBlob(fireWriterBytes);

                Path eggHeadPath = Paths.get("src/main/resources/static/images/egghead.jpg");
                byte[] eggheadBytes = Files.readAllBytes(eggHeadPath);
                SerialBlob eggHeadImageBlob = new SerialBlob(eggheadBytes);

                // Create student objects with image blobs
                Student fireWriten = new Student(
                        "John Writer",
                        "Publisher",
                        "johnwriterpublisher@gmail.com",
                        "SHEESH",
                        "Male",
                        "San Jose, Pili, Camarines Sur",
                        LocalDate.of(2003, Month.DECEMBER, 1),
                        fireWriterImageBlob
                );

                Student eggHead = new Student(
                        "Egg Head",
                        "Hero",
                        "eggheadhero@gmail.com",
                        "EGG",
                        "Male",
                        "San Felipe, Legazpi, Camarines Sur",
                        LocalDate.of(2004, Month.SEPTEMBER, 12),
                        eggHeadImageBlob
                );

                // Save students to repository
                repository.saveAll(List.of(fireWriten, eggHead));
                LOGGER.info("Students initialized successfully with profile images.");
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error initializing students with profile images", e);
            }
        };
    }
}