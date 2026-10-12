package com.lab1.tax;

import com.lab1.income.Income;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaxDeclarationTest {

    private TaxDeclaration declaration;

    @Mock
    private Income mockIncome1;

    @Mock
    private Income mockIncome2;

    @BeforeEach
    void setUp() {
        declaration = new TaxDeclaration("Іванов Іван");
    }

    @Test
    void testConstructorValid() {
        assertEquals("Іванов Іван", declaration.getTaxpayerName());
        assertTrue(declaration.getIncomes().isEmpty());
    }

    @Test
    void testConstructorInvalidNameThrows() {
        assertThrows(IllegalArgumentException.class, () -> new TaxDeclaration(null));
        assertThrows(IllegalArgumentException.class, () -> new TaxDeclaration("   "));
    }

    @Test
    void testAddIncomeAndCalculateTotalTaxWithMockito() {
        when(mockIncome1.calculateTax()).thenReturn(1500.0);
        when(mockIncome2.calculateTax()).thenReturn(3500.0);

        declaration.addIncome(mockIncome1);
        declaration.addIncome(mockIncome2);

        assertEquals(2, declaration.getIncomes().size());
        assertEquals(5000.0, declaration.calculateTotalTax());

        verify(mockIncome1).calculateTax();
        verify(mockIncome2).calculateTax();
    }

    @Test
    void testAddNullIncomeThrows() {
        assertThrows(IllegalArgumentException.class, () -> declaration.addIncome(null));
    }

    @Test
    void testGetTaxPaymentsReturnsSet() {
        when(mockIncome1.getSourceName()).thenReturn("Робота");
        when(mockIncome1.getTaxDescription()).thenReturn("ПДФО");
        when(mockIncome1.calculateTax()).thenReturn(2000.0);

        declaration.addIncome(mockIncome1);
        Set<TaxPayment> payments = declaration.getTaxPayments();

        assertNotNull(payments);
        assertEquals(1, payments.size());
        TaxPayment p = payments.iterator().next();
        assertEquals("Робота", p.getSourceName());
        assertEquals(2000.0, p.getTaxAmount());
    }

    @Test
    void testSortingTaxes() {
        when(mockIncome1.getSourceName()).thenReturn("Джерело А");
        when(mockIncome1.getTaxDescription()).thenReturn("Ставка 1");
        when(mockIncome1.calculateTax()).thenReturn(500.0);

        when(mockIncome2.getSourceName()).thenReturn("Джерело Б");
        when(mockIncome2.getTaxDescription()).thenReturn("Ставка 2");
        when(mockIncome2.calculateTax()).thenReturn(1500.0);

        declaration.addIncome(mockIncome2); // спочатку більше
        declaration.addIncome(mockIncome1); // потім менше

        List<TaxPayment> ascending = declaration.getSortedTaxes();
        assertEquals(500.0, ascending.get(0).getTaxAmount());
        assertEquals(1500.0, ascending.get(1).getTaxAmount());

        List<TaxPayment> descending = declaration.getSortedTaxesDescending();
        assertEquals(1500.0, descending.get(0).getTaxAmount());
        assertEquals(500.0, descending.get(1).getTaxAmount());
    }

    @Test
    void testFindTaxesByAmountRange() {
        when(mockIncome1.getSourceName()).thenReturn("Джерело А");
        when(mockIncome1.calculateTax()).thenReturn(300.0);

        when(mockIncome2.getSourceName()).thenReturn("Джерело Б");
        when(mockIncome2.calculateTax()).thenReturn(1200.0);

        declaration.addIncome(mockIncome1);
        declaration.addIncome(mockIncome2);

        List<TaxPayment> found = declaration.findTaxesByAmountRange(500.0, 1500.0);
        assertEquals(1, found.size());
        assertEquals(1200.0, found.get(0).getTaxAmount());
    }

    @Test
    void testFindTaxesInvalidRangeThrows() {
        assertThrows(IllegalArgumentException.class, () -> declaration.findTaxesByAmountRange(100.0, 50.0));
    }
}
