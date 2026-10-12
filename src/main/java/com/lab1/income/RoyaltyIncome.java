package com.lab1.income;

public class RoyaltyIncome extends Income {

    public static final double TOTAL_TAX_RATE = 0.195;

    private final double documentedExpenses;

    public RoyaltyIncome(String sourceName, double amount, double documentedExpenses) {
        super(sourceName, amount);
        if (documentedExpenses < 0) {
            throw new IllegalArgumentException("Documented expenses cannot be negative: " + documentedExpenses);
        }
        this.documentedExpenses = documentedExpenses;
    }

    public double getDocumentedExpenses() {
        return documentedExpenses;
    }

    @Override
    public double calculateTax() {
        double taxableBase = Math.max(0.0, getAmount() - documentedExpenses);
        return Math.round(taxableBase * TOTAL_TAX_RATE * 100.0) / 100.0;
    }

    @Override
    public String getTaxDescription() {
        return String.format("Royalty income 19.5%% (documented expenses: %.2f)", documentedExpenses);
    }
}
