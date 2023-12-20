package com.example.educationalCourse.response;

import com.example.educationalCourse.model.Course;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {
    private Long id;
    private LocalDate courseStart;
    private LocalDate courseFinish;
    private String studentFullName;
    private String studentEmail;
    private int NumOfStudents;
    private String bookingConfirmationCode;
    private CourseResponse course;

    public BookingResponse(Long id, LocalDate courseStart, LocalDate courseFinish, String bookingConfirmationCode) {
        this.id = id;
        this.courseStart = courseStart;
        this.courseFinish = courseFinish;
        this.bookingConfirmationCode = bookingConfirmationCode;
    }
}
