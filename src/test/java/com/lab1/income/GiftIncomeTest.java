package com.lab1.income;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GiftIncomeTest {

    @Test
    void testImmediateFamilyGiftTaxExempt() {
        GiftIncome gift = new GiftIncome("Gift from Parents", 200000.0, true);
        assertTrue(gift.isImmediateFamily());
        assertEquals(0.0, gift.calculateTax());
        assertTrue(gift.getTaxDescription().contains("0%"));
    }

    @Test
    void testNonImmediateFamilyGiftTaxed() {
        GiftIncome gift = new GiftIncome("Gift from Acquaintance", 100000.0, false);
        assertFalse(gift.isImmediateFamily());
        assertEquals(6500.0, gift.calculateTax());
        assertTrue(gift.getTaxDescription().contains("6.5%"));
    }
}
