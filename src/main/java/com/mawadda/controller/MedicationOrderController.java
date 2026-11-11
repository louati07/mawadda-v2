package com.mawadda.controller;

import com.mawadda.dto.MedicationOrderRequest;
import com.mawadda.dto.MedicationOrderResponse;
import com.mawadda.dto.MedicationOrderUpdateRequest;
import com.mawadda.entity.Admission;
import com.mawadda.entity.MedicationOrder;
import com.mawadda.entity.User;
import com.mawadda.service.IMedicationOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medications")
@RequiredArgsConstructor
@Tag(name = "Medications", description = "Pediatric Medication Orders & Prescriptions")
public class MedicationOrderController {

    private final IMedicationOrderService medicationOrderService;

    @PostMapping
    @Operation(summary = "Prescribe medication order for an admission")
    public ResponseEntity<MedicationOrderResponse> createMedicationOrder(@Valid @RequestBody MedicationOrderRequest request) {
        MedicationOrder medicationOrder = MedicationOrder.builder()
                .admission(Admission.builder().id(request.admissionId()).build())
                .medication(request.medication())
                .dosage(request.dosage())
                .route(request.route())
                .orderedBy(User.builder().id(request.orderedById()).build())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(MedicationOrderResponse.fromEntity(medicationOrderService.add(medicationOrder)));
    }

    @GetMapping
    @Operation(summary = "List all medication orders")
    public ResponseEntity<List<MedicationOrderResponse>> getAllMedicationOrders() {
        return ResponseEntity.ok(
                medicationOrderService.showAll().stream().map(MedicationOrderResponse::fromEntity).toList()
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get medication order by ID")
    public ResponseEntity<MedicationOrderResponse> getMedicationOrderById(@PathVariable Integer id) {
        return ResponseEntity.ok(MedicationOrderResponse.fromEntity(medicationOrderService.show(id)));
    }

    @GetMapping("/admission/{admissionId}")
    @Operation(summary = "List all medication orders for an admission")
    public ResponseEntity<List<MedicationOrderResponse>> getMedicationOrdersByAdmission(@PathVariable Integer admissionId) {
        return ResponseEntity.ok(
                medicationOrderService.showByAdmission(admissionId).stream().map(MedicationOrderResponse::fromEntity).toList()
        );
    }

    @PatchMapping("/{id}/discontinue")
    @Operation(summary = "Discontinue an active medication prescription")
    public ResponseEntity<MedicationOrderResponse> discontinueMedicationOrder(@PathVariable Integer id) {
        return ResponseEntity.ok(MedicationOrderResponse.fromEntity(medicationOrderService.discontinue(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update medication order dosage or route")
    public ResponseEntity<MedicationOrderResponse> updateMedicationOrder(@PathVariable Integer id, @Valid @RequestBody MedicationOrderUpdateRequest request) {
        MedicationOrder updateData = MedicationOrder.builder()
                .medication(request.medication())
                .dosage(request.dosage())
                .route(request.route())
                .build();

        return ResponseEntity.ok(MedicationOrderResponse.fromEntity(medicationOrderService.modify(id, updateData)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete medication order")
    public ResponseEntity<Void> deleteMedicationOrder(@PathVariable Integer id) {
        medicationOrderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
