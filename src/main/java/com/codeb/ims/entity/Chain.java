package com.codeb.ims.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "chains")
@Data
@NoArgsConstructor
public class Chain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chain_id")
    private Integer chainId;

    @Column(name = "chain_name", nullable = false, length = 100)
    private String chainName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}