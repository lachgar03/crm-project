package org.pfa.salesservice.entities;

import jakarta.persistence.*;
import lombok.*;
import org.pfa.salesservice.auth.entities.TenantAwareEntity;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer extends TenantAwareEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    private String phone;
    private String addressLine1;
    private String city;
    private String country;

    private String assignedToUserId; // UUID or String ID of the agent/user
}
