package com.library.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReservationRequest {

    @NotNull(message = "Book ID is required")
    private Long bookId;

    @NotNull(message = "Duration is required")
    private Integer durationDays;
}