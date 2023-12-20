package com.example.educationalCourse.service;

import com.example.educationalCourse.model.BookedCourse;

import java.util.List;

public interface BookedCourseService {
    void cancelBooking(Long bookingId);

    String saveBooking(Long courseId, BookedCourse bookingRequest);

    BookedCourse findByBookingConfirmationCode(String confirmationCode);

    List<BookedCourse> getAllBookings();
}
