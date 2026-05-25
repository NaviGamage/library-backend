package com.library.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ReservationResponse {

    private Long id;
    private String bookTitle;
    private String bookAuthor;
    private String userName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer durationDays;
    private LocalDateTime createdAt;
}