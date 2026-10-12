package com.lab1.income;

public class GiftIncome extends Income {

    public static final double STANDARD_GIFT_TAX_RATE = 0.065;

    private final boolean isImmediateFamily;

    public GiftIncome(String sourceName, double amount, boolean isImmediateFamily) {
        super(sourceName, amount);
        this.isImmediateFamily = isImmediateFamily;
    }

    public boolean isImmediateFamily() {
        return isImmediateFamily;
    }

    @Override
    public double calculateTax() {
        if (isImmediateFamily) {
            return 0.0;
        }
        return Math.round(getAmount() * STANDARD_GIFT_TAX_RATE * 100.0) / 100.0;
    }

    @Override
    public String getTaxDescription() {
        if (isImmediateFamily) {
            return "Gift from immediate family member (tax exempt 0%)";
        }
        return "Gift from other individuals (6.5% rate)";
    }
}
