package com.lab1.app;

import com.lab1.config.XMLConfigLoader;
import com.lab1.income.EmploymentIncome;
import com.lab1.income.FinancialAidIncome;
import com.lab1.income.ForeignTransferIncome;
import com.lab1.income.GiftIncome;
import com.lab1.income.Income;
import com.lab1.income.PropertySaleIncome;
import com.lab1.income.RoyaltyIncome;
import com.lab1.tax.TaxDeclaration;
import com.lab1.tax.TaxPayment;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Головний клас консольного додатку.
 * Реалізує мінімальне консольне меню для демонстрації можливостей програми.
 */
public class Main {

    public static void main(String[] args) {
        // Забезпечуємо коректне виведення символів Unicode в консолі Windows
        try {
            System.setOut(new java.io.PrintStream(System.out, true, StandardCharsets.UTF_8));
        } catch (Exception ignored) {
        }

        System.out.println("================================================================================");
        System.out.println("   СИСТЕМА РОЗРАХУНКУ РІЧНИХ ПОДАТКОВИХ ВИПЛАТ ФІЗИЧНОЇ ОСОБИ (ЛАБ. 1)");
        System.out.println("================================================================================");

        TaxDeclaration declaration = loadInitialDeclaration();

        // Якщо передано прапорець --demo або відсутній інтерактивний термінал
        if (args != null && args.length > 0 && "--demo".equalsIgnoreCase(args[0])) {
            runFullDemo(declaration);
            return;
        }

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n--- ГОЛОВНЕ МЕНЮ ---");
            System.out.println("1. Загальний огляд (доходи, множина податків та підсумкова сума)");
            System.out.println("2. Сортування податкових виплат за сумою (за зростанням)");
            System.out.println("3. Сортування податкових виплат за сумою (за спаданням)");
            System.out.println("4. Пошук податків у заданому діапазоні сум");
            System.out.println("0. Вихід");
            System.out.print("Оберіть пункт меню: ");

            if (!scanner.hasNextLine()) {
                break;
            }

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    displayOverview(declaration);
                    break;
                case "2":
                    displaySortedTaxes(declaration, true);
                    break;
                case "3":
                    displaySortedTaxes(declaration, false);
                    break;
                case "4":
                    handleSearchByRange(declaration, scanner);
                    break;
                case "0":
                    System.out.println("Завершення роботи програми. До побачення!");
                    running = false;
                    break;
                default:
                    System.out.println("Невірний вибір. Спробуйте ще раз.");
            }
        }
    }

    /**
     * Завантажує дані з XML або створює стандартну декларацію.
     */
    public static TaxDeclaration loadInitialDeclaration() {
        XMLConfigLoader loader = new XMLConfigLoader();
        try (InputStream is = Main.class.getResourceAsStream("/taxes.xml")) {
            if (is != null) {
                return loader.loadFromStream(is);
            }
        } catch (Exception e) {
            System.out.println("Попередження: не вдалося завантажити taxes.xml, створюємо тестові дані програмно.");
        }

        // Резервна ініціалізація, якщо XML не знайдено
        TaxDeclaration declaration = new TaxDeclaration("Коваленко Іван Олександрович");
        declaration.addIncome(new EmploymentIncome("Основна робота (IT Компанія)", 600000.0, true, 2));
        declaration.addIncome(new EmploymentIncome("Сумісництво (Викладач)", 120000.0, false, 0));
        declaration.addIncome(new RoyaltyIncome("Видання наукової монографії", 85000.0, 15000.0));
        declaration.addIncome(new PropertySaleIncome("Продаж квартири", 1500000.0, true));
        declaration.addIncome(new PropertySaleIncome("Продаж авто", 300000.0, false));
        declaration.addIncome(new GiftIncome("Грошовий подарунок від батьків", 200000.0, true));
        declaration.addIncome(new GiftIncome("Подарунок від партнера", 50000.0, false));
        declaration.addIncome(new ForeignTransferIncome("Переказ від замовника зі США", 5000.0, "USD", 41.50));
        declaration.addIncome(new FinancialAidIncome("Матеріальна допомога від фонду", 10000.0, 4240.0));
        return declaration;
    }

    /**
     * Повний демонстраційний вивід усіх результатів.
     */
    public static void runFullDemo(TaxDeclaration declaration) {
        displayOverview(declaration);
        displaySortedTaxes(declaration, true);
        displaySortedTaxes(declaration, false);
        System.out.println("\n[ДЕМО] Пошук податків у діапазоні від 5 000 до 50 000 грн:");
        List<TaxPayment> matched = declaration.findTaxesByAmountRange(5000.0, 50000.0);
        for (TaxPayment p : matched) {
            System.out.println("  * " + p);
        }
    }

    private static void displayOverview(TaxDeclaration declaration) {
        System.out.println("\n================================================================================");
        System.out.println("Платник податків: " + declaration.getTaxpayerName());
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("1. ПЕРЕЛІК ДЖЕРЕЛ ДОХОДІВ:");
        for (Income inc : declaration.getIncomes()) {
            System.out.printf("  - %-32s : %10.2f грн | %s%n",
                    inc.getSourceName(), inc.getAmount(), inc.getTaxDescription());
        }

        System.out.println("\n2. МНОЖИНА НАРАХОВАНИХ ПОДАТКОВИХ ВИПЛАТ (Set<TaxPayment>):");
        Set<TaxPayment> payments = declaration.getTaxPayments();
        for (TaxPayment p : payments) {
            System.out.println("  • " + p);
        }

        System.out.println("--------------------------------------------------------------------------------");
        System.out.printf("ЗАГАЛЬНА СУМА ПОДАТКОВИХ ВИПЛАТ ЗА РІК: %.2f грн%n", declaration.calculateTotalTax());
        System.out.println("================================================================================");
    }

    private static void displaySortedTaxes(TaxDeclaration declaration, boolean ascending) {
        System.out.println("\n================================================================================");
        System.out.printf("РЕЗУЛЬТАТ СОРТУВАННЯ ПОДАТКІВ ЗА СУМОЮ (%s):%n",
                ascending ? "ЗА ЗРОСТАННЯМ" : "ЗА СПАДАННЯМ");
        System.out.println("--------------------------------------------------------------------------------");
        List<TaxPayment> sorted = ascending ? declaration.getSortedTaxes() : declaration.getSortedTaxesDescending();
        int index = 1;
        for (TaxPayment p : sorted) {
            System.out.printf("%2d. %-35s -> %10.2f грн (%s)%n",
                    index++, p.getSourceName(), p.getTaxAmount(), p.getTaxDescription());
        }
        System.out.println("================================================================================");
    }

    private static void handleSearchByRange(TaxDeclaration declaration, Scanner scanner) {
        try {
            System.out.print("Введіть мінімальну суму податку: ");
            double min = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("Введіть максимальну суму податку: ");
            double max = Double.parseDouble(scanner.nextLine().trim());

            List<TaxPayment> found = declaration.findTaxesByAmountRange(min, max);
            System.out.printf("\nЗнайдено податків у діапазоні від %.2f до %.2f грн: %d%n", min, max, found.size());
            for (TaxPayment p : found) {
                System.out.println("  • " + p);
            }
        } catch (NumberFormatException e) {
            System.out.println("Помилка: введено некоректне числове значення.");
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка: " + e.getMessage());
        }
    }
}
