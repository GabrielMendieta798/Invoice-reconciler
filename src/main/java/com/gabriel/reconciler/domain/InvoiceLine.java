package com.gabriel.reconciler.domain;

import java.math.BigDecimal;
import java.util.Objects;

public record InvoiceLine(
    String supplierProductCode,
    String description,
    BigDecimal actualQuantity,
    BigDecimal actualUnitPrice
) {
    public InvoiceLine {
        Objects.requireNonNull(description, "Description is required");
        Objects.requireNonNull(actualQuantity, "Invoice quantity is required");
        Objects.requireNonNull(actualUnitPrice, "Invoice unit price is required");

        if (actualQuantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("actual quantity must be greater than zero");
        }
        if (actualUnitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("actual unit price cannot be negative");
        }
    }
}
