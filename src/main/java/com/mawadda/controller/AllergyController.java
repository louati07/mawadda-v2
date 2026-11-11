package com.mawadda.controller;

import com.mawadda.dto.AllergyRequest;
import com.mawadda.dto.AllergyResponse;
import com.mawadda.dto.AllergyUpdateRequest;
import com.mawadda.entity.Allergy;
import com.mawadda.entity.Patient;
import com.mawadda.entity.User;
import com.mawadda.service.IAllergyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/allergies")
@RequiredArgsConstructor
@Tag(name = "Allergies", description = "Adverse Drug & Substance Allergies")
public class AllergyController {

    private final IAllergyService allergyService;

    @PostMapping
    @Operation(summary = "Log an allergy against a patient record")
    public ResponseEntity<AllergyResponse> createAllergy(@Valid @RequestBody AllergyRequest request) {
        Allergy allergy = Allergy.builder()
                .patient(Patient.builder().id(request.patientId()).build())
                .allergen(request.allergen())
                .reaction(request.reaction())
                .severity(request.severity())
                .loggedBy(User.builder().id(request.loggedById()).build())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(AllergyResponse.fromEntity(allergyService.add(allergy)));
    }

    @GetMapping
    @Operation(summary = "List all logged allergies")
    public ResponseEntity<List<AllergyResponse>> getAllAllergies() {
        return ResponseEntity.ok(
                allergyService.showAll().stream().map(AllergyResponse::fromEntity).toList()
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get allergy record by ID")
    public ResponseEntity<AllergyResponse> getAllergyById(@PathVariable Integer id) {
        return ResponseEntity.ok(AllergyResponse.fromEntity(allergyService.show(id)));
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "List all known allergies for a patient")
    public ResponseEntity<List<AllergyResponse>> getAllergiesByPatient(@PathVariable Integer patientId) {
        return ResponseEntity.ok(
                allergyService.showByPatient(patientId).stream().map(AllergyResponse::fromEntity).toList()
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update allergy reaction or severity")
    public ResponseEntity<AllergyResponse> updateAllergy(@PathVariable Integer id, @Valid @RequestBody AllergyUpdateRequest request) {
        Allergy updateData = Allergy.builder()
                .allergen(request.allergen())
                .reaction(request.reaction())
                .severity(request.severity())
                .build();

        return ResponseEntity.ok(AllergyResponse.fromEntity(allergyService.modify(id, updateData)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete allergy record")
    public ResponseEntity<Void> deleteAllergy(@PathVariable Integer id) {
        allergyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
