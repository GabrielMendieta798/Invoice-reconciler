package com.gabriel.reconciler.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;


public record Invoice (
    String invoiceNumber,
    String supplierName,
    LocalDate issueDate,
    List<InvoiceLine> lines
){
    public Invoice {
        Objects.requireNonNull(invoiceNumber, "Invoice number is required");
        Objects.requireNonNull(supplierName, "Supplier name is required");
        Objects.requireNonNull(issueDate, "Issue date is required");
        Objects.requireNonNull(lines, "Invoice lines are required");

        if (lines.isEmpty()) {
            throw new IllegalArgumentException("Invoice lines cannot be empty");
        }
        if (invoiceNumber.isBlank()) {
            throw new IllegalArgumentException("Invoice number cannot be blank");
        }
        if (supplierName.isBlank()) {
            throw new IllegalArgumentException("Supplier name cannot be blank");
        }

        lines = List.copyOf(lines);
    }
}
