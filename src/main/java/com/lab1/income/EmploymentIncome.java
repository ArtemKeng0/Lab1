package com.lab1.income;

/**
 * Дохід від трудової діяльності (основне або додаткове місце роботи).
 * Враховує право на соціальну пільгу на дітей за основним місцем роботи.
 */
public class EmploymentIncome extends Income {

    public static final double TOTAL_TAX_RATE = 0.195; // 18% ПДФО + 1.5% Військовий збір
    public static final double BENEFIT_PER_CHILD = 1500.0; // Розмір пільги на 1 дитину

    private final boolean isMainJob;
    private final int childrenCount;

    /**
     * Конструктор доходу від зайнятості.
     *
     * @param sourceName    назва роботодавця / компанії
     * @param amount        нарахована заробітна плата
     * @param isMainJob     чи є це основним місцем роботи
     * @param childrenCount кількість дітей (для нарахування пільги)
     */
    public EmploymentIncome(String sourceName, double amount, boolean isMainJob, int childrenCount) {
        super(sourceName, amount);
        if (childrenCount < 0) {
            throw new IllegalArgumentException("Кількість дітей не може бути від'ємною: " + childrenCount);
        }
        this.isMainJob = isMainJob;
        this.childrenCount = childrenCount;
    }

    public boolean isMainJob() {
        return isMainJob;
    }

    public int getChildrenCount() {
        return childrenCount;
    }

    @Override
    public double calculateTax() {
        double taxableBase = getAmount();
        // Пільга на дітей застосовується виключно за основним місцем роботи
        if (isMainJob && childrenCount > 0) {
            double totalBenefit = childrenCount * BENEFIT_PER_CHILD;
            taxableBase = Math.max(0.0, taxableBase - totalBenefit);
        }
        return Math.round(taxableBase * TOTAL_TAX_RATE * 100.0) / 100.0;
    }

    @Override
    public String getTaxDescription() {
        if (isMainJob) {
            return String.format("Основне місце роботи (19.5%%, пільга на %d дітей)", childrenCount);
        }
        return "Сумісництво / додаткове місце (19.5%, без пільг)";
    }
}
