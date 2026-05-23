package com.library.controller;

import com.library.model.User;
import com.library.service.UserManagementService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserManagementController {

    private final UserManagementService userManagementService;

    @GetMapping
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userManagementService.getAllUsers());
    }

    @PatchMapping("/{id}/blacklist")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<Void> blacklistUser(
            @PathVariable @Positive(message = "User ID must be a positive number") Long id) {
        userManagementService.blacklistUser(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/unblacklist")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<Void> unblacklistUser(
            @PathVariable @Positive(message = "User ID must be a positive number") Long id) {
        userManagementService.unblacklistUser(id);
        return ResponseEntity.noContent().build();
    }
}