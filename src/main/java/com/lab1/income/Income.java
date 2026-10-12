package com.lab1.income;

/**
 * Базовий абстрактний клас, що представляє джерело доходу фізичної особи.
 * Демонструє інкапсуляцію спільних полів та оголошує поліморфний контракт
 * для розрахунку податку.
 */
public abstract class Income {

    private final String sourceName;
    private final double amount;

    /**
     * Конструктор базового доходу.
     *
     * @param sourceName назва або джерело доходу
     * @param amount     сума доходу (не може бути від'ємною)
     */
    public Income(String sourceName, double amount) {
        if (sourceName == null || sourceName.trim().isEmpty()) {
            throw new IllegalArgumentException("Назва джерела доходу не може бути порожньою.");
        }
        if (amount < 0) {
            throw new IllegalArgumentException("Сума доходу не може бути від'ємною: " + amount);
        }
        this.sourceName = sourceName.trim();
        this.amount = amount;
    }

    public String getSourceName() {
        return sourceName;
    }

    public double getAmount() {
        return amount;
    }

    /**
     * Поліморфний метод обчислення податкових виплат.
     * Кожен підклас реалізує специфічні правила оподаткування.
     *
     * @return розрахована сума податку
     */
    public abstract double calculateTax();

    /**
     * Повертає опис типу податку або застосованої ставки.
     *
     * @return текстовий опис податку
     */
    public abstract String getTaxDescription();

    @Override
    public String toString() {
        return String.format("%s [Джерело: '%s', Сума: %.2f грн, Податок: %.2f грн (%s)]",
                getClass().getSimpleName(), sourceName, amount, calculateTax(), getTaxDescription());
    }
}
