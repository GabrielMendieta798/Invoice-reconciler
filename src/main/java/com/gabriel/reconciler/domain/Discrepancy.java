package com.gabriel.reconciler.domain;

public record Discrepancy(
    String productReference,
    DiscrepancyType type,
    String message
) {
    
}
