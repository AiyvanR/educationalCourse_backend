package com.example.educationalCourse.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.List;


@Data
@NoArgsConstructor

public class CourseResponse {
    private Long id;
    private String courseType;
    private BigDecimal coursePrice;
    private boolean isBooked;
    private String photo;
    private List<BookingResponse> bookings;

    public CourseResponse(Long id, String courseType, BigDecimal coursePrice) {
        this.id = id;
        this.courseType = courseType;
        this.coursePrice = coursePrice;
    }

    public CourseResponse(Long id, String courseType, BigDecimal coursePrice, boolean isBooked,
                          byte[] photoBytes) {
        this.id = id;
        this.courseType = courseType;
        this.coursePrice = coursePrice;
        this.isBooked = isBooked;
        this.photo = photoBytes != null ? Base64.encodeBase64String(photoBytes) : null;
//        this.bookings = bookings;
    }



}
