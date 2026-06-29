package com.gabriel.reconciler.application;

import com.gabriel.reconciler.domain.Discrepancy;
import com.gabriel.reconciler.domain.DiscrepancyType;
import com.gabriel.reconciler.domain.Invoice;
import com.gabriel.reconciler.domain.InvoiceLine;
import com.gabriel.reconciler.domain.PurchaseOrder;
import com.gabriel.reconciler.domain.PurchaseOrderLine;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;

class InvoiceReconciliationServiceTest {
    private final InvoiceReconciliationService service = new InvoiceReconciliationService();

@Test
void shouldDetectQuantityMismatchForMatchingProduct() {
        PurchaseOrder purchaseOrder = new PurchaseOrder(
                "NP-001",
                "Coca-Cola",
                LocalDate.of(2026, 6, 28),
                List.of(
                        new PurchaseOrderLine(
                                "458921",
                                "Coca-Cola Original 2.25 L",
                                new BigDecimal("10"),
                                new BigDecimal("2000.00")
                        )
                )
        );

        Invoice invoice = new Invoice(
                "0001-000123",
                "Coca-Cola",
                LocalDate.of(2026, 6, 28),
                List.of(
                        new InvoiceLine(
                                "458921",
                                "COCA COLA ORIGINAL 2.25L",
                                new BigDecimal("12"),
                                new BigDecimal("2000.00")
                        )
                )
        );

        List<Discrepancy> discrepancies =
                service.reconcile(purchaseOrder, invoice);

        assertEquals(1, discrepancies.size());

        Discrepancy discrepancy = discrepancies.getFirst();

        assertEquals(
                DiscrepancyType.QUANTITY_MISMATCH,
                discrepancy.type()
        );

        assertEquals(
                "458921",
                discrepancy.productReference()
        );
    }
}
