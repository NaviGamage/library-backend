package com.library.dto;

import com.library.model.BookStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BookResponse {

    private Long id;
    private String title;
    private String author;
    private String genre;
    private String language;
    private BookStatus status;
    private String coverImage;
    private String categoryName;
    private LocalDateTime createdAt;
}