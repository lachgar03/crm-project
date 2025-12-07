package org.pfa.billingservice.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/billing")
public class BillingController {

    @GetMapping("/check-limit/{tenantId}")
    public ResponseEntity<Boolean> checkLimit(@PathVariable Long tenantId) {
        // Hardcode the limit logic for now (max 10 users per tenant)
        // In real scenario, we might query db or config
        return ResponseEntity.ok(true); 
    }
}
