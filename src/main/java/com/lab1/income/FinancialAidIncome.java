package com.lab1.income;

/**
 * Нецільова матеріальна допомога.
 * Згідно із законодавством встановлюється неоподатковуваний ліміт на рік.
 * Сума допомоги в межах ліміту звільнена від оподаткування (0%).
 * Оподатковується лише перевищення ліміту за ставкою 19.5% (18% ПДФО + 1.5% Військовий збір).
 */
public class FinancialAidIncome extends Income {

    public static final double TOTAL_TAX_RATE = 0.195; // 18% ПДФО + 1.5% Військовий збір
    public static final double DEFAULT_NON_TAXABLE_LIMIT = 4240.0; // Граничний річний неоподатковуваний розмір

    private final double nonTaxableLimit;

    /**
     * Конструктор матеріальної допомоги із зазначенням ліміту.
     *
     * @param sourceName      джерело надання допомоги (наприклад, профспілка або фонд)
     * @param amount          сума матеріальної допомоги
     * @param nonTaxableLimit розмір неоподатковуваного ліміту
     */
    public FinancialAidIncome(String sourceName, double amount, double nonTaxableLimit) {
        super(sourceName, amount);
        if (nonTaxableLimit < 0) {
            throw new IllegalArgumentException("Неоподатковуваний ліміт не може бути від'ємним: " + nonTaxableLimit);
        }
        this.nonTaxableLimit = nonTaxableLimit;
    }

    /**
     * Конструктор матеріальної допомоги зі стандартним лімітом.
     *
     * @param sourceName джерело надання допомоги
     * @param amount     сума матеріальної допомоги
     */
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
        return String.format("Матеріальна допомога (неоподатковуваний ліміт: %.2f грн, ставка на надлишок: 19.5%%)",
                nonTaxableLimit);
    }
}
