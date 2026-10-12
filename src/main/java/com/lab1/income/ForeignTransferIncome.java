package com.lab1.income;

public class ForeignTransferIncome extends Income {

    public static final double TOTAL_TAX_RATE = 0.195;

    private final double exchangeRate;
    private final String currencyCode;

    public ForeignTransferIncome(String sourceName, double amount, String currencyCode, double exchangeRate) {
        super(sourceName, amount);
        if (exchangeRate <= 0) {
            throw new IllegalArgumentException("Exchange rate must be greater than zero: " + exchangeRate);
        }
        if (currencyCode == null || currencyCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Currency code cannot be empty.");
        }
        this.currencyCode = currencyCode.trim().toUpperCase();
        this.exchangeRate = exchangeRate;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public double getAmountInLocalCurrency() {
        return getAmount() * exchangeRate;
    }

    @Override
    public double calculateTax() {
        double localAmount = getAmountInLocalCurrency();
        return Math.round(localAmount * TOTAL_TAX_RATE * 100.0) / 100.0;
    }

    @Override
    public String getTaxDescription() {
        return String.format("Foreign transfer (%s at rate %.2f, 19.5%% tax rate)", currencyCode, exchangeRate);
    }
}
