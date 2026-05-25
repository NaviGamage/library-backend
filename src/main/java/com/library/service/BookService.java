package com.library.service;

import com.library.dto.BookRequest;
import com.library.dto.BookResponse;
import com.library.model.Book;
import com.library.model.BookStatus;
import com.library.model.Category;
import com.library.repository.BookRepository;
import com.library.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<BookResponse> getAllBooks(Long categoryId, String author,
                                          String genre, String language) {
        return bookRepository.findWithFilters(categoryId, author, genre, language)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BookResponse getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
        return mapToResponse(book);
    }

    @Transactional
    public BookResponse addBook(BookRequest request) {
        validateBookRequest(request);
        Book book = new Book();
        setBookFields(book, request);
        return mapToResponse(bookRepository.save(book));
    }

    @Transactional
    public BookResponse updateBook(Long id, BookRequest request) {
        validateBookRequest(request);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
        setBookFields(book, request);
        return mapToResponse(bookRepository.save(book));
    }

    @Transactional
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }

    @Transactional
    public BookResponse updateBookStatus(Long id, BookRequest request) {
        if (request.getStatus() == null) {
            throw new RuntimeException("Status is required!");
        }
        validateBookStatus(request.getStatus());
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
        book.setStatus(request.getStatus());
        return mapToResponse(bookRepository.save(book));
    }

    private void validateBookRequest(BookRequest request) {
        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new RuntimeException("Book title is required!");
        }
        if (request.getAuthor() == null || request.getAuthor().isBlank()) {
            throw new RuntimeException("Book author is required!");
        }
        if (request.getCategoryId() != null &&
                !categoryRepository.existsById(request.getCategoryId())) {
            throw new RuntimeException("Category not found with id: " + request.getCategoryId());
        }
    }

    private void validateBookStatus(BookStatus status) {
        try {
            BookStatus.valueOf(status.name());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid book status: " + status);
        }
    }

    private void setBookFields(Book book, BookRequest request) {
        book.setTitle(request.getTitle().trim());
        book.setAuthor(request.getAuthor().trim());
        book.setGenre(request.getGenre() != null ? request.getGenre().trim() : null);
        book.setLanguage(request.getLanguage() != null ? request.getLanguage().trim() : null);
        book.setCoverImage(request.getCoverImage());

        if (request.getStatus() != null) {
            book.setStatus(request.getStatus());
        }

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException(
                            "Category not found with id: " + request.getCategoryId()));
            book.setCategory(category);
        }
    }

    private BookResponse mapToResponse(Book book) {
        BookResponse response = new BookResponse();
        response.setId(book.getId());
        response.setTitle(book.getTitle());
        response.setAuthor(book.getAuthor());
        response.setGenre(book.getGenre());
        response.setLanguage(book.getLanguage());
        response.setStatus(book.getStatus());
        response.setCoverImage(book.getCoverImage());
        response.setCreatedAt(book.getCreatedAt());

        if (book.getCategory() != null) {
            response.setCategoryName(book.getCategory().getName());
        }

        return response;
    }
}