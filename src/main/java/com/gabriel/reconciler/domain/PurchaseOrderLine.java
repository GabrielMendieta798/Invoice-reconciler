package com.gabriel.reconciler.domain;

import java.math.BigDecimal;
import java.util.Objects;

public record PurchaseOrderLine(    
    String supplierProductCode,
    String description,
    BigDecimal expectedQuantity,
    BigDecimal expectedUnitPrice
)
{
    public PurchaseOrderLine {
        Objects.requireNonNull(description, "description is required");
        Objects.requireNonNull(expectedQuantity, "expectedQuantity is required");
        Objects.requireNonNull(expectedUnitPrice, "expectedUnitPrice is required");

        if (expectedQuantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("expected quantity must be greater than zero");
        }
        if (expectedUnitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("expected unit price cannot be negative");
        }
    }

}
