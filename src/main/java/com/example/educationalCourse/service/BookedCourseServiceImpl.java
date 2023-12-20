package com.example.educationalCourse.service;


import com.example.educationalCourse.exception.InvalidBookingRequestException;
import com.example.educationalCourse.model.BookedCourse;
import com.example.educationalCourse.model.Course;
import com.example.educationalCourse.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookedCourseServiceImpl implements BookedCourseService{

    private final BookingRepository bookingRepository;
    private final CourseService courseService;

    public List<BookedCourse> getAllBookingsByCourseId(Long courseId) {
        return bookingRepository.findByCourseId(courseId);
    }

    @Override
    public void cancelBooking(Long bookingId) {
        bookingRepository.deleteById(bookingId);
    }

    @Override
    public String saveBooking(Long courseId, BookedCourse bookingRequest) {
        if(bookingRequest.getCourseFinish().isBefore(bookingRequest.getCourseStart())){
            throw new InvalidBookingRequestException("Course Start Date must be before Course Finish Date");
        }
        Course course = courseService.getCourseById(courseId).get();
        List<BookedCourse> existingBookings= course.getBookings();
        boolean courseIsAvailable = courseIsAvailable(bookingRequest,existingBookings);
        if(courseIsAvailable){
            course.addBookings(bookingRequest);
            bookingRepository.save(bookingRequest);
        }else{
            throw new InvalidBookingRequestException("Sorry, This course is not available for selected date");
        }
        return bookingRequest.getBookingConfirmationCode();
    }



    @Override
    public BookedCourse findByBookingConfirmationCode(String confirmationCode) {
        return bookingRepository.findByBookingConfirmationCode(confirmationCode);
        }

    @Override
    public List<BookedCourse> getAllBookings() {
        return bookingRepository.findAll();
    }


    private boolean courseIsAvailable(BookedCourse bookingRequest, List<BookedCourse> existingBookings) {
        return existingBookings.stream()
                .noneMatch(existingBooking ->
                        bookingRequest.getCourseStart().equals(existingBooking.getCourseStart())
                                || bookingRequest.getCourseFinish().isBefore(existingBooking.getCourseFinish())
                                || (bookingRequest.getCourseStart().isAfter(existingBooking.getCourseStart())
                                && bookingRequest.getCourseStart().isBefore(existingBooking.getCourseFinish()))
                                || (bookingRequest.getCourseStart().isBefore(existingBooking.getCourseStart())

                                && bookingRequest.getCourseFinish().equals(existingBooking.getCourseFinish()))
                                || (bookingRequest.getCourseStart().isBefore(existingBooking.getCourseStart())

                                && bookingRequest.getCourseFinish().isAfter(existingBooking.getCourseFinish()))

                                || (bookingRequest.getCourseStart().equals(existingBooking.getCourseFinish())
                                && bookingRequest.getCourseFinish().equals(existingBooking.getCourseStart()))

                                || (bookingRequest.getCourseStart().equals(existingBooking.getCourseFinish())
                                && bookingRequest.getCourseFinish().equals(bookingRequest.getCourseStart()))
                );
    }
}
