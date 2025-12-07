package org.pfa.salesservice.entities;

import jakarta.persistence.*;
import lombok.*;
import org.pfa.salesservice.auth.entities.TenantAwareEntity;

@Entity
@Table(name = "tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket extends TenantAwareEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String status; // OPEN, IN_PROGRESS, CLOSED

    private String priority; // LOW, MEDIUM, HIGH

    private Long customerId; // Foreign Key to Customer (managed manually or JPA relationship)
    
    private String agentId; // Assigned Agent ID
}
