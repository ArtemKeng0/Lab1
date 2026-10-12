package com.lab1.tax;

import java.util.Objects;

/**
 * Клас, що представляє окреме податкове зобов'язання (платіж).
 * Реалізує інтерфейс Comparable для природного сортування за сумою податку.
 */
public class TaxPayment implements Comparable<TaxPayment> {

    private final String sourceName;
    private final String taxDescription;
    private final double taxAmount;

    /**
     * Конструктор податкового платежу.
     *
     * @param sourceName     джерело доходу
     * @param taxDescription опис ставки або типу податку
     * @param taxAmount      сума податкової виплати
     */
    public TaxPayment(String sourceName, String taxDescription, double taxAmount) {
        if (sourceName == null || sourceName.trim().isEmpty()) {
            throw new IllegalArgumentException("Назва джерела не може бути порожньою.");
        }
        if (taxAmount < 0) {
            throw new IllegalArgumentException("Сума податку не може бути від'ємною: " + taxAmount);
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

    /**
     * Порівняння за сумою податку для забезпечення сортування.
     */
    @Override
    public int compareTo(TaxPayment other) {
        if (other == null) {
            return 1;
        }
        int cmp = Double.compare(this.taxAmount, other.taxAmount);
        if (cmp != 0) {
            return cmp;
        }
        // За однакової суми порівнюємо за назвою джерела
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
        return String.format("Податок: %.2f грн | Джерело: %-25s | Примітка: %s",
                taxAmount, sourceName, taxDescription);
    }
}
