package com.codeb.ims.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDateTime;

public class ClientDTOs {

    @Data
    public static class ClientRequest {
        @NotBlank(message = "Client name is required")
        private String clientName;
        private String contactName;
        private String email;
        private String phone;
        private String gstin;
        private String address;
        private Integer groupId;
        private Integer chainId;
        private Integer brandId;
        private Integer subzoneId;
        private Integer assignedTo;
    }

    @Data
    public static class ClientResponse {
        private Integer clientId;
        private String clientName;
        private String contactName;
        private String email;
        private String phone;
        private String gstin;
        private String address;
        private String groupName;
        private String chainName;
        private String brandName;
        private String subzoneName;
        private String assignedToName;
        private String status;
        private LocalDateTime createdAt;
    }

    @Data
    public static class GroupRequest {
        @NotBlank(message = "Group name is required")
        private String groupName;
        private String description;
    }

    @Data
    public static class ChainRequest {
        @NotBlank(message = "Chain name is required")
        private String chainName;
        private Integer groupId;
    }

    @Data
    public static class BrandRequest {
        @NotBlank(message = "Brand name is required")
        private String brandName;
        private Integer chainId;
    }

    @Data
    public static class SubzoneRequest {
        @NotBlank(message = "Subzone name is required")
        private String subzoneName;
        private String region;
    }
}