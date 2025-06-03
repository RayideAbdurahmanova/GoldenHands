package com.matrix.Java._Spring.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.mapping.ToOne;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Table(name = "balance")
@Entity
public class Balance {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

}
