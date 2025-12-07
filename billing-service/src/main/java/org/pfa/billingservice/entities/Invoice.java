package org.pfa.billingservice.entities;

import jakarta.persistence.*;
import lombok.*;
import org.pfa.billingservice.auth.entities.TenantAwareEntity;

import java.math.BigDecimal;

@Entity
@Table(name = "invoices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice extends TenantAwareEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String invoiceNumber;

    @Column(nullable = false)
    private BigDecimal amountDue;

    private BigDecimal amountPaid;

    private String status; // PAID, UNPAID, CANCELLED

    private Long customerId; // Loose coupling with Sales Service
}
