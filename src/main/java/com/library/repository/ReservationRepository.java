package com.library.repository;

import com.library.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r " +
            "JOIN FETCH r.user u " +
            "JOIN FETCH r.book b " +
            "WHERE u.id = :userId")
    List<Reservation> findByUserIdWithDetails(@Param("userId") Long userId);

    @Query("SELECT r FROM Reservation r " +
            "JOIN FETCH r.user u " +
            "JOIN FETCH r.book b")
    List<Reservation> findAllWithDetails();

    @Query("SELECT r FROM Reservation r " +
            "JOIN FETCH r.user u " +
            "JOIN FETCH r.book b " +
            "WHERE b.id = :bookId")
    List<Reservation> findByBookIdWithDetails(@Param("bookId") Long bookId);

    @Query("SELECT r FROM Reservation r " +
            "JOIN FETCH r.user u " +
            "JOIN FETCH r.book b " +
            "WHERE r.id = :id")
    Optional<Reservation> findByIdWithDetails(@Param("id") Long id);

    boolean existsByUserIdAndBookId(Long userId, Long bookId);
}