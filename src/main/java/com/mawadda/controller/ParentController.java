package com.mawadda.controller;

import com.mawadda.dto.ParentRequest;
import com.mawadda.dto.ParentResponse;
import com.mawadda.entity.Parent;
import com.mawadda.service.IParentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parents")
@RequiredArgsConstructor
@Tag(name = "Parents", description = "Parent & Legal Guardian Management")
public class ParentController {

    private final IParentService parentService;

    @PostMapping
    @Operation(summary = "Register a new legal guardian / parent")
    public ResponseEntity<ParentResponse> createParent(@Valid @RequestBody ParentRequest request) {
        Parent parent = Parent.builder()
                .cin(request.cin())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .phone(request.phone())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ParentResponse.fromEntity(parentService.add(parent)));
    }

    @GetMapping
    @Operation(summary = "List all registered parents")
    public ResponseEntity<List<ParentResponse>> getAllParents() {
        return ResponseEntity.ok(
                parentService.showAll().stream().map(ParentResponse::fromEntity).toList()
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get parent by ID")
    public ResponseEntity<ParentResponse> getParentById(@PathVariable Integer id) {
        return ResponseEntity.ok(ParentResponse.fromEntity(parentService.show(id)));
    }

    @GetMapping("/cin/{cin}")
    @Operation(summary = "Get parent by National CIN")
    public ResponseEntity<ParentResponse> getParentByCin(@PathVariable String cin) {
        return ResponseEntity.ok(ParentResponse.fromEntity(parentService.showByCin(cin)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update parent details")
    public ResponseEntity<ParentResponse> updateParent(@PathVariable Integer id, @Valid @RequestBody ParentRequest request) {
        Parent updateData = Parent.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .phone(request.phone())
                .build();
        return ResponseEntity.ok(ParentResponse.fromEntity(parentService.modify(id, updateData)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete parent record")
    public ResponseEntity<Void> deleteParent(@PathVariable Integer id) {
        parentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
