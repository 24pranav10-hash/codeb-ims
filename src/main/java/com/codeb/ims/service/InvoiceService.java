package com.codeb.ims.service;

import com.codeb.ims.dto.InvoiceDTOs.*;
import com.codeb.ims.entity.*;
import com.codeb.ims.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository  invoiceRepository;
    private final ClientRepository   clientRepository;
    private final UserRepository     userRepository;
    private final PaymentRepository  paymentRepository;
    private final EstimateRepository estimateRepository;

    public InvoiceResponse createInvoice(InvoiceRequest req, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Client client = clientRepository.findById(req.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found"));

        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber("INV-" + System.currentTimeMillis());
        invoice.setClient(client);
        invoice.setCreatedBy(user);
        invoice.setTaxPercent(req.getTaxPercent() != null ? req.getTaxPercent() : new BigDecimal("18.00"));
        invoice.setDueDate(req.getDueDate());
        invoice.setNotes(req.getNotes());

        if (req.getEstimateId() != null)
            estimateRepository.findById(req.getEstimateId()).ifPresent(invoice::setEstimate);

        // Build line items
        List<InvoiceItem> items = req.getItems().stream().map(i -> {
            InvoiceItem item = new InvoiceItem();
            item.setInvoice(invoice);
            item.setDescription(i.getDescription());
            item.setQuantity(i.getQuantity());
            item.setUnitPrice(i.getUnitPrice());
            item.setTotalPrice(i.getQuantity().multiply(i.getUnitPrice()));
            return item;
        }).collect(Collectors.toList());
        invoice.setItems(items);

        // Calculate totals
        BigDecimal subtotal = items.stream()
                .map(InvoiceItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal taxAmount = subtotal.multiply(invoice.getTaxPercent())
                .divide(new BigDecimal("100"));
        invoice.setSubtotal(subtotal);
        invoice.setTaxAmount(taxAmount);
        invoice.setTotalAmount(subtotal.add(taxAmount));
        invoice.setBalanceDue(invoice.getTotalAmount());

        return toResponse(invoiceRepository.save(invoice));
    }

    public List<InvoiceResponse> getAllInvoices() {
        return invoiceRepository.findAll().stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    public InvoiceResponse getInvoiceById(Integer id) {
        return toResponse(invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found")));
    }

    public PaymentResponse recordPayment(PaymentRequest req, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Invoice invoice = invoiceRepository.findById(req.getInvoiceId())
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        // Record payment
        Payment payment = new Payment();
        payment.setInvoice(invoice);
        payment.setAmount(req.getAmount());
        payment.setPaymentDate(req.getPaymentDate());
        payment.setPaymentMode(req.getPaymentMode());
        payment.setReferenceNum(req.getReferenceNum());
        payment.setNotes(req.getNotes());
        payment.setRecordedBy(user);
        paymentRepository.save(payment);

        // Update invoice
        BigDecimal newAmountPaid = invoice.getAmountPaid().add(req.getAmount());
        invoice.setAmountPaid(newAmountPaid);
        invoice.setBalanceDue(invoice.getTotalAmount().subtract(newAmountPaid));
        if (invoice.getBalanceDue().compareTo(BigDecimal.ZERO) <= 0) {
            invoice.setStatus(Invoice.Status.paid);
        } else {
            invoice.setStatus(Invoice.Status.partially_paid);
        }
        invoiceRepository.save(invoice);

        PaymentResponse res = new PaymentResponse();
        res.setPaymentId(payment.getPaymentId());
        res.setInvoiceNumber(invoice.getInvoiceNumber());
        res.setAmount(payment.getAmount());
        res.setPaymentDate(payment.getPaymentDate());
        res.setPaymentMode(payment.getPaymentMode());
        res.setReferenceNum(payment.getReferenceNum());
        res.setCreatedAt(payment.getCreatedAt());
        return res;
    }

    public List<InvoiceResponse> getInvoicesByClient(Integer clientId) {
        return invoiceRepository.findByClientClientId(clientId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    private InvoiceResponse toResponse(Invoice i) {
        InvoiceResponse res = new InvoiceResponse();
        res.setInvoiceId(i.getInvoiceId());
        res.setInvoiceNumber(i.getInvoiceNumber());
        res.setClientName(i.getClient().getClientName());
        res.setCreatedByName(i.getCreatedBy().getFullName());
        res.setSubtotal(i.getSubtotal());
        res.setTaxPercent(i.getTaxPercent());
        res.setTaxAmount(i.getTaxAmount());
        res.setTotalAmount(i.getTotalAmount());
        res.setAmountPaid(i.getAmountPaid());
        res.setBalanceDue(i.getBalanceDue());
        res.setStatus(i.getStatus().name());
        res.setDueDate(i.getDueDate());
        res.setNotes(i.getNotes());
        res.setCreatedAt(i.getCreatedAt());
        if (i.getItems() != null) {
            res.setItems(i.getItems().stream().map(item -> {
                InvoiceItemResponse ir = new InvoiceItemResponse();
                ir.setItemId(item.getItemId());
                ir.setDescription(item.getDescription());
                ir.setQuantity(item.getQuantity());
                ir.setUnitPrice(item.getUnitPrice());
                ir.setTotalPrice(item.getTotalPrice());
                return ir;
            }).collect(Collectors.toList()));
        }
        return res;
    }
}