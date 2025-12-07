package org.pfa.billingservice.services;

import lombok.RequiredArgsConstructor;
import org.pfa.billingservice.entities.Invoice;
import org.pfa.billingservice.repositories.InvoiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    @Transactional(readOnly = true)
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Invoice> getInvoiceById(Long id) {
        return invoiceRepository.findById(id);
    }

    @Transactional
    public Invoice createInvoice(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    @Transactional
    public Invoice updateInvoice(Long id, Invoice invoiceDetails) {
        return invoiceRepository.findById(id)
                .map(invoice -> {
                    invoice.setInvoiceNumber(invoiceDetails.getInvoiceNumber());
                    invoice.setAmountDue(invoiceDetails.getAmountDue());
                    invoice.setAmountPaid(invoiceDetails.getAmountPaid());
                    invoice.setStatus(invoiceDetails.getStatus());
                    invoice.setCustomerId(invoiceDetails.getCustomerId());
                    return invoiceRepository.save(invoice);
                })
                .orElseThrow(() -> new RuntimeException("Invoice not found with id " + id));
    }

    @Transactional
    public void deleteInvoice(Long id) {
        invoiceRepository.deleteById(id);
    }
}
