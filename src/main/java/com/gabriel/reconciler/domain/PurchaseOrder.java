package com.gabriel.reconciler.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;


public record PurchaseOrder(
    String orderNumber,
    String supplierName,
    LocalDate orderDate,
    List<PurchaseOrderLine> lines
){
    public PurchaseOrder {
    Objects.requireNonNull(orderNumber, "Order number is required");
    Objects.requireNonNull(supplierName, "Supplier name is required");
    Objects.requireNonNull(orderDate, "Order date is required");
    Objects.requireNonNull(lines, "Order lines are required");

    if (lines.isEmpty()) {
        throw new IllegalArgumentException("Order lines cannot be empty");
    }

    if (orderDate.isAfter(LocalDate.now())) {
        throw new IllegalArgumentException("Order date cannot be in the future");
    }

    if (orderNumber.isBlank()) {
        throw new IllegalArgumentException("Order number cannot be blank");
    }
    if (supplierName.isBlank()) {
        throw new IllegalArgumentException("Supplier name cannot be blank");
    }

    lines = List.copyOf(lines);
    
    }
}
