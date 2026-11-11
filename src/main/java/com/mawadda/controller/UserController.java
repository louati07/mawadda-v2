package com.mawadda.controller;

import com.mawadda.dto.UserRequest;
import com.mawadda.dto.UserResponse;
import com.mawadda.dto.UserUpdateRequest;
import com.mawadda.entity.MedicalService;
import com.mawadda.entity.User;
import com.mawadda.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Hospital Staff & User Accounts Management")
public class UserController {

    private final IUserService userService;

    @PostMapping
    @Operation(summary = "Register user account (auto-hashes password)")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest request) {
        User user = User.builder()
                .email(request.email())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .password(request.password())
                .role(request.role())
                .canCreateService(request.canCreateService() != null ? request.canCreateService() : false)
                .service(request.serviceId() != null ? MedicalService.builder().id(request.serviceId()).build() : null)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserResponse.fromEntity(userService.add(user)));
    }

    @GetMapping
    @Operation(summary = "List all staff users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(
                userService.showAll().stream().map(UserResponse::fromEntity).toList()
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user profile by ID")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Integer id) {
        return ResponseEntity.ok(UserResponse.fromEntity(userService.show(id)));
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Find user by email")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(UserResponse.fromEntity(userService.showByEmail(email)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user details")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Integer id, @Valid @RequestBody UserUpdateRequest request) {
        User updateData = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .password(request.password())
                .role(request.role())
                .service(request.serviceId() != null ? MedicalService.builder().id(request.serviceId()).build() : null)
                .build();

        return ResponseEntity.ok(UserResponse.fromEntity(userService.modify(id, updateData)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user account")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
