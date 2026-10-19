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

public class Main {

    public static void main(String[] args) {
        try {
            System.setOut(new java.io.PrintStream(System.out, true, StandardCharsets.UTF_8));
        } catch (Exception ignored) {
        }

        System.out.println("          ANNUAL PERSONAL INCOME TAX CALCULATOR SYSTEM (LAB 1)");

        TaxDeclaration declaration = loadInitialDeclaration();

        if (args != null && args.length > 0 && "--demo".equalsIgnoreCase(args[0])) {
            runFullDemo(declaration);
            return;
        }

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\nMAIN MENU");
            System.out.println("1. Overview (incomes, tax payments set and total tax)");
            System.out.println("2. Sort tax payments by amount (ascending)");
            System.out.println("3. Sort tax payments by amount (descending)");
            System.out.println("4. Find tax payments in amount range");
            System.out.println("0. Exit");
            System.out.print("Select an option: ");

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
                    System.out.println("Exiting application. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public static TaxDeclaration loadInitialDeclaration() {
        XMLConfigLoader loader = new XMLConfigLoader();
        try (InputStream is = Main.class.getResourceAsStream("/taxes.xml")) {
            if (is != null) {
                return loader.loadFromStream(is);
            }
        } catch (Exception e) {
            System.out.println("Warning: Could not load taxes.xml from resources, initializing fallback data.");
        }

        TaxDeclaration declaration = new TaxDeclaration("John Doe");
        declaration.addIncome(new EmploymentIncome("Tech Systems Corp (Main Job)", 600000.0, true, 2));
        declaration.addIncome(new EmploymentIncome("IT Academy (Secondary Job)", 120000.0, false, 0));
        declaration.addIncome(new RoyaltyIncome("Publishing House (Java Book)", 85000.0, 15000.0));
        declaration.addIncome(new PropertySaleIncome("Apartment Sale", 1500000.0, true));
        declaration.addIncome(new PropertySaleIncome("Car Sale", 300000.0, false));
        declaration.addIncome(new GiftIncome("Cash Gift from Parents", 200000.0, true));
        declaration.addIncome(new GiftIncome("Gift from Business Partner", 50000.0, false));
        declaration.addIncome(new ForeignTransferIncome("Upwork Software Contract", 5000.0, "USD", 41.50));
        declaration.addIncome(new FinancialAidIncome("Trade Union Financial Aid", 10000.0, 4240.0));
        return declaration;
    }

    public static void runFullDemo(TaxDeclaration declaration) {
        displayOverview(declaration);
        displaySortedTaxes(declaration, true);
        displaySortedTaxes(declaration, false);
        System.out.println("\n[DEMO] Finding taxes in range [5000.00, 50000.00]:");
        List<TaxPayment> matched = declaration.findTaxesByAmountRange(5000.0, 50000.0);
        for (TaxPayment p : matched) {
            System.out.println("  * " + p);
        }
    }

    private static void displayOverview(TaxDeclaration declaration) {
        System.out.println("Taxpayer: " + declaration.getTaxpayerName());
        System.out.println("1. REGISTERED INCOMES:");
        for (Income inc : declaration.getIncomes()) {
            System.out.printf("  - %-35s : %10.2f | %s%n",
                    inc.getSourceName(), inc.getAmount(), inc.getTaxDescription());
        }

        System.out.println("\n2. SET OF TAX PAYMENTS (Set<TaxPayment>):");
        Set<TaxPayment> payments = declaration.getTaxPayments();
        for (TaxPayment p : payments) {
            System.out.println("  * " + p);
        }

        System.out.printf("TOTAL ANNUAL TAX OBLIGATION: %.2f%n", declaration.calculateTotalTax());
    }

    private static void displaySortedTaxes(TaxDeclaration declaration, boolean ascending) {
        System.out.printf("TAX PAYMENTS SORTED BY AMOUNT (%s):%n",
                ascending ? "ASCENDING" : "DESCENDING");
        List<TaxPayment> sorted = ascending ? declaration.getSortedTaxes() : declaration.getSortedTaxesDescending();
        int index = 1;
        for (TaxPayment p : sorted) {
            System.out.printf("%2d. %-35s -> %10.2f (%s)%n",
                    index++, p.getSourceName(), p.getTaxAmount(), p.getTaxDescription());
        }
    }

    private static void handleSearchByRange(TaxDeclaration declaration, Scanner scanner) {
        try {
            System.out.print("Enter minimum tax amount: ");
            double min = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("Enter maximum tax amount: ");
            double max = Double.parseDouble(scanner.nextLine().trim());

            List<TaxPayment> found = declaration.findTaxesByAmountRange(min, max);
            System.out.printf("\nFound %d tax payment(s) in range [%.2f, %.2f]:%n", found.size(), min, max);
            for (TaxPayment p : found) {
                System.out.println("  * " + p);
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid numeric input.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
