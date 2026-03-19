package com.codeb.ims.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "subzones")
@Data
@NoArgsConstructor
public class Subzone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subzone_id")
    private Integer subzoneId;

    @Column(name = "subzone_name", nullable = false, length = 100)
    private String subzoneName;

    @Column(name = "region", length = 100)
    private String region;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}