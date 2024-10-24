package com.rhidoy.taxcalculator;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements IncomeData.TaxCalculateListener {
    private IncomeData incomeData;

    private TextView tax;
    private TextView taxToPay;
    private TextView salaryTotal;
    private TextView totalAmount;
    private TextView eligibleInvestmentAmount;
    private CheckBox monthlySalary;
    private EditText salaryMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        incomeData = new IncomeData(this);

        initTextBox();
        initSpinner();
        initEditText();

        monthlySalary = findViewById(R.id.cb_salary_monthly);
        monthlySalary.setChecked(incomeData.isMonthlySalary());
        monthlySalary.setOnClickListener(view -> incomeData.setMonthlySalary(monthlySalary.isChecked()));

        incomeData.calculateTax();
    }

    private void initSpinner() {
        Spinner payerType = findViewById(R.id.payer_category_sp);
        Spinner zone = findViewById(R.id.zone_sp);

        ArrayAdapter<CharSequence> payerTypeAd = ArrayAdapter
                .createFromResource(
                        this,
                        R.array.payer_type,
                        android.R.layout.simple_spinner_item);
        payerTypeAd.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        payerType.setAdapter(payerTypeAd);
        payerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                incomeData.setPayerType(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<CharSequence> zoneAd = ArrayAdapter
                .createFromResource(
                        this,
                        R.array.zone,
                        android.R.layout.simple_spinner_item);
        zoneAd.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        zone.setAdapter(zoneAd);
        zone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                incomeData.setZone(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initTextBox() {
        tax = findViewById(R.id.tax);
        taxToPay = findViewById(R.id.tax_pay);
        salaryTotal = findViewById(R.id.salary_yearly);
        totalAmount = findViewById(R.id.total_total);
        eligibleInvestmentAmount = findViewById(R.id.eligible_amount);
    }

    private void initEditText() {
        //salary total
        salaryMonth = findViewById(R.id.salary_month);
        salaryMonth.setText((incomeData.getSalary()) + "");
        salaryMonth.addTextChangedListener(new TextWatcher(0, incomeData, salaryMonth));

        //incentive
        EditText incentive = findViewById(R.id.incentive_total);
        incentive.setText(incomeData.getIncentive() + "");
        incentive.addTextChangedListener(new TextWatcher(5, incomeData, incentive));

        //bonus
        EditText bonus = findViewById(R.id.bonus_total);
        bonus.setText(incomeData.getBonus() + "");
        bonus.addTextChangedListener(new TextWatcher(6, incomeData, bonus));

        //investedAmount
        EditText investedAmount = findViewById(R.id.invested_amount);
        investedAmount.setText(incomeData.getInvestedAmount() + "");
        investedAmount.addTextChangedListener(new TextWatcher(7, incomeData, investedAmount));

    }

    private void setValue() {
        //update salary edit box for check box change
        int previousPosition = salaryMonth.getSelectionStart();
        salaryMonth.setText((incomeData.getSalary()) + "");
        salaryMonth.setSelection(previousPosition);
        tax.setText(String.format("Your payable tax %.2f ", incomeData.getPayableTax()));
        taxToPay.setText("You have to pay monthly " + (int) incomeData.getHaveToPayTax() / 12);
        salaryTotal.setText(incomeData.getSalaryTotal() + "");
        totalAmount.setText(incomeData.getTotalAmount() + "");
        eligibleInvestmentAmount.setText(incomeData.getEligibleInvestment() + "");
        TextView taxAbleIncome = findViewById(R.id.taxable_total);
        taxAbleIncome.setText(incomeData.getTotalTaxableIncome() + "");
        TextView taxCalculation = findViewById(R.id.tax_calculation);
        taxCalculation.setText("Tax calculation:" + incomeData.getTaxCalculation());
    }

    @Override
    public void onCalculate(IncomeData data) {
        setValue();
    }
}