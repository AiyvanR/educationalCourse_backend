package com.example.educationalCourse.service;


import com.example.educationalCourse.exception.InternalServerException;
import com.example.educationalCourse.exception.ResourceNotFoundException;
import com.example.educationalCourse.model.Course;
import com.example.educationalCourse.repository.CourseRepository;
import com.example.educationalCourse.response.CourseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    @Override
    public Course addNewCourse(MultipartFile photo, String courseType, BigDecimal coursePrice) throws SQLException, IOException {
        Course course = new Course();
        course.setCourseType(courseType);
        course.setCoursePrice(coursePrice);
        if(!photo.isEmpty()){
            byte[] photoByte = photo.getBytes();
            Blob photoBlob = new SerialBlob(photoByte);
            course.setPhoto(photoBlob);
        }
        return courseRepository.save(course);
    }

    @Override
    public List<String> getAllCourseTypes() {
        return courseRepository.findDistinctCourseTypes();
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public byte[] getCoursePhotoById(Long courseId) throws SQLException {
        Optional<Course> theCourse = courseRepository.findById(courseId);
        if(theCourse.isEmpty()){
            throw new ResourceNotFoundException("Sorry, Course not found!");
        }
        Blob photoBlob = theCourse.get().getPhoto();
        if(photoBlob != null){
            return photoBlob.getBytes(1,(int) photoBlob.length());
        }
        return null;
    }

    @Override
    public void deleteCourse(Long courseId) {
        Optional<Course> theCourse = courseRepository.findById(courseId);
        if(theCourse.isPresent()){
            courseRepository.deleteById(courseId);
        }
    }

    @Override
    public Course updateCourse(Long courseId, String courseType, BigDecimal coursePrice, byte[] photoByte) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        if(courseType != null) course.setCourseType(courseType);
        if(coursePrice!= null) course.setCoursePrice(coursePrice);
        if(photoByte != null && photoByte.length > 0){
            try {
                course.setPhoto(new SerialBlob(photoByte));
            }catch (SQLException e) {
                throw new InternalServerException("Error updating course");
            }
        }
            return courseRepository.save(course);
    }


    @Override
    public Optional<Course> getCourseById(Long courseId) {
        return Optional.of(courseRepository.findById(courseId).get());
    }
}
