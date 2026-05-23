package com.library.dto;

import com.library.model.BookStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BookRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @NotBlank(message = "Author is required")
    @Size(max = 150, message = "Author must not exceed 150 characters")
    private String author;

    @Size(max = 100, message = "Genre must not exceed 100 characters")
    private String genre;

    @Size(max = 50, message = "Language must not exceed 50 characters")
    private String language;

    private BookStatus status;

    private String coverImage;

    private Long categoryId;
}