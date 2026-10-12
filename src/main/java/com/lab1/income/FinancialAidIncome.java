package com.lab1.income;

public class FinancialAidIncome extends Income {

    public static final double TOTAL_TAX_RATE = 0.195;
    public static final double DEFAULT_NON_TAXABLE_LIMIT = 4240.0;

    private final double nonTaxableLimit;

    public FinancialAidIncome(String sourceName, double amount, double nonTaxableLimit) {
        super(sourceName, amount);
        if (nonTaxableLimit < 0) {
            throw new IllegalArgumentException("Non-taxable limit cannot be negative: " + nonTaxableLimit);
        }
        this.nonTaxableLimit = nonTaxableLimit;
    }

    public FinancialAidIncome(String sourceName, double amount) {
        this(sourceName, amount, DEFAULT_NON_TAXABLE_LIMIT);
    }

    public double getNonTaxableLimit() {
        return nonTaxableLimit;
    }

    @Override
    public double calculateTax() {
        double excessAmount = Math.max(0.0, getAmount() - nonTaxableLimit);
        return Math.round(excessAmount * TOTAL_TAX_RATE * 100.0) / 100.0;
    }

    @Override
    public String getTaxDescription() {
        return String.format("Financial aid (limit: %.2f, 19.5%% tax on excess)", nonTaxableLimit);
    }
}
