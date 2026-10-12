package com.lab1.income;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PropertySaleIncomeTest {

    @Test
    void testFirstSaleTaxExempt() {
        PropertySaleIncome sale = new PropertySaleIncome("Apartment", 1000000.0, true);
        assertTrue(sale.isFirstSaleOfYear());
        assertEquals(0.0, sale.calculateTax());
        assertTrue(sale.getTaxDescription().contains("0%"));
    }

    @Test
    void testSubsequentSaleTaxedAt6Point5Percent() {
        PropertySaleIncome sale = new PropertySaleIncome("Car", 100000.0, false);
        assertFalse(sale.isFirstSaleOfYear());
        assertEquals(6500.0, sale.calculateTax());
        assertTrue(sale.getTaxDescription().contains("6.5%"));
    }
}
