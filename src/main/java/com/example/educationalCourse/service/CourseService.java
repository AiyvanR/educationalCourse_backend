package com.example.educationalCourse.service;

import com.example.educationalCourse.model.Course;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CourseService {

    Course addNewCourse(MultipartFile photo, String courseType, BigDecimal coursePrice) throws SQLException, IOException;

    List<String> getAllCourseTypes();

    List<Course> getAllCourses();

    byte[] getCoursePhotoById(Long courseId) throws SQLException;

    void deleteCourse(Long courseId);

    Course updateCourse(Long courseId, String courseType, BigDecimal coursePrice, byte[] photoByte);

    Optional<Course> getCourseById(Long courseId);
}
