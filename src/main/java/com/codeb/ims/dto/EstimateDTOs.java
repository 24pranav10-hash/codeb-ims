package com.codeb.ims.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class EstimateDTOs {

    @Data
    public static class EstimateItemRequest {
        private String description;
        private BigDecimal quantity;
        private BigDecimal unitPrice;
    }

    @Data
    public static class EstimateRequest {
        private Integer clientId;
        private Integer chainId;
        private BigDecimal taxPercent;
        private LocalDate validUntil;
        private String notes;
        private List<EstimateItemRequest> items;
    }

    @Data
    public static class EstimateItemResponse {
        private Integer itemId;
        private String description;
        private BigDecimal quantity;
        private BigDecimal unitPrice;
        private BigDecimal totalPrice;
    }

    @Data
    public static class EstimateResponse {
        private Integer estimateId;
        private String estimateNumber;
        private String clientName;
        private String chainName;
        private String createdByName;
        private BigDecimal subtotal;
        private BigDecimal taxPercent;
        private BigDecimal taxAmount;
        private BigDecimal totalAmount;
        private String status;
        private LocalDate validUntil;
        private String notes;
        private List<EstimateItemResponse> items;
        private LocalDateTime createdAt;
    }
}