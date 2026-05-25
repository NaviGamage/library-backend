package com.library.service;

import com.library.dto.ReservationRequest;
import com.library.dto.ReservationResponse;
import com.library.model.Book;
import com.library.model.BookStatus;
import com.library.model.Reservation;
import com.library.model.User;
import com.library.repository.BookRepository;
import com.library.repository.ReservationRepository;
import com.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    private static final List<Integer> ALLOWED_DURATIONS = Arrays.asList(7, 14, 21);

    @Transactional
    public ReservationResponse reserveBook(ReservationRequest request) {
        // 1. Validate duration
        if (!ALLOWED_DURATIONS.contains(request.getDurationDays())) {
            throw new IllegalArgumentException("Duration must be 7, 14, or 21 days!");
        }

        // 2. Get and validate logged-in user
        User user = getAuthenticatedUser();
        if (user.isBlacklisted()) {
            throw new IllegalStateException("Your account is blacklisted!");
        }

        // 3. Get and validate book
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new IllegalArgumentException("Book not found with id: " + request.getBookId()));

        if (book.getStatus() != BookStatus.AVAILABLE) {
            throw new IllegalStateException("Book is not available for reservation!");
        }

        // 4. Duplicate check
        if (reservationRepository.existsByUserIdAndBookId(user.getId(), book.getId())) {
            throw new IllegalStateException("You have already reserved this book!");
        }

        // 5. Create reservation
        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setBook(book);
        reservation.setStartDate(LocalDate.now());
        reservation.setEndDate(LocalDate.now().plusDays(request.getDurationDays()));
        reservation.setDurationDays(request.getDurationDays());

        // 6. Update book status (Dirty checking so automatically saved)
        book.setStatus(BookStatus.RESERVED);

        Reservation saved = reservationRepository.save(reservation);

        // 7. one of the record JOIN FETCH loading
        return reservationRepository.findByIdWithDetails(saved.getId())
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Reservation failed to reload!"));
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> getMyReservations() {
        User user = getAuthenticatedUser();
        return reservationRepository.findByUserIdWithDetails(user.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> getAllReservations() {
        return reservationRepository.findAllWithDetails()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // helper method to prevent duplicate security entries.
    private User getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found!"));
    }

    private ReservationResponse mapToResponse(Reservation reservation) {
        ReservationResponse response = new ReservationResponse();
        response.setId(reservation.getId());
        response.setBookTitle(reservation.getBook().getTitle());
        response.setBookAuthor(reservation.getBook().getAuthor());
        response.setUserName(reservation.getUser().getName());
        response.setStartDate(reservation.getStartDate());
        response.setEndDate(reservation.getEndDate());
        response.setDurationDays(reservation.getDurationDays());
        response.setCreatedAt(reservation.getCreatedAt());
        return response;
    }
}