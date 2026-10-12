package com.lab1.income;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmploymentIncomeTest {

    @Test
    void testMainJobWithoutChildren() {
        EmploymentIncome income = new EmploymentIncome("Main Job", 10000.0, true, 0);
        assertTrue(income.isMainJob());
        assertEquals(0, income.getChildrenCount());
        assertEquals(1950.0, income.calculateTax());
    }

    @Test
    void testMainJobWithChildrenBenefit() {
        EmploymentIncome income = new EmploymentIncome("Main Job", 10000.0, true, 2);
        assertEquals(1365.0, income.calculateTax());
        assertTrue(income.getTaxDescription().contains("2 child benefit(s)"));
    }

    @Test
    void testMainJobBenefitExceedsAmount() {
        EmploymentIncome income = new EmploymentIncome("Main Job", 2000.0, true, 2);
        assertEquals(0.0, income.calculateTax());
    }

    @Test
    void testSecondaryJobDoesNotApplyChildBenefit() {
        EmploymentIncome income = new EmploymentIncome("Secondary Job", 10000.0, false, 2);
        assertFalse(income.isMainJob());
        assertEquals(1950.0, income.calculateTax());
    }

    @Test
    void testNegativeChildrenThrows() {
        assertThrows(IllegalArgumentException.class, () -> new EmploymentIncome("Job", 10000.0, true, -1));
    }
}
