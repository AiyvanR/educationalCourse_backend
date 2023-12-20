package com.example.educationalCourse.repository;

import com.example.educationalCourse.model.BookedCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<BookedCourse, Long> {

    BookedCourse findByBookingConfirmationCode(String confirmationCode);
    List<BookedCourse> findByCourseId(Long courseId);
}
