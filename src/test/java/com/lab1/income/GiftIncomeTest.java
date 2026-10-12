package com.lab1.income;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GiftIncomeTest {

    @Test
    void testImmediateFamilyGiftTaxExempt() {
        GiftIncome gift = new GiftIncome("Подарунок від батьків", 200000.0, true);
        assertTrue(gift.isImmediateFamily());
        assertEquals(0.0, gift.calculateTax());
        assertTrue(gift.getTaxDescription().contains("0%"));
    }

    @Test
    void testNonImmediateFamilyGiftTaxed() {
        // Від інших осіб: 100000 * 0.065 = 6500.0 грн
        GiftIncome gift = new GiftIncome("Подарунок від знайомого", 100000.0, false);
        assertFalse(gift.isImmediateFamily());
        assertEquals(6500.0, gift.calculateTax());
        assertTrue(gift.getTaxDescription().contains("6.5%"));
    }
}
