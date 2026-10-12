package com.lab1.income;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IncomeTest {

    private static class TestIncome extends Income {
        public TestIncome(String sourceName, double amount) {
            super(sourceName, amount);
        }

        @Override
        public double calculateTax() {
            return getAmount() * 0.1;
        }

        @Override
        public String getTaxDescription() {
            return "Тестовий податок 10%";
        }
    }

    @Test
    void testValidIncomeCreation() {
        Income income = new TestIncome("Тестове джерело", 1000.0);
        assertEquals("Тестове джерело", income.getSourceName());
        assertEquals(1000.0, income.getAmount());
        assertEquals(100.0, income.calculateTax());
        assertEquals("Тестовий податок 10%", income.getTaxDescription());
        assertTrue(income.toString().contains("Тестове джерело"));
    }

    @Test
    void testInvalidIncomeCreationThrows() {
        assertThrows(IllegalArgumentException.class, () -> new TestIncome(null, 100.0));
        assertThrows(IllegalArgumentException.class, () -> new TestIncome("   ", 100.0));
        assertThrows(IllegalArgumentException.class, () -> new TestIncome("Джерело", -50.0));
    }
}
