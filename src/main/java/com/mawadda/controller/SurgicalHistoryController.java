package com.mawadda.controller;

import com.mawadda.dto.SurgicalHistoryRequest;
import com.mawadda.dto.SurgicalHistoryResponse;
import com.mawadda.dto.SurgicalHistoryUpdateRequest;
import com.mawadda.entity.Patient;
import com.mawadda.entity.SurgicalHistory;
import com.mawadda.service.ISurgicalHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/surgical-history")
@RequiredArgsConstructor
@Tag(name = "Surgical History", description = "Past Pediatric Surgeries & Interventions")
public class SurgicalHistoryController {

    private final ISurgicalHistoryService surgicalHistoryService;

    @PostMapping
    @Operation(summary = "Log past surgical procedure for a patient")
    public ResponseEntity<SurgicalHistoryResponse> createSurgicalHistory(@Valid @RequestBody SurgicalHistoryRequest request) {
        SurgicalHistory surgicalHistory = SurgicalHistory.builder()
                .patient(Patient.builder().id(request.patientId()).build())
                .name(request.name())
                .bodySite(request.bodySite())
                .date(request.date())
                .complications(request.complications())
                .notes(request.notes())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SurgicalHistoryResponse.fromEntity(surgicalHistoryService.add(surgicalHistory)));
    }

    @GetMapping
    @Operation(summary = "List all surgical histories")
    public ResponseEntity<List<SurgicalHistoryResponse>> getAllSurgicalHistories() {
        return ResponseEntity.ok(
                surgicalHistoryService.showAll().stream().map(SurgicalHistoryResponse::fromEntity).toList()
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get surgical history entry by ID")
    public ResponseEntity<SurgicalHistoryResponse> getSurgicalHistoryById(@PathVariable Integer id) {
        return ResponseEntity.ok(SurgicalHistoryResponse.fromEntity(surgicalHistoryService.show(id)));
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "List surgical history for a patient")
    public ResponseEntity<List<SurgicalHistoryResponse>> getSurgicalHistoriesByPatient(@PathVariable Integer patientId) {
        return ResponseEntity.ok(
                surgicalHistoryService.showByPatient(patientId).stream().map(SurgicalHistoryResponse::fromEntity).toList()
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update surgical history entry")
    public ResponseEntity<SurgicalHistoryResponse> updateSurgicalHistory(@PathVariable Integer id, @Valid @RequestBody SurgicalHistoryUpdateRequest request) {
        SurgicalHistory updateData = SurgicalHistory.builder()
                .name(request.name())
                .bodySite(request.bodySite())
                .date(request.date())
                .complications(request.complications())
                .notes(request.notes())
                .build();

        return ResponseEntity.ok(SurgicalHistoryResponse.fromEntity(surgicalHistoryService.modify(id, updateData)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete surgical history record")
    public ResponseEntity<Void> deleteSurgicalHistory(@PathVariable Integer id) {
        surgicalHistoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
