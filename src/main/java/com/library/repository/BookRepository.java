package com.library.repository;

import com.library.model.Book;
import com.library.model.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByCategory_Id(Long categoryId);
    List<Book> findByAuthorContainingIgnoreCase(String author);
    List<Book> findByGenreContainingIgnoreCase(String genre);
    List<Book> findByLanguageContainingIgnoreCase(String language);
    List<Book> findByStatus(BookStatus status);


    @Query("SELECT b FROM Book b " +
            "LEFT JOIN FETCH b.category c " +
            "WHERE (:categoryId IS NULL OR c.id = :categoryId) AND " +
            "(:author IS NULL OR LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%'))) AND " +
            "(:genre IS NULL OR LOWER(b.genre) LIKE LOWER(CONCAT('%', :genre, '%'))) AND " +
            "(:language IS NULL OR LOWER(b.language) LIKE LOWER(CONCAT('%', :language, '%')))")

    List<Book> findWithFilters(
            @Param("categoryId") Long categoryId,
            @Param("author") String author,
            @Param("genre") String genre,
            @Param("language") String language
    );
}