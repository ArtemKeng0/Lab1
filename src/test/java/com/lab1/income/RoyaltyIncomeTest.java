package com.lab1.income;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RoyaltyIncomeTest {

    @Test
    void testRoyaltyCalculationWithExpenses() {
        RoyaltyIncome royalty = new RoyaltyIncome("Publishing House", 50000.0, 10000.0);
        assertEquals(10000.0, royalty.getDocumentedExpenses());
        assertEquals(7800.0, royalty.calculateTax());
    }

    @Test
    void testRoyaltyExpensesExceedAmount() {
        RoyaltyIncome royalty = new RoyaltyIncome("Publishing House", 50000.0, 60000.0);
        assertEquals(0.0, royalty.calculateTax());
    }

    @Test
    void testNegativeExpensesThrows() {
        assertThrows(IllegalArgumentException.class, () -> new RoyaltyIncome("Publishing House", 50000.0, -100.0));
    }
}
