package com.gabriel.reconciler.application;

import com.gabriel.reconciler.domain.InvoiceLine;
import com.gabriel.reconciler.domain.PurchaseOrderLine;

import java.text.Normalizer;
import java.util.Locale;
import java.util.Objects;

public class ProductMatcher {

    public boolean matches(
            PurchaseOrderLine purchaseOrderLine,
            InvoiceLine invoiceLine
    ) {
        Objects.requireNonNull(purchaseOrderLine, "Purchase order line is required");
        Objects.requireNonNull(invoiceLine, "Invoice line is required");

        String orderCode = purchaseOrderLine.supplierProductCode();
        String invoiceCode = invoiceLine.supplierProductCode();

        // Si ambos documentos tienen código, ese código manda.
        if (hasText(orderCode) && hasText(invoiceCode)) {
            return normalizeCode(orderCode)
                    .equals(normalizeCode(invoiceCode));
        }

        // Si falta el código en alguno, comparamos nombres normalizados.
        return normalizeDescription(purchaseOrderLine.description())
                .equals(normalizeDescription(invoiceLine.description()));
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private String normalizeCode(String code) {
        return code
                .trim()
                .toUpperCase(Locale.ROOT)
                .replaceAll("\\s+", "");
    }

    private String normalizeDescription(String description) {
        String withoutAccents = Normalizer.normalize(
                description,
                Normalizer.Form.NFD
        ).replaceAll("\\p{M}", "");

        return withoutAccents
                .toUpperCase(Locale.ROOT)
                .replaceAll("[^A-Z0-9]", "");
    }
}