package com.lab1.income;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FinancialAidIncomeTest {

    @Test
    void testAidWithinLimitTaxExempt() {
        FinancialAidIncome aid = new FinancialAidIncome("Trade Union", 4000.0, 4240.0);
        assertEquals(4240.0, aid.getNonTaxableLimit());
        assertEquals(0.0, aid.calculateTax());
    }

    @Test
    void testAidExceedingLimitTaxedOnExcess() {
        FinancialAidIncome aid = new FinancialAidIncome("Trade Union", 10000.0, 4240.0);
        assertEquals(1123.20, aid.calculateTax());
    }

    @Test
    void testDefaultLimitConstructor() {
        FinancialAidIncome aid = new FinancialAidIncome("Fund", 2000.0);
        assertEquals(FinancialAidIncome.DEFAULT_NON_TAXABLE_LIMIT, aid.getNonTaxableLimit());
        assertEquals(0.0, aid.calculateTax());
    }

    @Test
    void testNegativeLimitThrows() {
        assertThrows(IllegalArgumentException.class, () -> new FinancialAidIncome("Fund", 1000.0, -5.0));
    }
}
