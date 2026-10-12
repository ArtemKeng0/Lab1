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
            return "Test tax 10%";
        }
    }

    @Test
    void testValidIncomeCreation() {
        Income income = new TestIncome("Test Source", 1000.0);
        assertEquals("Test Source", income.getSourceName());
        assertEquals(1000.0, income.getAmount());
        assertEquals(100.0, income.calculateTax());
        assertEquals("Test tax 10%", income.getTaxDescription());
        assertTrue(income.toString().contains("Test Source"));
    }

    @Test
    void testInvalidIncomeCreationThrows() {
        assertThrows(IllegalArgumentException.class, () -> new TestIncome(null, 100.0));
        assertThrows(IllegalArgumentException.class, () -> new TestIncome("   ", 100.0));
        assertThrows(IllegalArgumentException.class, () -> new TestIncome("Source", -50.0));
    }
}
