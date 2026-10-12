package com.lab1.income;

/**
 * Дохід від переказів з-за кордону (іноземні доходи).
 * Сума у валюті конвертується за офіційним курсом НБУ в національну валюту
 * та оподатковується за стандартною ставкою 19.5% (18% ПДФО + 1.5% Військовий збір).
 */
public class ForeignTransferIncome extends Income {

    public static final double TOTAL_TAX_RATE = 0.195; // 18% ПДФО + 1.5% Військовий збір

    private final double exchangeRate;
    private final String currencyCode;

    /**
     * Конструктор іноземного переказу.
     *
     * @param sourceName   джерело / призначення переказу
     * @param amount       сума в іноземній валюті
     * @param currencyCode код валюти (наприклад, "USD", "EUR")
     * @param exchangeRate курс валюти до гривні
     */
    public ForeignTransferIncome(String sourceName, double amount, String currencyCode, double exchangeRate) {
        super(sourceName, amount);
        if (exchangeRate <= 0) {
            throw new IllegalArgumentException("Курс валюти повинен бути більшим за нуль: " + exchangeRate);
        }
        if (currencyCode == null || currencyCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Код валюти не може бути порожнім.");
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

    /**
     * Повертає суму переказу в гривнях.
     *
     * @return еквівалент у гривнях
     */
    public double getAmountInUah() {
        return getAmount() * exchangeRate;
    }

    @Override
    public double calculateTax() {
        double amountInUah = getAmountInUah();
        return Math.round(amountInUah * TOTAL_TAX_RATE * 100.0) / 100.0;
    }

    @Override
    public String getTaxDescription() {
        return String.format("Іноземний переказ (%s за курсом %.2f, ставка 19.5%%)", currencyCode, exchangeRate);
    }
}
