package com.codeb.ims.controller;

import com.codeb.ims.dto.InvoiceDTOs.*;
import com.codeb.ims.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping
    public ResponseEntity<InvoiceResponse> createInvoice(
            @RequestBody InvoiceRequest request,
            Authentication auth) {
        return ResponseEntity.ok(
                invoiceService.createInvoice(request, auth.getName()));
    }

    @GetMapping
    public ResponseEntity<List<InvoiceResponse>> getAllInvoices() {
        return ResponseEntity.ok(invoiceService.getAllInvoices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponse> getInvoiceById(
            @PathVariable Integer id) {
        return ResponseEntity.ok(invoiceService.getInvoiceById(id));
    }

    @GetMapping("/by-client/{clientId}")
    public ResponseEntity<List<InvoiceResponse>> getByClient(
            @PathVariable Integer clientId) {
        return ResponseEntity.ok(
                invoiceService.getInvoicesByClient(clientId));
    }

    @PostMapping("/payments")
    public ResponseEntity<PaymentResponse> recordPayment(
            @RequestBody PaymentRequest request,
            Authentication auth) {
        return ResponseEntity.ok(
                invoiceService.recordPayment(request, auth.getName()));
    }
}