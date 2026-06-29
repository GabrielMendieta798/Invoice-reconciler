package com.gabriel.reconciler.application;

import com.gabriel.reconciler.domain.Discrepancy;
import com.gabriel.reconciler.domain.DiscrepancyType;
import com.gabriel.reconciler.domain.Invoice;
import com.gabriel.reconciler.domain.InvoiceLine;
import com.gabriel.reconciler.domain.PurchaseOrder;
import com.gabriel.reconciler.domain.PurchaseOrderLine;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InvoiceReconciliationService {

    private final ProductMatcher productMatcher;

    public InvoiceReconciliationService() {
        this(new ProductMatcher());
    }

    public InvoiceReconciliationService(ProductMatcher productMatcher) {
        this.productMatcher = Objects.requireNonNull(
                productMatcher,
                "Product matcher is required"
        );
    }

    public List<Discrepancy> reconcile(
            PurchaseOrder purchaseOrder,
            Invoice invoice
    ) {
        Objects.requireNonNull(purchaseOrder, "Purchase order is required");
        Objects.requireNonNull(invoice, "Invoice is required");

        List<Discrepancy> discrepancies = new ArrayList<>();
        List<InvoiceLine> unmatchedInvoiceLines =
                new ArrayList<>(invoice.lines());

        for (PurchaseOrderLine orderLine : purchaseOrder.lines()) {
            InvoiceLine matchingInvoiceLine = unmatchedInvoiceLines.stream()
                    .filter(invoiceLine -> productMatcher.matches(
                            orderLine,
                            invoiceLine
                    ))
                    .findFirst()
                    .orElse(null);

            if (matchingInvoiceLine == null) {
                discrepancies.add(new Discrepancy(
                        productReference(orderLine),
                        DiscrepancyType.MISSING_FROM_INVOICE,
                        "Product from purchase order was not found in invoice"
                ));
                continue;
            }

            unmatchedInvoiceLines.remove(matchingInvoiceLine);

            if (orderLine.expectedQuantity()
                    .compareTo(matchingInvoiceLine.actualQuantity()) != 0) {

                discrepancies.add(new Discrepancy(
                        productReference(orderLine),
                        DiscrepancyType.QUANTITY_MISMATCH,
                        "Expected quantity: " + orderLine.expectedQuantity()
                                + ", actual quantity: "
                                + matchingInvoiceLine.actualQuantity()
                ));
            }
        }

        for (InvoiceLine invoiceLine : unmatchedInvoiceLines) {
            discrepancies.add(new Discrepancy(
                    productReference(invoiceLine),
                    DiscrepancyType.UNEXPECTED_IN_INVOICE,
                    "Product from invoice was not found in purchase order"
            ));
        }

        return List.copyOf(discrepancies);
    }

    private String productReference(PurchaseOrderLine line) {
        if (hasText(line.supplierProductCode())) {
            return line.supplierProductCode();
        }

        return line.description();
    }

    private String productReference(InvoiceLine line) {
        if (hasText(line.supplierProductCode())) {
            return line.supplierProductCode();
        }

        return line.description();
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}