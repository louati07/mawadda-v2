package com.mawadda.controller;

import com.mawadda.dto.PatientRequest;
import com.mawadda.dto.PatientResponse;
import com.mawadda.dto.PatientUpdateRequest;
import com.mawadda.entity.MedicalService;
import com.mawadda.entity.Parent;
import com.mawadda.entity.Patient;
import com.mawadda.enums.BloodType;
import com.mawadda.service.IPatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@Tag(name = "Patients", description = "Pediatric Patients Identity & Registry")
public class PatientController {

    private final IPatientService patientService;

    @PostMapping
    @Operation(summary = "Register a pediatric patient")
    public ResponseEntity<PatientResponse> createPatient(@Valid @RequestBody PatientRequest request) {
        Patient patient = Patient.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .birthdate(request.birthdate())
                .sex(request.sex())
                .bloodType(request.bloodType() != null ? request.bloodType() : BloodType.UNKNOWN)
                .birthType(request.birthType())
                .birthWeight(request.birthWeight())
                .gestationalAge(request.gestationalAge())
                .complications(request.complications())
                .parent(Parent.builder().id(request.parentId()).build())
                .service(MedicalService.builder().id(request.serviceId()).build())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(PatientResponse.fromEntity(patientService.add(patient)));
    }

    @GetMapping
    @Operation(summary = "List all pediatric patients")
    public ResponseEntity<List<PatientResponse>> getAllPatients() {
        return ResponseEntity.ok(
                patientService.showAll().stream().map(PatientResponse::fromEntity).toList()
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get pediatric patient by ID")
    public ResponseEntity<PatientResponse> getPatientById(@PathVariable Integer id) {
        return ResponseEntity.ok(PatientResponse.fromEntity(patientService.show(id)));
    }

    @GetMapping("/service/{serviceId}")
    @Operation(summary = "List patients by medical service ID")
    public ResponseEntity<List<PatientResponse>> getPatientsByService(@PathVariable Integer serviceId) {
        return ResponseEntity.ok(
                patientService.showByService(serviceId).stream().map(PatientResponse::fromEntity).toList()
        );
    }

    @GetMapping("/parent/{cin}")
    @Operation(summary = "List children by parent CIN")
    public ResponseEntity<List<PatientResponse>> getPatientsByParentCin(@PathVariable String cin) {
        return ResponseEntity.ok(
                patientService.showByParentCin(cin).stream().map(PatientResponse::fromEntity).toList()
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update patient clinical identity")
    public ResponseEntity<PatientResponse> updatePatient(@PathVariable Integer id, @Valid @RequestBody PatientUpdateRequest request) {
        Patient updateData = Patient.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .birthdate(request.birthdate())
                .sex(request.sex())
                .bloodType(request.bloodType())
                .birthType(request.birthType())
                .birthWeight(request.birthWeight())
                .gestationalAge(request.gestationalAge())
                .complications(request.complications())
                .build();

        return ResponseEntity.ok(PatientResponse.fromEntity(patientService.modify(id, updateData)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete patient record")
    public ResponseEntity<Void> deletePatient(@PathVariable Integer id) {
        patientService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
