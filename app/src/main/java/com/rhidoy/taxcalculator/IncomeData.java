package com.rhidoy.taxcalculator;

public class IncomeData {

    private static final int maxSalaryIncomeExceptionLimit = 450000; //4lakh 50hajar
    private static final double eligibleInvestmentPercent = .15;

    private static final double maxIncomeExemptionPercent = 0.03;
    private static final double maxInvestmentExemptionPercent = 0.20;
    private static final int maxIncomeExemptionLimit = 1000000; //10lakh

    private int payerType = 0;
    private int zone = 0;

    private int totalAmount;
    private int eligibleInvestment;

    private int salary;

    private int incentive;
    private int bonus;
    private int investedAmount;

    private double haveToPayTax;
    private double payableTax;
    private final TaxCalculateListener listener;
    private boolean monthlySalary = true;

    private int totalTaxableIncome = 0;

    private StringBuilder taxCalculation = new StringBuilder();

    public IncomeData(TaxCalculateListener listener) {
        this.listener = listener;
        resetValue();
    }

    public double getPayableTax() {
        return payableTax;
    }


    public void setPayerType(int payerType) {
        if (this.payerType != payerType) {
            this.payerType = payerType;
            calculateTax();
        }
    }

    public int getZone() {
        return zone;
    }

    public void setZone(int zone) {
        if (this.zone != zone) {
            this.zone = zone;
            calculateTax();
        }
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getSalary() {
        if (monthlySalary)
            return salary / 12;

        return salary;
    }

    public int getSalaryTotal() {
        return salary;
    }

    public void setSalary(int salary) {
        if (getSalary() != salary) {
            if (monthlySalary)
                salary = salary * 12;
            this.salary = salary;
            calculateTax();
        }
    }

    public int getIncentive() {
        return incentive;
    }

    public void setIncentive(int incentive) {
        if (this.incentive != incentive) {
            this.incentive = incentive;
            calculateTax();
        }
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        if (this.bonus != bonus) {
            this.bonus = bonus;
            calculateTax();
        }
    }

    public int getEligibleInvestment() {
        return eligibleInvestment;
    }

    public int getInvestedAmount() {
        return investedAmount;
    }

    public void setInvestedAmount(int investedAmount) {
        if (this.investedAmount != investedAmount) {
            this.investedAmount = investedAmount;
            calculateTax();
        }
    }

    public double getHaveToPayTax() {
        return haveToPayTax;
    }

    public void setHaveToPayTax(double haveToPayTax) {
        this.haveToPayTax = haveToPayTax;
    }

    private void resetValue() {
        salary = 100000;
        if (monthlySalary)
            salary = salary * 12;
        incentive = 0;
        bonus = 0;
        investedAmount = 0;
        totalTaxableIncome = 0;
        updateValue();
    }

    private void updateValue() {
        taxCalculation = new StringBuilder();
        setTotalAmount(salary + incentive + bonus);
    }

    private double totalTax;
    private int taxFreeMaxIncome;

    public void calculateTax() {
        payableTax = 0;
        updateValue();

        //calculate taxable amount
        totalTaxableIncome = totalAmount - Math.min(totalAmount / 3, maxSalaryIncomeExceptionLimit);

        taxFreeMaxIncome = 350000; //general
        if (payerType == 1)
            taxFreeMaxIncome = taxFreeMaxIncome + 50000; //Female/Senior Citizen
        else if (payerType == 2)
            taxFreeMaxIncome = taxFreeMaxIncome + 150000; //Disabled
        else if (payerType == 3)
            taxFreeMaxIncome = taxFreeMaxIncome + 150000; //Gazetted Freedom Fighters

        totalTax = 0;
        if (totalTaxableIncome > taxFreeMaxIncome) {
            calculateTax(totalTaxableIncome, 0);
        }

        //now calculate investment tax
        eligibleInvestment = (int) (totalTaxableIncome * eligibleInvestmentPercent);

        payableTax = totalTax - getTaxExemption();

        if (totalTax > 0 && payableTax < 5000) {
            if (getZone() == 0)
                setHaveToPayTax(5000);
            else if (getZone() == 1)
                setHaveToPayTax(4000);
            else setHaveToPayTax(3000);
        } else setHaveToPayTax(payableTax);

        listener.onCalculate(this);
    }

    private double getTaxExemption() {
        double incomeExemption = totalTaxableIncome * maxIncomeExemptionPercent;
        double investmentExemption = getInvestedAmount() * maxInvestmentExemptionPercent;

        double rebate = Math.min(Math.min(incomeExemption, investmentExemption), maxIncomeExemptionLimit);

        taxCalculation.append("\n\nYour Total Tax:\t\t")
                .append(totalTax);

        taxCalculation.append("\n\nExemption Rebate Calculation:")
                .append("\nFrom Income:\t\t")
                .append(incomeExemption)
                .append(" at ")
                .append(maxIncomeExemptionPercent)
                .append("%")
                .append("\nFrom Investment:\t\t")
                .append(investmentExemption)
                .append(" at ")
                .append(maxInvestmentExemptionPercent)
                .append("%")
                .append("\nWhere max limit is:\t\t")
                .append(maxIncomeExemptionLimit)
                .append("\nYour Eligible Rebate is (which low):\t\t")
                .append(rebate);
        return rebate;
    }

    private void calculateTax(int income, int slab) {
        double taxPercent;
        int limit;
        switch (slab) {
            case 5:
                taxPercent = 0.25;
                limit = income;
                break;
            case 4:
                taxPercent = 0.20;
                limit = 500000;
                break;
            case 3:
                taxPercent = 0.15;
                limit = 500000;
                break;
            case 2:
                taxPercent = 0.10;
                limit = 400000;
                break;
            case 1:
                taxPercent = 0.05;
                limit = 100000;
                break;
            default:
                taxPercent = 0;
                limit = taxFreeMaxIncome;
        }

        if (income > limit) {
            setTaxCalculation(limit, taxPercent);
            calculateTax(income - limit, slab + 1);
        } else {
            setTaxCalculation(income, taxPercent);
        }
    }

    public boolean isMonthlySalary() {
        return monthlySalary;
    }

    public void setMonthlySalary(boolean monthlySalary) {
        this.monthlySalary = monthlySalary;
        calculateTax();
    }

    public int getTotalTaxableIncome() {
        return totalTaxableIncome;
    }

    public String getTaxCalculation() {
        return taxCalculation.toString();
    }

    private void setTaxCalculation(int amount, double taxPercent) {
        taxCalculation.append("\nAmount:\t\t").append(amount)
                .append("\t\t Percent: \t").append(String.format("%.2f ", taxPercent))
                .append("\t\t Tax: \t").append(String.format("%.2f ", amount * taxPercent));

        totalTax += amount * taxPercent;
    }

    public interface TaxCalculateListener {
        void onCalculate(IncomeData data);
    }
}
