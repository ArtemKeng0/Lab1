# Laboratory Work 1: Annual Individual Tax Calculator

Console Java application developed according to Object-Oriented Programming (OOP) principles and Java Code Conventions.

---

## Task Description

**Topic:** Taxes.  
**Goal:** Determine the set and total amount of annual tax payments for an individual, taking into account:
- Incomes from main and secondary employment;
- Royalty fees;
- Property sales (movable and immovable);
- Cash and property gifts;
- Foreign money transfers;
- Child tax benefits;
- Financial aid (with a non-taxable threshold).  
**Required feature:** Sort tax payments by amount.

---

## 🏛️ OOP Architecture

1. **Encapsulation:**
   - All fields are `private final`.
   - Access via getters.
   - Input validation in constructors.

2. **Inheritance & Hierarchy:**
   - Abstract class `Income` serves as the base for all income streams.
   - Subclasses model specific tax rules:
     - `EmploymentIncome`: main vs secondary job, child benefit reduction.
     - `RoyaltyIncome`: reduction of taxable base by documented author expenses.
     - `PropertySaleIncome`: first sale exemption (0%), subsequent sales at 6.5%.
     - `GiftIncome`: immediate family exemption (0%), other gifts at 6.5%.
     - `ForeignTransferIncome`: currency conversion by exchange rate and 19.5% tax.
     - `FinancialAidIncome`: non-taxable threshold, 19.5% tax on excess only.

3. **Polymorphism:**
   - Polymorphic method `calculateTax()` eliminates `instanceof` and `switch` statements.

4. **Set & Sorting:**
   - `TaxPayment` represents a single tax obligation and implements `Comparable<TaxPayment>`.
   - `TaxDeclaration.getTaxPayments()` returns a `Set<TaxPayment>` guaranteeing uniqueness.
   - `getSortedTaxes()` and `getSortedTaxesDescending()` provide sorting by amount.
   - `findTaxesByAmountRange(min, max)` filters payments in a given range.

5. **Initialization Parameters (XML):**
   - `XMLConfigLoader` loads and saves declarations using standard Java DOM API (`javax.xml`).

---

## Project Layout

```
lab1
├── pom.xml                                     # Maven configuration
├── README.md                                   # Documentation
├── .gitignore                                  # Git ignore rules
├── mvnw / mvnw.cmd                             # Maven wrapper scripts
└── src
    ├── main
    │   ├── java/com/lab1
    │   │   ├── app
    │   │   │   └── Main.java                   # Entry point and minimal console menu
    │   │   ├── config
    │   │   │   └── XMLConfigLoader.java        # XML configuration loader
    │   │   ├── tax
    │   │   │   ├── TaxDeclaration.java         # Aggregator container
    │   │   │   └── TaxPayment.java             # Comparable tax payment
    │   │   └── income
    │   │       ├── Income.java                 # Base abstract class
    │   │       ├── EmploymentIncome.java       # Employment income & child benefits
    │   │       ├── RoyaltyIncome.java          # Royalty & documented expenses
    │   │       ├── PropertySaleIncome.java     # Property sales
    │   │       ├── GiftIncome.java             # Gifts
    │   │       ├── ForeignTransferIncome.java  # Foreign transfers
    │   │       └── FinancialAidIncome.java     # Financial aid
    │   └── resources
    │       └── taxes.xml                       # Default XML parameters
    └── test
        ├── java/com/lab1
        │   ├── config
        │   │   └── XMLConfigLoaderTest.java    # XML loader tests
        │   ├── tax
        │   │   ├── TaxDeclarationTest.java     # Aggregator tests with Mockito
        │   │   └── TaxPaymentTest.java         # TaxPayment & sorting tests
        │   └── income
        │       ├── IncomeTest.java
        │       ├── EmploymentIncomeTest.java
        │       ├── RoyaltyIncomeTest.java
        │       ├── PropertySaleIncomeTest.java
        │       ├── GiftIncomeTest.java
        │       ├── ForeignTransferIncomeTest.java
        │       └── FinancialAidIncomeTest.java
        └── resources
            └── mockito-extensions
                └── org.mockito.plugins.MockMaker
```

---

## Testing (JUnit 5 + Mockito)

Run test suite:
```bash
./mvnw test
```
or on Windows:
```powershell
.\mvnw.cmd test
```

---

## Build & Run

### 1. Build JAR:
```bash
.\mvnw.cmd clean package
```

### 2. Interactive Console Menu:
```bash
java -jar target/lab1-tax-calculator-1.0.0-jar-with-dependencies.jar
```

### 3. Demo Mode:
```bash
java -jar target/lab1-tax-calculator-1.0.0-jar-with-dependencies.jar --demo
```
