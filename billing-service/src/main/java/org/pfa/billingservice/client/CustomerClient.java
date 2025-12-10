package org.pfa.billingservice.client;

import org.pfa.billingservice.dto.CustomerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "sales-service", path = "/api/v1/customers")
public interface CustomerClient {
    @GetMapping("/{id}")
    CustomerDto getCustomerById(@PathVariable("id") Long id);
}
