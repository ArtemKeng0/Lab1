package com.lab1.income;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FinancialAidIncomeTest {

    @Test
    void testAidWithinLimitTaxExempt() {
        FinancialAidIncome aid = new FinancialAidIncome("Профспілка", 4000.0, 4240.0);
        assertEquals(4240.0, aid.getNonTaxableLimit());
        assertEquals(0.0, aid.calculateTax());
    }

    @Test
    void testAidExceedingLimitTaxedOnExcess() {
        // Допомога 10000, ліміт 4240. Надлишок = 5760. Податок = 5760 * 0.195 = 1123.20 грн.
        FinancialAidIncome aid = new FinancialAidIncome("Профспілка", 10000.0, 4240.0);
        assertEquals(1123.20, aid.calculateTax());
    }

    @Test
    void testDefaultLimitConstructor() {
        FinancialAidIncome aid = new FinancialAidIncome("Фонд", 2000.0);
        assertEquals(FinancialAidIncome.DEFAULT_NON_TAXABLE_LIMIT, aid.getNonTaxableLimit());
        assertEquals(0.0, aid.calculateTax());
    }

    @Test
    void testNegativeLimitThrows() {
        assertThrows(IllegalArgumentException.class, () -> new FinancialAidIncome("Фонд", 1000.0, -5.0));
    }
}
