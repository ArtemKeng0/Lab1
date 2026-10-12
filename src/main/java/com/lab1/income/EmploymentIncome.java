package com.lab1.income;

public class EmploymentIncome extends Income {

    public static final double TOTAL_TAX_RATE = 0.195;
    public static final double BENEFIT_PER_CHILD = 1500.0;

    private final boolean isMainJob;
    private final int childrenCount;

    public EmploymentIncome(String sourceName, double amount, boolean isMainJob, int childrenCount) {
        super(sourceName, amount);
        if (childrenCount < 0) {
            throw new IllegalArgumentException("Children count cannot be negative: " + childrenCount);
        }
        this.isMainJob = isMainJob;
        this.childrenCount = childrenCount;
    }

    public boolean isMainJob() {
        return isMainJob;
    }

    public int getChildrenCount() {
        return childrenCount;
    }

    @Override
    public double calculateTax() {
        double taxableBase = getAmount();
        if (isMainJob && childrenCount > 0) {
            double totalBenefit = childrenCount * BENEFIT_PER_CHILD;
            taxableBase = Math.max(0.0, taxableBase - totalBenefit);
        }
        return Math.round(taxableBase * TOTAL_TAX_RATE * 100.0) / 100.0;
    }

    @Override
    public String getTaxDescription() {
        if (isMainJob) {
            return String.format("Main employment (19.5%% rate, %d child benefit(s))", childrenCount);
        }
        return "Secondary employment (19.5% rate, no benefit)";
    }
}
