package org.pfa.billingservice.entities;

import jakarta.persistence.*;
import lombok.*;
import org.pfa.billingservice.auth.entities.TenantAwareEntity;

import java.math.BigDecimal;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order extends TenantAwareEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String orderNumber;

    private BigDecimal totalAmount;

    private String status; // PENDING, CONFIRMED, SHIPPED
}
