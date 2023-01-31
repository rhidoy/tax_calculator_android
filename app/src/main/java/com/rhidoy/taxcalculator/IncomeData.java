package com.rhidoy.taxcalculator;

public class IncomeData {

    private final int maxHouseRentYearlyLimit = 25000 * 12;
    private final int maxMedicalAllowanceYearlyLimit = 120000; //1 lakh 20K
    private final int maxMedicalAllowanceYearlyLimitDisabled = 1000000; //10 lakh
    private final int maxYearlyConveyanceLimit = 30000;
    private final int maxYearlyInvestmentLimit = 10000000; //1 koti

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
        return salary / 12;
    }

    public int getSalaryTotal() {
        return salary;
    }

    public void setSalary(int salary) {
        if (getSalary() != salary) {
            this.salary = salary * 12;
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

    public boolean isPercenMatch() {
        return basicPercent + houseRentPercent + medicalPercent + conveyancePercent == 100;
    }

    private void resetValue() {
        salary = 55000 * 12;
        incentive = 0;
        bonus = 0;
        investedAmount = 0;
        updateValue();
    }

    private void updateValue() {
        basic = (int) (salary * ((double) getBasicPercent() / 100));
        houseRent = (int) (salary * ((double) getHouseRentPercent() / 100));
        medical = (int) (salary * ((double) getMedicalPercent() / 100));
        conveyance = (int) (salary * ((double) getConveyancePercent() / 100));
        salary = basic + houseRent + medical + conveyance;
        setTotalAmount(salary + incentive + bonus);
    }

    public void calculateTax() {

        if (isPercenMatch()) {
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
            int totalPayableIncome = getBasicTotal() + payableRent
                    + payableMedical + payableConveyance + bonus;

            eligibleInvestment = (int) (totalPayableIncome * .25);

            int taxFreeMaxIncome = 300000; //general
            if (payerType == 1)
                taxFreeMaxIncome = 350000; //Female/Senior Citizen
            else if (payerType == 2)
                taxFreeMaxIncome = 450000; //Disabled
            else if (payerType == 3)
                taxFreeMaxIncome = 450000; //Gazetted Freedom Fighters

            if (totalPayableIncome > taxFreeMaxIncome)
                payableTax = calculateTax(totalPayableIncome, taxFreeMaxIncome, 0);

            //now calculate investment tax
            int taxFreeInvestment = Math.min(getInvestedAmount(), getEligibleInvestment());
            taxFreeInvestment = Math.min(taxFreeInvestment, maxYearlyInvestmentLimit);

            double taxReducePercent = .15;
            if (totalPayableIncome > 1500000) {
                taxReducePercent = .1; //10%
            }
            payableTax = payableTax - (taxFreeInvestment * taxReducePercent);


            if (payableTax < 5000) {
                if (getZone() == 0)
                    setHaveToPayTax(5000);
                else if (getZone() == 1)
                    setHaveToPayTax(4000);
                else setHaveToPayTax(3000);
            } else setHaveToPayTax(payableTax);
        }

        listener.onCalculate(this);
    }

    private double calculateTax(int income, int limit, int slab) {
        if (income > limit)
            income = income - limit;

        double taxPercent;

        switch (slab) {
            case 4:
                return income * 0.25;
            case 3:
                taxPercent = 0.20;
                limit = 500000;
                break;
            case 2:
                taxPercent = 0.15;
                limit = 400000;
                break;
            case 1:
                taxPercent = 0.10;
                limit = 300000;
                break;
            default:
                taxPercent = 0.05;
                limit = 100000;

        }

        if (income > limit) {
            return limit * taxPercent + calculateTax(income, limit, slab + 1);
        } else {
            return income * taxPercent;  //zero tax
        }


//        switch (slab) {
//            case 5:
//                taxPercent = 0.25;
//                return income * .25;  //rest money 25%
//            case 4:
//                taxPercent = 0.20;
//                newLimit = 0;
//                if (income > limit)
//                    return calculateTax(unCalculateIncome, 0, 5);
//                else return income * .20;  //next 5 lakh 20%
//            case 3:
//                taxPercent = 0.15;
//                newLimit = 500000;
//                if (income > limit)
//                    return calculateTax(unCalculateIncome, 500000, 4);
//                else return income * .15;  //next 4 lakh 15%
//            case 2:
//                taxPercent = 0.10;
//                newLimit = 400000;
//                if (income > limit)
//                    return calculateTax(unCalculateIncome, 400000, 3);
//                else return income * .10;  //next 3 lakh 10%
//            case 1:
//                newLimit = 300000;
//                if (income > limit)
//                    return calculateTax(unCalculateIncome, 300000, 2);
////                else return income * .05;  //next 1 lakh 5%
//            default:
//                if (income > limit)
//                    return calculateTax(unCalculateIncome, 100000, 1);
//                else return 0;  //zero tax
//        }
    }

    public interface TaxCalculateListener {
        void onCalculate(IncomeData data);
    }
}
