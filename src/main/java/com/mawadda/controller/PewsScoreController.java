package com.mawadda.controller;

import com.mawadda.dto.PewsScoreRequest;
import com.mawadda.dto.PewsScoreResponse;
import com.mawadda.entity.Admission;
import com.mawadda.entity.PewsScore;
import com.mawadda.entity.User;
import com.mawadda.service.IPewsScoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pews")
@RequiredArgsConstructor
@Tag(name = "PEWS Scores", description = "Pediatric Early Warning Scoring & Triage Engine")
public class PewsScoreController {

    private final IPewsScoreService pewsScoreService;

    @PostMapping
    @Operation(summary = "Record clinical PEWS vital assessment (auto-computes composite score)")
    public ResponseEntity<PewsScoreResponse> createPewsScore(@Valid @RequestBody PewsScoreRequest request) {
        PewsScore pewsScore = PewsScore.builder()
                .admission(Admission.builder().id(request.admissionId()).build())
                .heartRate(request.heartRate())
                .respiratoryRate(request.respiratoryRate())
                .oxygenSaturation(request.oxygenSaturation())
                .temperature(request.temperature())
                .capillaryRefill(request.capillaryRefill())
                .consciousness(request.consciousness())
                .needsOxygen(request.needsOxygen() != null ? request.needsOxygen() : false)
                .totalScore(request.totalScore())
                .recordedBy(User.builder().id(request.recordedById()).build())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(PewsScoreResponse.fromEntity(pewsScoreService.add(pewsScore)));
    }

    @GetMapping
    @Operation(summary = "List all recorded PEWS evaluations")
    public ResponseEntity<List<PewsScoreResponse>> getAllPewsScores() {
        return ResponseEntity.ok(
                pewsScoreService.showAll().stream().map(PewsScoreResponse::fromEntity).toList()
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get PEWS evaluation by ID")
    public ResponseEntity<PewsScoreResponse> getPewsScoreById(@PathVariable Integer id) {
        return ResponseEntity.ok(PewsScoreResponse.fromEntity(pewsScoreService.show(id)));
    }

    @GetMapping("/admission/{admissionId}")
    @Operation(summary = "Get chronologically ordered PEWS history for an admission")
    public ResponseEntity<List<PewsScoreResponse>> getPewsScoresByAdmission(@PathVariable Integer admissionId) {
        return ResponseEntity.ok(
                pewsScoreService.showByAdmission(admissionId).stream().map(PewsScoreResponse::fromEntity).toList()
        );
    }

    @PatchMapping("/{id}/error")
    @Operation(summary = "Mark PEWS evaluation as audit error with clinical reason")
    public ResponseEntity<PewsScoreResponse> markPewsAsError(@PathVariable Integer id, @RequestParam String reason) {
        return ResponseEntity.ok(PewsScoreResponse.fromEntity(pewsScoreService.markAsError(id, reason)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update PEWS vital parameters")
    public ResponseEntity<PewsScoreResponse> updatePewsScore(@PathVariable Integer id, @Valid @RequestBody PewsScoreRequest request) {
        PewsScore updateData = PewsScore.builder()
                .heartRate(request.heartRate())
                .respiratoryRate(request.respiratoryRate())
                .oxygenSaturation(request.oxygenSaturation())
                .temperature(request.temperature())
                .capillaryRefill(request.capillaryRefill())
                .consciousness(request.consciousness())
                .needsOxygen(request.needsOxygen())
                .totalScore(request.totalScore())
                .build();

        return ResponseEntity.ok(PewsScoreResponse.fromEntity(pewsScoreService.modify(id, updateData)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete PEWS evaluation record")
    public ResponseEntity<Void> deletePewsScore(@PathVariable Integer id) {
        pewsScoreService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
