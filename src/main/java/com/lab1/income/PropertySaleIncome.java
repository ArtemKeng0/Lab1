package com.lab1.income;

public class PropertySaleIncome extends Income {

    public static final double SUBSEQUENT_SALE_RATE = 0.065;

    private final boolean isFirstSaleOfYear;

    public PropertySaleIncome(String sourceName, double amount, boolean isFirstSaleOfYear) {
        super(sourceName, amount);
        this.isFirstSaleOfYear = isFirstSaleOfYear;
    }

    public boolean isFirstSaleOfYear() {
        return isFirstSaleOfYear;
    }

    @Override
    public double calculateTax() {
        if (isFirstSaleOfYear) {
            return 0.0;
        }
        return Math.round(getAmount() * SUBSEQUENT_SALE_RATE * 100.0) / 100.0;
    }

    @Override
    public String getTaxDescription() {
        if (isFirstSaleOfYear) {
            return "First property sale of the year (tax exempt 0%)";
        }
        return "Subsequent property sale (6.5% rate)";
    }
}
