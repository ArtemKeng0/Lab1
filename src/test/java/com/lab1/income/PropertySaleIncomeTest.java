package com.lab1.income;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PropertySaleIncomeTest {

    @Test
    void testFirstSaleTaxExempt() {
        PropertySaleIncome sale = new PropertySaleIncome("Квартира", 1000000.0, true);
        assertTrue(sale.isFirstSaleOfYear());
        assertEquals(0.0, sale.calculateTax());
        assertTrue(sale.getTaxDescription().contains("0%"));
    }

    @Test
    void testSubsequentSaleTaxedAt6Point5Percent() {
        // Повторний продаж: 100000 * 0.065 = 6500.0 грн
        PropertySaleIncome sale = new PropertySaleIncome("Автомобіль", 100000.0, false);
        assertFalse(sale.isFirstSaleOfYear());
        assertEquals(6500.0, sale.calculateTax());
        assertTrue(sale.getTaxDescription().contains("6.5%"));
    }
}
