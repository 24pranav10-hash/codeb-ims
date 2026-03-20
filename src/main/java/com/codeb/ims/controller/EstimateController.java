package com.codeb.ims.controller;

import com.codeb.ims.dto.EstimateDTOs.*;
import com.codeb.ims.service.EstimateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/estimates")
@RequiredArgsConstructor
public class EstimateController {

    private final EstimateService estimateService;

    @PostMapping
    public ResponseEntity<EstimateResponse> createEstimate(
            @RequestBody EstimateRequest request,
            Authentication auth) {
        return ResponseEntity.ok(
                estimateService.createEstimate(request, auth.getName()));
    }

    @GetMapping
    public ResponseEntity<List<EstimateResponse>> getAllEstimates() {
        return ResponseEntity.ok(estimateService.getAllEstimates());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstimateResponse> getEstimateById(
            @PathVariable Integer id) {
        return ResponseEntity.ok(estimateService.getEstimateById(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<EstimateResponse> updateStatus(
            @PathVariable Integer id,
            @RequestParam String status) {
        return ResponseEntity.ok(estimateService.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEstimate(@PathVariable Integer id) {
        estimateService.deleteEstimate(id);
        return ResponseEntity.ok("Estimate deleted.");
    }
}