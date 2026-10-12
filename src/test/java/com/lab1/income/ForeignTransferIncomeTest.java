package com.lab1.income;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ForeignTransferIncomeTest {

    @Test
    void testForeignTransferCalculation() {
        // 1000 USD * 40.0 = 40000 грн. Податок: 40000 * 0.195 = 7800.0 грн.
        ForeignTransferIncome transfer = new ForeignTransferIncome("Upwork", 1000.0, "USD", 40.0);
        assertEquals("USD", transfer.getCurrencyCode());
        assertEquals(40.0, transfer.getExchangeRate());
        assertEquals(40000.0, transfer.getAmountInUah());
        assertEquals(7800.0, transfer.calculateTax());
    }

    @Test
    void testInvalidRateOrCurrencyThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> new ForeignTransferIncome("Upwork", 1000.0, "USD", -1.0));
        assertThrows(IllegalArgumentException.class,
                () -> new ForeignTransferIncome("Upwork", 1000.0, "", 40.0));
    }
}
