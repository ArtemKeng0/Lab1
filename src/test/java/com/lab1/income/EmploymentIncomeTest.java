package com.lab1.income;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmploymentIncomeTest {

    @Test
    void testMainJobWithoutChildren() {
        EmploymentIncome income = new EmploymentIncome("Основна робота", 10000.0, true, 0);
        assertTrue(income.isMainJob());
        assertEquals(0, income.getChildrenCount());
        // 10000 * 0.195 = 1950.0
        assertEquals(1950.0, income.calculateTax());
    }

    @Test
    void testMainJobWithChildrenBenefit() {
        // 2 дітей: пільга 2 * 1500 = 3000 грн. Оподатковувана база = 10000 - 3000 = 7000 грн.
        // Податок: 7000 * 0.195 = 1365.0 грн.
        EmploymentIncome income = new EmploymentIncome("Основна робота", 10000.0, true, 2);
        assertEquals(1365.0, income.calculateTax());
        assertTrue(income.getTaxDescription().contains("пільга на 2 дітей"));
    }

    @Test
    void testMainJobBenefitExceedsAmount() {
        // База не може стати від'ємною
        EmploymentIncome income = new EmploymentIncome("Основна робота", 2000.0, true, 2);
        assertEquals(0.0, income.calculateTax());
    }

    @Test
    void testSecondaryJobDoesNotApplyChildBenefit() {
        // Додаткове місце роботи: пільга не застосовується навіть за наявності дітей
        EmploymentIncome income = new EmploymentIncome("Сумісництво", 10000.0, false, 2);
        assertFalse(income.isMainJob());
        assertEquals(1950.0, income.calculateTax());
    }

    @Test
    void testNegativeChildrenThrows() {
        assertThrows(IllegalArgumentException.class, () -> new EmploymentIncome("Робота", 10000.0, true, -1));
    }
}
