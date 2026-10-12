package com.lab1.income;

/**
 * Дохід від продажу рухомого чи нерухомого майна.
 * Перший продаж майна протягом звітного року звільняється від оподаткування (0%).
 * Наступні продажі оподатковуються за зниженою ставкою 6.5% (5% ПДФО + 1.5% Військовий збір).
 */
public class PropertySaleIncome extends Income {

    public static final double SUBSEQUENT_SALE_RATE = 0.065; // 5% ПДФО + 1.5% ВЗ

    private final boolean isFirstSaleOfYear;

    /**
     * Конструктор доходу від відчуження майна.
     *
     * @param sourceName         опис майна (наприклад, "Квартира", "Автомобіль")
     * @param amount             сума доходу від продажу
     * @param isFirstSaleOfYear  чи є цей продаж першим за звітний рік
     */
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
            return "Перший продаж майна за рік (пільгова ставка 0%)";
        }
        return "Повторний продаж майна (6.5%: 5% ПДФО + 1.5% ВЗ)";
    }
}
