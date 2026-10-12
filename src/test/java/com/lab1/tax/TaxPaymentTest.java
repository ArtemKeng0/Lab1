package com.lab1.tax;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaxPaymentTest {

    @Test
    void testValidCreation() {
        TaxPayment payment = new TaxPayment("Зарплата", "ПДФО 18%", 5400.0);
        assertEquals("Зарплата", payment.getSourceName());
        assertEquals("ПДФО 18%", payment.getTaxDescription());
        assertEquals(5400.0, payment.getTaxAmount());
    }

    @Test
    void testInvalidCreationThrows() {
        assertThrows(IllegalArgumentException.class, () -> new TaxPayment(null, "ПДФО", 100.0));
        assertThrows(IllegalArgumentException.class, () -> new TaxPayment("  ", "ПДФО", 100.0));
        assertThrows(IllegalArgumentException.class, () -> new TaxPayment("Зарплата", "ПДФО", -1.0));
    }

    @Test
    void testCompareTo() {
        TaxPayment p1 = new TaxPayment("A", "Tax", 100.0);
        TaxPayment p2 = new TaxPayment("B", "Tax", 200.0);
        TaxPayment p3 = new TaxPayment("C", "Tax", 100.0);

        assertTrue(p1.compareTo(p2) < 0);
        assertTrue(p2.compareTo(p1) > 0);
        assertTrue(p1.compareTo(p3) < 0); // за назвою "A" < "C"
    }

    @Test
    void testEqualsAndHashCode() {
        TaxPayment p1 = new TaxPayment("A", "Tax", 100.0);
        TaxPayment p2 = new TaxPayment("A", "Tax", 100.0);
        TaxPayment p3 = new TaxPayment("B", "Tax", 100.0);

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
        assertNotEquals(p1, p3);
    }
}
