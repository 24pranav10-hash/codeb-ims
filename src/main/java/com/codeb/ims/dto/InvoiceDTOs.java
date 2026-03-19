package com.codeb.ims.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class InvoiceDTOs {

    @Data
    public static class InvoiceItemRequest {
        private String description;
        private BigDecimal quantity;
        private BigDecimal unitPrice;
    }

    @Data
    public static class InvoiceRequest {
        private Integer clientId;
        private Integer estimateId;
        private BigDecimal taxPercent;
        private LocalDate dueDate;
        private String notes;
        private List<InvoiceItemRequest> items;
    }

    @Data
    public static class InvoiceItemResponse {
        private Integer itemId;
        private String description;
        private BigDecimal quantity;
        private BigDecimal unitPrice;
        private BigDecimal totalPrice;
    }

    @Data
    public static class InvoiceResponse {
        private Integer invoiceId;
        private String invoiceNumber;
        private String clientName;
        private String createdByName;
        private BigDecimal subtotal;
        private BigDecimal taxPercent;
        private BigDecimal taxAmount;
        private BigDecimal totalAmount;
        private BigDecimal amountPaid;
        private BigDecimal balanceDue;
        private String status;
        private LocalDate dueDate;
        private String notes;
        private List<InvoiceItemResponse> items;
        private LocalDateTime createdAt;
    }

    @Data
    public static class PaymentRequest {
        private Integer invoiceId;
        private BigDecimal amount;
        private LocalDate paymentDate;
        private String paymentMode;
        private String referenceNum;
        private String notes;
    }

    @Data
    public static class PaymentResponse {
        private Integer paymentId;
        private String invoiceNumber;
        private BigDecimal amount;
        private LocalDate paymentDate;
        private String paymentMode;
        private String referenceNum;
        private LocalDateTime createdAt;
    }
}