package com.example.educationalCourse.controller;

import com.example.educationalCourse.exception.InvalidBookingRequestException;
import com.example.educationalCourse.exception.ResourceNotFoundException;
import com.example.educationalCourse.model.BookedCourse;
import com.example.educationalCourse.model.Course;
import com.example.educationalCourse.response.BookingResponse;
import com.example.educationalCourse.response.CourseResponse;
import com.example.educationalCourse.service.BookedCourseService;
import com.example.educationalCourse.service.BookedCourseServiceImpl;
import com.example.educationalCourse.service.CourseService;
import com.example.educationalCourse.service.CourseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/bookings")
public class BookingController {

    private final BookedCourseService bookedCourseService;
    private final CourseService courseService;

    @GetMapping("/all-bookings")
    public ResponseEntity<List<BookingResponse>> getAll(){
        List<BookedCourse> bookings = bookedCourseService.getAllBookings();
        List<BookingResponse> bookingResponses = new ArrayList<>();

        for(BookedCourse booking : bookings){
            BookingResponse bookingResponse= getBookingResponse(booking);
            bookingResponses.add(bookingResponse);
        }
        return ResponseEntity.ok(bookingResponses);
    }

    @GetMapping("/confirmation/{confirmationCode}")
    public ResponseEntity<?> getBookingByConfirmationCode(@PathVariable String confirmationCode){
        try {
            BookedCourse booking = bookedCourseService.findByBookingConfirmationCode(confirmationCode);
            BookingResponse bookingResponse = getBookingResponse(booking);
            return ResponseEntity.ok(bookingResponse);
        }catch (ResourceNotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }


    @PostMapping("/course/{courseId}/booking")
    public ResponseEntity<?> saveBooking(@PathVariable Long courseId,@RequestBody BookedCourse bookingRequest){
        try {
            String confirmationCode =bookedCourseService.saveBooking(courseId,bookingRequest);
            return ResponseEntity.ok("Course Booked successfully. Your Confirmation Code :"+ confirmationCode);
        }catch (InvalidBookingRequestException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @DeleteMapping("/booking/{bookingId}/delete")
    public void cancelBooking(@PathVariable Long bookingId){
        bookedCourseService.cancelBooking(bookingId);
    }



    private BookingResponse getBookingResponse(BookedCourse booking) {

        Course theCourse = courseService.getCourseById(booking.getCourse().getId()).get();
        CourseResponse course = new CourseResponse(theCourse.getId(),
                theCourse.getCourseType(),
                theCourse.getCoursePrice());
        return new BookingResponse(booking.getBookingId(),booking.getCourseStart(),booking.getCourseFinish(),booking.getStudentFullName(),
                booking.getStudentEmail(),booking.getNumOfStudents(),booking.getBookingConfirmationCode(),course);
    }
}
