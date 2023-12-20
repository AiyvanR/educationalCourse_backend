package com.example.educationalCourse.controller;

import com.example.educationalCourse.exception.PhotoRetrievalException;
import com.example.educationalCourse.exception.ResourceNotFoundException;
import com.example.educationalCourse.model.BookedCourse;
import com.example.educationalCourse.model.Course;
import com.example.educationalCourse.response.BookingResponse;
import com.example.educationalCourse.response.CourseResponse;
import com.example.educationalCourse.service.BookedCourseServiceImpl;
import com.example.educationalCourse.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;
    private final BookedCourseServiceImpl bookedCourseService;

    @PostMapping("/add/new-course")
    public ResponseEntity<CourseResponse> addNewCourse(
            @RequestParam("photo") MultipartFile photo,
            @RequestParam("courseType")String courseType,
            @RequestParam("coursePrice") BigDecimal coursePrice) throws SQLException, IOException {
        Course savedCourse = courseService.addNewCourse(photo, courseType,coursePrice);
        CourseResponse response = new CourseResponse(savedCourse.getId(),savedCourse.getCourseType(),savedCourse.getCoursePrice());
        return ResponseEntity.ok(response);
    }


    @GetMapping("/course/types")
    public List<String> getCourseTypes(){
        return courseService.getAllCourseTypes();
    }

    @GetMapping("/all-courses")
    public ResponseEntity<List<CourseResponse>> getAllCourses() throws SQLException {
        List<Course> courses = courseService.getAllCourses();
        List<CourseResponse> courseResponses = new ArrayList<>();
        for(Course course: courses){
            byte[] photoByte = courseService.getCoursePhotoById(course.getId());
            if (photoByte != null && photoByte.length > 0){
                String base64Photo = Base64.encodeBase64String(photoByte);
                CourseResponse courseResponse = getCourseResponse(course);
                courseResponse.setPhoto(base64Photo);
                courseResponses.add(courseResponse);
            }
        }
        return ResponseEntity.ok(courseResponses);
    }

    @DeleteMapping("/delete/course/{courseId}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long courseId){
        courseService.deleteCourse(courseId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
     }

    @PutMapping("/update/{courseId}")
     public ResponseEntity<CourseResponse> updateCourse(@PathVariable Long courseId,
                                                        @RequestParam(required = false) String courseType,
                                                        @RequestParam(required = false)BigDecimal coursePrice,
                                                        @RequestParam(required = false)MultipartFile photo) throws SQLException, IOException {
        byte[] photoByte = photo != null &&  !photo.isEmpty() ?
                photo.getBytes() : courseService.getCoursePhotoById(courseId);
        Blob photoBlob = photoByte != null && photoByte.length>0 ? new SerialBlob(photoByte) : null;
        Course theCourse = courseService.updateCourse(courseId,courseType,coursePrice,photoByte);
        theCourse.setPhoto(photoBlob);
        CourseResponse courseResponse = getCourseResponse(theCourse);
        return ResponseEntity.ok(courseResponse);
     }

    @GetMapping("/course/{courseId}")
     public ResponseEntity<Optional<CourseResponse>> getCourseById(@PathVariable Long courseId){
        Optional<Course> theCourse = courseService.getCourseById(courseId);
        return theCourse.map(course -> {
            CourseResponse courseResponse = getCourseResponse(course);
            return ResponseEntity.ok(Optional.of(courseResponse));
        }).orElseThrow(() -> new ResourceNotFoundException("Course is not found"));
     }

    private CourseResponse getCourseResponse(Course course) {
        List<BookedCourse> bookings = getAllBookingsByCourseId(course.getId());
//        List<BookingResponse> bookingInfo = bookings.stream()
//                .map(booking -> new BookingResponse(booking.getBookingId(), booking.getCourseStart(),
//                        booking.getCourseFinish(),
//                        booking.getBookingConfirmationCode())).toList();
        byte[] photoBytes = null;
        Blob photoBlob = course.getPhoto();
        if(photoBlob != null){
            try {
                photoBytes =photoBlob.getBytes(1,(int) photoBlob.length());
            }catch (SQLException e) {
                throw new PhotoRetrievalException("Error retreiving photo");
            }
        }
        return new CourseResponse(course.getId(),course.getCourseType(),course.getCoursePrice()
                ,course.isBooked(),photoBytes);
    }

    private List<BookedCourse> getAllBookingsByCourseId(Long courseId) {
        return bookedCourseService.getAllBookingsByCourseId(courseId);
    }
}
