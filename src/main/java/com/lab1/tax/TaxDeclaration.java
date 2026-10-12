package com.lab1.tax;

import com.lab1.income.Income;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Річна податкова декларація фізичної особи.
 * Зберігає перелік доходів, формує множину податкових виплат,
 * обчислює загальну суму та надає методи сортування і пошуку податків.
 */
public class TaxDeclaration {

    private final String taxpayerName;
    private final List<Income> incomes;

    /**
     * Конструктор декларації платника податків.
     *
     * @param taxpayerName ПІБ платника податків
     */
    public TaxDeclaration(String taxpayerName) {
        if (taxpayerName == null || taxpayerName.trim().isEmpty()) {
            throw new IllegalArgumentException("ПІБ платника податків не може бути порожнім.");
        }
        this.taxpayerName = taxpayerName.trim();
        this.incomes = new ArrayList<>();
    }

    public String getTaxpayerName() {
        return taxpayerName;
    }

    /**
     * Додає новий дохід до декларації.
     *
     * @param income об'єкт доходу
     */
    public void addIncome(Income income) {
        if (income == null) {
            throw new IllegalArgumentException("Об'єкт доходу не може бути null.");
        }
        this.incomes.add(income);
    }

    /**
     * Повертає незмінний список зареєстрованих доходів.
     *
     * @return список доходів
     */
    public List<Income> getIncomes() {
        return Collections.unmodifiableList(incomes);
    }

    /**
     * Формує множину (Set) усіх податкових виплат фізичної особи.
     * Множина гарантує відсутність дублікатів однакових нарахувань.
     *
     * @return множина податкових платежів
     */
    public Set<TaxPayment> getTaxPayments() {
        Set<TaxPayment> payments = new LinkedHashSet<>();
        for (Income income : incomes) {
            TaxPayment payment = new TaxPayment(
                    income.getSourceName(),
                    income.getTaxDescription(),
                    income.calculateTax()
            );
            payments.add(payment);
        }
        return payments;
    }

    /**
     * Обчислює загальну суму всіх нарахованих податкових виплат за рік.
     *
     * @return загальна сума податків
     */
    public double calculateTotalTax() {
        double total = 0.0;
        for (Income income : incomes) {
            total += income.calculateTax();
        }
        return Math.round(total * 100.0) / 100.0;
    }

    /**
     * Повертає список податків, відсортований за зростанням суми.
     *
     * @return відсортований список податкових платежів
     */
    public List<TaxPayment> getSortedTaxes() {
        List<TaxPayment> sortedList = new ArrayList<>(getTaxPayments());
        Collections.sort(sortedList);
        return sortedList;
    }

    /**
     * Повертає список податків, відсортований за спаданням суми.
     *
     * @return відсортований список податкових платежів
     */
    public List<TaxPayment> getSortedTaxesDescending() {
        List<TaxPayment> sortedList = new ArrayList<>(getTaxPayments());
        sortedList.sort(Collections.reverseOrder());
        return sortedList;
    }

    /**
     * Знаходить усі податкові платежі, сума яких потрапляє у заданий діапазон.
     *
     * @param minAmount мінімальна межа (включно)
     * @param maxAmount максимальна межа (включно)
     * @return список знайдених платежів
     */
    public List<TaxPayment> findTaxesByAmountRange(double minAmount, double maxAmount) {
        if (minAmount > maxAmount) {
            throw new IllegalArgumentException("Мінімальна сума не може перевищувати максимальну.");
        }
        List<TaxPayment> matches = new ArrayList<>();
        for (TaxPayment payment : getTaxPayments()) {
            if (payment.getTaxAmount() >= minAmount && payment.getTaxAmount() <= maxAmount) {
                matches.add(payment);
            }
        }
        return matches;
    }
}
