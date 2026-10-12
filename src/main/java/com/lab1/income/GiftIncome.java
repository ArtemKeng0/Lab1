package com.lab1.income;

/**
 * Отримання в подарунок грошових сум або майна.
 * Подарунки від членів сім'ї першого ступеня споріднення оподатковуються за ставкою 0%.
 * Подарунки від інших осіб оподатковуються за ставкою 6.5% (5% ПДФО + 1.5% Військовий збір).
 */
public class GiftIncome extends Income {

    public static final double STANDARD_GIFT_TAX_RATE = 0.065; // 5% ПДФО + 1.5% ВЗ

    private final boolean isImmediateFamily;

    /**
     * Конструктор доходу у вигляді подарунка.
     *
     * @param sourceName        опис подарунка (наприклад, "Грошовий переказ від батьків", "Авто від колеги")
     * @param amount            вартість подарованого майна або грошова сума
     * @param isImmediateFamily чи отримано від члена сім'ї 1-го ступеня споріднення
     */
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
            return "Подарунок від найближчого родича (пільгова ставка 0%)";
        }
        return "Подарунок від інших осіб (6.5%: 5% ПДФО + 1.5% ВЗ)";
    }
}
