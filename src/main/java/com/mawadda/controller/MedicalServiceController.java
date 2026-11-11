package com.mawadda.controller;

import com.mawadda.dto.MedicalServiceRequest;
import com.mawadda.dto.MedicalServiceResponse;
import com.mawadda.entity.MedicalService;
import com.mawadda.service.IMedicalServiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
@Tag(name = "Medical Services", description = "Hospital Departments & Invite Code Management")
public class MedicalServiceController {

    private final IMedicalServiceService serviceService;

    @PostMapping
    @Operation(summary = "Create a new medical department (auto-generates invite code)")
    public ResponseEntity<MedicalServiceResponse> createService(@Valid @RequestBody MedicalServiceRequest request) {
        MedicalService service = MedicalService.builder()
                .name(request.name())
                .inviteCode(request.inviteCode())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(MedicalServiceResponse.fromEntity(serviceService.add(service)));
    }

    @GetMapping
    @Operation(summary = "List all hospital medical services")
    public ResponseEntity<List<MedicalServiceResponse>> getAllServices() {
        return ResponseEntity.ok(
                serviceService.showAll().stream().map(MedicalServiceResponse::fromEntity).toList()
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get service by ID")
    public ResponseEntity<MedicalServiceResponse> getServiceById(@PathVariable Integer id) {
        return ResponseEntity.ok(MedicalServiceResponse.fromEntity(serviceService.show(id)));
    }

    @GetMapping("/invite/{inviteCode}")
    @Operation(summary = "Look up service by invite code")
    public ResponseEntity<MedicalServiceResponse> getServiceByInviteCode(@PathVariable String inviteCode) {
        return ResponseEntity.ok(MedicalServiceResponse.fromEntity(serviceService.showByInviteCode(inviteCode)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update department details")
    public ResponseEntity<MedicalServiceResponse> updateService(@PathVariable Integer id, @Valid @RequestBody MedicalServiceRequest request) {
        MedicalService updateData = MedicalService.builder()
                .name(request.name())
                .build();
        return ResponseEntity.ok(MedicalServiceResponse.fromEntity(serviceService.modify(id, updateData)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete department")
    public ResponseEntity<Void> deleteService(@PathVariable Integer id) {
        serviceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
