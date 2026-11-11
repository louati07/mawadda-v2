package com.mawadda.controller;

import com.mawadda.dto.AdmissionRequest;
import com.mawadda.dto.AdmissionResponse;
import com.mawadda.dto.AdmissionUpdateRequest;
import com.mawadda.entity.Admission;
import com.mawadda.entity.MedicalService;
import com.mawadda.entity.Patient;
import com.mawadda.service.IAdmissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admissions")
@RequiredArgsConstructor
@Tag(name = "Admissions", description = "Inpatient Admissions & Hospitalization Lifecycle")
public class AdmissionController {

    private final IAdmissionService admissionService;

    @PostMapping
    @Operation(summary = "Admit a patient to a bed (guarantees single-active-admission constraint)")
    public ResponseEntity<AdmissionResponse> createAdmission(@Valid @RequestBody AdmissionRequest request) {
        Admission admission = Admission.builder()
                .patient(Patient.builder().id(request.patientId()).build())
                .service(MedicalService.builder().id(request.serviceId()).build())
                .chiefComplaint(request.chiefComplaint())
                .weight(request.weight())
                .status(request.status())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(AdmissionResponse.fromEntity(admissionService.add(admission)));
    }

    @GetMapping
    @Operation(summary = "List all admissions")
    public ResponseEntity<List<AdmissionResponse>> getAllAdmissions() {
        return ResponseEntity.ok(
                admissionService.showAll().stream().map(AdmissionResponse::fromEntity).toList()
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get admission details by ID")
    public ResponseEntity<AdmissionResponse> getAdmissionById(@PathVariable Integer id) {
        return ResponseEntity.ok(AdmissionResponse.fromEntity(admissionService.show(id)));
    }

    @GetMapping("/active/service/{serviceId}")
    @Operation(summary = "List active inpatients in a medical service")
    public ResponseEntity<List<AdmissionResponse>> getActiveByService(@PathVariable Integer serviceId) {
        return ResponseEntity.ok(
                admissionService.showActiveByService(serviceId).stream().map(AdmissionResponse::fromEntity).toList()
        );
    }

    @GetMapping("/active/patient/{patientId}")
    @Operation(summary = "Get active admission for a patient")
    public ResponseEntity<AdmissionResponse> getActiveByPatient(@PathVariable Integer patientId) {
        return ResponseEntity.ok(AdmissionResponse.fromEntity(admissionService.showActiveAdmissionByPatient(patientId)));
    }

    @PatchMapping("/{id}/discharge")
    @Operation(summary = "Discharge patient from active hospital stay")
    public ResponseEntity<AdmissionResponse> dischargeAdmission(@PathVariable Integer id) {
        return ResponseEntity.ok(AdmissionResponse.fromEntity(admissionService.discharge(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update clinical details of admission")
    public ResponseEntity<AdmissionResponse> updateAdmission(@PathVariable Integer id, @Valid @RequestBody AdmissionUpdateRequest request) {
        Admission updateData = Admission.builder()
                .chiefComplaint(request.chiefComplaint())
                .weight(request.weight())
                .status(request.status())
                .build();

        return ResponseEntity.ok(AdmissionResponse.fromEntity(admissionService.modify(id, updateData)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete admission record")
    public ResponseEntity<Void> deleteAdmission(@PathVariable Integer id) {
        admissionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
