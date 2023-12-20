package com.example.educationalCourse.repository;

import com.example.educationalCourse.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("SELECT DISTINCT c.courseType FROM Course c")
    List<String> findDistinctCourseTypes();
}
