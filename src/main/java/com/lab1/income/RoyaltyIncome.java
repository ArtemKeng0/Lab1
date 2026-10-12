package com.lab1.income;

/**
 * Авторська винагорода (роялті) за створення та використання творів науки, літератури, мистецтва.
 * Дозволяє зменшити базу оподаткування на документально підтверджені витрати автора.
 */
public class RoyaltyIncome extends Income {

    public static final double TOTAL_TAX_RATE = 0.195; // 18% ПДФО + 1.5% Військовий збір

    private final double documentedExpenses;

    /**
     * Конструктор авторської винагороди.
     *
     * @param sourceName         назва твору або видавництва/замовника
     * @param amount             сума роялті
     * @param documentedExpenses документально підтверджені витрати автора
     */
    public RoyaltyIncome(String sourceName, double amount, double documentedExpenses) {
        super(sourceName, amount);
        if (documentedExpenses < 0) {
            throw new IllegalArgumentException("Витрати автора не можуть бути від'ємними: " + documentedExpenses);
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
        return String.format("Роялті 19.5%% (витрати: %.2f грн)", documentedExpenses);
    }
}
