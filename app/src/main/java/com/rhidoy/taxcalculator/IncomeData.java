package com.rhidoy.taxcalculator;

public class IncomeData {

    private final int maxHouseRentYearlyLimit = 25000 * 12;
    private final int maxMedicalAllowanceYearlyLimit = 120000; //1 lakh 20K
    private final int maxMedicalAllowanceYearlyLimitDisabled = 1000000; //10 lakh
    private final int maxYearlyConveyanceLimit = 30000;
    private final int maxYearlyInvestmentLimit = 10000000; //1 koti

    private final double eligibleInvestmentPercent = .20;

    private final double generalTaxReducePercent = .15; //15%
//    private final int taxReduceMinIncome = 1500000; //15lakh
//    private final double minTaxReducePercent = .1; //10%

    private int payerType = 0;
    private int zone = 0;

    private int totalAmount;
    private int eligibleInvestment;

    private int salary;

    private int basic;
    private int basicPercent = 55;
    private int houseRent;
    private int houseRentPercent = 30;
    private int medical;
    private int medicalPercent = 10;
    private int conveyance;
    private int conveyancePercent = 5;
    private int incentive;
    private int bonus;
    private int investedAmount;

    private double haveToPayTax;
    private double payableTax;
    private final TaxCalculateListener listener;
    private boolean monthlySalary = true;

    private int totalPayableIncome = 0;

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

    public int getBasic() {
        return basic / 12;
    }

    public int getBasicPercent() {
//        return (int) (((double) getBasic() / getSalary()) * 100);
        return basicPercent;
    }

    public int getBasicTotal() {
        return basic;
    }

    public void setBasic(int basicPercent) {
        if (this.basicPercent != basic) {
            this.basicPercent = basicPercent;
            calculateTax();
        }
    }

    public int getHouseRent() {
        return houseRent / 12;
    }

    public int getHouseRentPercent() {
//        return (int) (((double) getHouseRent() / getSalary()) * 100);
        return houseRentPercent;
    }

    public int getHouseRentTotal() {
        return houseRent;
    }

    public void setHouseRent(int houseRent) {
        if (this.houseRentPercent != houseRent) {
            this.houseRentPercent = houseRent;
            calculateTax();
        }
    }

    public int getMedical() {
        return medical / 12;
    }

    public int getMedicalPercent() {
//        return (int) (((double) getMedical() / getSalary()) * 100);
        return medicalPercent;
    }

    public int getMedicalTotal() {
        return medical;
    }

    public void setMedical(int medical) {
        if (this.medicalPercent != medical) {
            this.medicalPercent = medical;
            calculateTax();
        }
    }

    public int getConveyance() {
        return conveyance / 12;
    }

    public int getConveyancePercent() {
//        return (int) (((double) getConveyance() / getSalary()) * 100);
        return conveyancePercent;
    }

    public int getConveyanceTotal() {
        return conveyance;
    }

