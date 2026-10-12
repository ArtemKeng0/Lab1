package com.lab1.income;

public abstract class Income {

    private final String sourceName;
    private final double amount;

    public Income(String sourceName, double amount) {
        if (sourceName == null || sourceName.trim().isEmpty()) {
            throw new IllegalArgumentException("Income source name cannot be empty.");
        }
        if (amount < 0) {
            throw new IllegalArgumentException("Income amount cannot be negative: " + amount);
        }
        this.sourceName = sourceName.trim();
        this.amount = amount;
    }

    public String getSourceName() {
        return sourceName;
    }

    public double getAmount() {
        return amount;
    }

    public abstract double calculateTax();

    public abstract String getTaxDescription();

    @Override
    public String toString() {
        return String.format("%s [Source: '%s', Amount: %.2f, Tax: %.2f (%s)]",
                getClass().getSimpleName(), sourceName, amount, calculateTax(), getTaxDescription());
    }
}
