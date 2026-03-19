package com.codeb.ims.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "brands")
@Data
@NoArgsConstructor
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_id")
    private Integer brandId;

    @Column(name = "brand_name", nullable = false, length = 100)
    private String brandName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chain_id", nullable = false)
    private Chain chain;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}