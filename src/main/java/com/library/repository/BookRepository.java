package com.library.repository;

import com.library.model.Book;
import com.library.model.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByCategory_Id(Long categoryId);
    List<Book> findByAuthorContainingIgnoreCase(String author);
    List<Book> findByGenreContainingIgnoreCase(String genre);
    List<Book> findByLanguageContainingIgnoreCase(String language);
    List<Book> findByStatus(BookStatus status);
}