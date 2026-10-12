package com.lab1.tax;

import com.lab1.income.Income;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class TaxDeclaration {

    private final String taxpayerName;
    private final List<Income> incomes;

    public TaxDeclaration(String taxpayerName) {
        if (taxpayerName == null || taxpayerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Taxpayer name cannot be empty.");
        }
        this.taxpayerName = taxpayerName.trim();
        this.incomes = new ArrayList<>();
    }

    public String getTaxpayerName() {
        return taxpayerName;
    }

    public void addIncome(Income income) {
        if (income == null) {
            throw new IllegalArgumentException("Income cannot be null.");
        }
        this.incomes.add(income);
    }

    public List<Income> getIncomes() {
        return Collections.unmodifiableList(incomes);
    }

    public Set<TaxPayment> getTaxPayments() {
        Set<TaxPayment> payments = new LinkedHashSet<>();
        for (Income income : incomes) {
            payments.add(new TaxPayment(
                    income.getSourceName(),
                    income.getTaxDescription(),
                    income.calculateTax()
            ));
        }
        return payments;
    }

    public double calculateTotalTax() {
        double total = 0.0;
        for (Income income : incomes) {
            total += income.calculateTax();
        }
        return Math.round(total * 100.0) / 100.0;
    }

    public List<TaxPayment> getSortedTaxes() {
        List<TaxPayment> list = new ArrayList<>(getTaxPayments());
        Collections.sort(list);
        return list;
    }

    public List<TaxPayment> getSortedTaxesDescending() {
        List<TaxPayment> list = new ArrayList<>(getTaxPayments());
        list.sort(Collections.reverseOrder());
        return list;
    }

    public List<TaxPayment> findTaxesByAmountRange(double minAmount, double maxAmount) {
        if (minAmount > maxAmount) {
            throw new IllegalArgumentException("Minimum amount cannot exceed maximum amount.");
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
