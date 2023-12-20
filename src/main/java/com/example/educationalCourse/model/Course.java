package com.example.educationalCourse.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.yaml.snakeyaml.events.Event;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Entity
@Data
@AllArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String courseType;
    private BigDecimal coursePrice;
    private boolean isBooked = false;

    @Lob
    private Blob photo;
    @OneToMany(mappedBy ="course", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<BookedCourse> bookings;

    public Course() {
        this.bookings = new ArrayList<>();
    }


    public void addBookings(BookedCourse booking){
        if(booking == null){
            bookings = new ArrayList<>();
        }

        bookings.add(booking);
        booking.setCourse(this);
        isBooked = true;
        String bookingCode = RandomStringUtils.randomNumeric(10);
        booking.setBookingConfirmationCode(bookingCode);
    }
}