    public void setConveyance(int conveyance) {
        if (this.conveyancePercent != conveyance) {
            this.conveyancePercent = conveyance;
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

    public boolean isPercentMatch() {
        return basicPercent + houseRentPercent + medicalPercent + conveyancePercent == 100;
    }

    private void resetValue() {
        salary = 55000;
        if (monthlySalary)
            salary = salary * 12;
        incentive = 0;
        bonus = 0;
        investedAmount = 0;
        totalPayableIncome = 0;
        updateValue();
    }

    private void updateValue() {
        taxCalculation = new StringBuilder();
        basic = (int) (salary * ((double) getBasicPercent() / 100));
        houseRent = (int) (salary * ((double) getHouseRentPercent() / 100));
        medical = (int) (salary * ((double) getMedicalPercent() / 100));
        conveyance = (int) (salary * ((double) getConveyancePercent() / 100));
        salary = basic + houseRent + medical + conveyance;
        setTotalAmount(salary + incentive + bonus);
    }

    private double totalTax;
    private int taxFreeMaxIncome;

    public void calculateTax() {

        if (isPercentMatch()) {
            payableTax = 0;
            updateValue();
            //first find tax free house rent
            //the equation is basic*.5 (50%) or monthly 25,000 which less
            int taxFreeHouseRent = (int) (getBasicTotal() * .5);
            if (taxFreeHouseRent > maxHouseRentYearlyLimit)
                taxFreeHouseRent = maxHouseRentYearlyLimit;

            //then calculate tax free medical allowance
            //the equation is basic*.1 (10%) or yearly 120,000
            //for disabled it is 1,000,000
            //which is less
            int taxFreeMedicalAllowance = (int) (getBasicTotal() * .1);
            if (taxFreeMedicalAllowance > maxMedicalAllowanceYearlyLimit) {
                taxFreeMedicalAllowance = payerType == 2 //when disabled
                        ? maxMedicalAllowanceYearlyLimitDisabled : maxMedicalAllowanceYearlyLimit;
            }

            //house rent
            int payableRent = getHouseRentTotal() > taxFreeHouseRent
                    ? getHouseRentTotal() - taxFreeHouseRent : 0;
            int payableMedical = getMedicalTotal() > taxFreeMedicalAllowance
                    ? getMedicalTotal() - taxFreeMedicalAllowance : 0;

            //then find tax free conveyance
            //the equation is yearly 30,000
            int payableConveyance = getConveyanceTotal() > maxYearlyConveyanceLimit
                    ? getConveyanceTotal() - maxYearlyConveyanceLimit : 0;


            //calculate taxable amount
            totalPayableIncome = getBasicTotal() + payableRent
                    + payableMedical + payableConveyance + bonus + incentive;

            taxFreeMaxIncome = 300000; //general
            if (payerType == 1)
                taxFreeMaxIncome = 350000; //Female/Senior Citizen
            else if (payerType == 2)
                taxFreeMaxIncome = 450000; //Disabled
            else if (payerType == 3)
                taxFreeMaxIncome = 450000; //Gazetted Freedom Fighters

            totalTax = 0;
            if (totalPayableIncome > taxFreeMaxIncome) {
                calculateTax(totalPayableIncome, 0);
            }

            //now calculate investment tax
            eligibleInvestment = (int) (totalPayableIncome * eligibleInvestmentPercent);
            int taxFreeInvestment = Math.min(getInvestedAmount(), getEligibleInvestment());
            taxFreeInvestment = Math.min(taxFreeInvestment, maxYearlyInvestmentLimit);


            taxCalculation.append("\n\nYour Total Tax:\t\t")
                    .append(totalTax);

            taxCalculation.append("\nYour Investment Rebate:\t")
                    .append(taxFreeInvestment * generalTaxReducePercent)
                    .append(" \tPercent ")
                    .append(generalTaxReducePercent);

            payableTax = totalTax - (taxFreeInvestment * generalTaxReducePercent);


            if (totalTax > 0 && payableTax < 5000) {
                if (getZone() == 0)
                    setHaveToPayTax(5000);
                else if (getZone() == 1)
                    setHaveToPayTax(4000);
                else setHaveToPayTax(3000);
            } else setHaveToPayTax(payableTax);
        }

        listener.onCalculate(this);
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
                limit = 400000;
                break;
            case 2:
                taxPercent = 0.10;
                limit = 300000;
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

    public int getTotalPayableIncome() {
        return totalPayableIncome;
    }

    public String getTaxCalculation() {
        return taxCalculation.toString();
    }

    private void setTaxCalculation(int amount, double taxPercent) {
        taxCalculation.append("\nAmount:\t").append(amount)
                .append("\t\t Percent: \t").append(taxPercent)
                .append("\t\t Tax: \t").append(amount * taxPercent);

        totalTax += amount * taxPercent;
    }

    public interface TaxCalculateListener {
        void onCalculate(IncomeData data);
    }
}
