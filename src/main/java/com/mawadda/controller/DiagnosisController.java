package com.mawadda.controller;

import com.mawadda.dto.DiagnosisRequest;
import com.mawadda.dto.DiagnosisResponse;
import com.mawadda.dto.DiagnosisUpdateRequest;
import com.mawadda.entity.Admission;
import com.mawadda.entity.Diagnosis;
import com.mawadda.entity.User;
import com.mawadda.service.IDiagnosisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/diagnoses")
@RequiredArgsConstructor
@Tag(name = "Diagnoses", description = "Admitting, Working, and Resolved Diagnoses")
public class DiagnosisController {

    private final IDiagnosisService diagnosisService;

    @PostMapping
    @Operation(summary = "Log a diagnosis for an active admission")
    public ResponseEntity<DiagnosisResponse> createDiagnosis(@Valid @RequestBody DiagnosisRequest request) {
        Diagnosis diagnosis = Diagnosis.builder()
                .admission(Admission.builder().id(request.admissionId()).build())
                .description(request.description())
                .type(request.type())
                .loggedBy(User.builder().id(request.loggedById()).build())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(DiagnosisResponse.fromEntity(diagnosisService.add(diagnosis)));
    }

    @GetMapping
    @Operation(summary = "List all diagnoses")
    public ResponseEntity<List<DiagnosisResponse>> getAllDiagnoses() {
        return ResponseEntity.ok(
                diagnosisService.showAll().stream().map(DiagnosisResponse::fromEntity).toList()
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get diagnosis by ID")
    public ResponseEntity<DiagnosisResponse> getDiagnosisById(@PathVariable Integer id) {
        return ResponseEntity.ok(DiagnosisResponse.fromEntity(diagnosisService.show(id)));
    }

    @GetMapping("/admission/{admissionId}")
    @Operation(summary = "List all diagnoses logged for an admission")
    public ResponseEntity<List<DiagnosisResponse>> getDiagnosesByAdmission(@PathVariable Integer admissionId) {
        return ResponseEntity.ok(
                diagnosisService.showByAdmission(admissionId).stream().map(DiagnosisResponse::fromEntity).toList()
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update diagnosis status or description")
    public ResponseEntity<DiagnosisResponse> updateDiagnosis(@PathVariable Integer id, @Valid @RequestBody DiagnosisUpdateRequest request) {
        Diagnosis updateData = Diagnosis.builder()
                .description(request.description())
                .type(request.type())
                .build();

        return ResponseEntity.ok(DiagnosisResponse.fromEntity(diagnosisService.modify(id, updateData)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete diagnosis record")
    public ResponseEntity<Void> deleteDiagnosis(@PathVariable Integer id) {
        diagnosisService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
