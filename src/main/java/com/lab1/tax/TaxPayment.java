package com.lab1.tax;

import java.util.Objects;

public class TaxPayment implements Comparable<TaxPayment> {

    private final String sourceName;
    private final String taxDescription;
    private final double taxAmount;

    public TaxPayment(String sourceName, String taxDescription, double taxAmount) {
        if (sourceName == null || sourceName.trim().isEmpty()) {
            throw new IllegalArgumentException("Source name cannot be empty.");
        }
        if (taxAmount < 0) {
            throw new IllegalArgumentException("Tax amount cannot be negative: " + taxAmount);
        }
        this.sourceName = sourceName.trim();
        this.taxDescription = (taxDescription != null) ? taxDescription.trim() : "";
        this.taxAmount = Math.round(taxAmount * 100.0) / 100.0;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getTaxDescription() {
        return taxDescription;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    @Override
    public int compareTo(TaxPayment other) {
        if (other == null) {
            return 1;
        }
        int cmp = Double.compare(this.taxAmount, other.taxAmount);
        if (cmp != 0) {
            return cmp;
        }
        return this.sourceName.compareTo(other.sourceName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TaxPayment that)) {
            return false;
        }
        return Double.compare(that.taxAmount, taxAmount) == 0 &&
                Objects.equals(sourceName, that.sourceName) &&
                Objects.equals(taxDescription, that.taxDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceName, taxDescription, taxAmount);
    }

    @Override
    public String toString() {
        return String.format("Tax: %.2f | Source: %-30s | Note: %s",
                taxAmount, sourceName, taxDescription);
    }
}
