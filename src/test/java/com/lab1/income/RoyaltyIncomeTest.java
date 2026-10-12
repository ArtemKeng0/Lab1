package com.lab1.income;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RoyaltyIncomeTest {

    @Test
    void testRoyaltyCalculationWithExpenses() {
        // Дохід 50000, витрати 10000. База = 40000. Податок = 40000 * 0.195 = 7800.0 грн.
        RoyaltyIncome royalty = new RoyaltyIncome("Видавництво", 50000.0, 10000.0);
        assertEquals(10000.0, royalty.getDocumentedExpenses());
        assertEquals(7800.0, royalty.calculateTax());
    }

    @Test
    void testRoyaltyExpensesExceedAmount() {
        RoyaltyIncome royalty = new RoyaltyIncome("Видавництво", 50000.0, 60000.0);
        assertEquals(0.0, royalty.calculateTax());
    }

    @Test
    void testNegativeExpensesThrows() {
        assertThrows(IllegalArgumentException.class, () -> new RoyaltyIncome("Видавництво", 50000.0, -100.0));
    }
}
