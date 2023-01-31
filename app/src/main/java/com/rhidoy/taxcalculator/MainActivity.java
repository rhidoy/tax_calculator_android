package com.rhidoy.taxcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements IncomeData.TaxCalculateListener {
    private Spinner payerType;
    private Spinner zone;
    private IncomeData incomeData;

    private TextView tax;
    private TextView taxToPay;
    private TextView salaryTotal;
    private TextView basicTotal;
    private TextView houseRentTotal;
    private TextView medicalTotal;
    private TextView conveyanceTotal;
    private TextView totalAmount;
    private TextView eligibleInvestmentAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        incomeData = new IncomeData(this);

        initTextBox();
        initSpinner();
        initEditText();
        incomeData.calculateTax();
    }

    private void initSpinner() {
        payerType = findViewById(R.id.payer_category_sp);
        zone = findViewById(R.id.zone_sp);

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
        basicTotal = findViewById(R.id.basic_total);
        houseRentTotal = findViewById(R.id.house_total);
        medicalTotal = findViewById(R.id.medical_total);
        conveyanceTotal = findViewById(R.id.conveyance_total);
        totalAmount = findViewById(R.id.total_total);
        eligibleInvestmentAmount = findViewById(R.id.eligible_amount);
    }

    private void initEditText() {
        //salary total
        EditText salaryMonth = findViewById(R.id.salary_month);
        salaryMonth.setText((incomeData.getSalary()) + "");
        salaryMonth.addTextChangedListener(new TextWatcher(0, incomeData, salaryMonth));

        //basic
        EditText basicMonth = findViewById(R.id.basic_value);
        basicMonth.setText(incomeData.getBasicPercent() + "");
        basicMonth.addTextChangedListener(new TextWatcher(1, incomeData, basicMonth));

        //house
        EditText houseRent = findViewById(R.id.house_value);
        houseRent.setText(incomeData.getHouseRentPercent() + "");
        houseRent.addTextChangedListener(new TextWatcher(2, incomeData, houseRent));

        //medical
        EditText medical = findViewById(R.id.medical_value);
        medical.setText(incomeData.getMedicalPercent() + "");
        medical.addTextChangedListener(new TextWatcher(3, incomeData, medical));

        //conveyance
        EditText conveyance = findViewById(R.id.conveyance_value);
        conveyance.setText(incomeData.getConveyancePercent() + "");
        conveyance.addTextChangedListener(new TextWatcher(4, incomeData, conveyance));

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
        tax.setText("Your payable tax " + incomeData.getPayableTax());
        taxToPay.setText("You have to pay monthly " + (int) incomeData.getHaveToPayTax() / 12);
        salaryTotal.setText(incomeData.getSalaryTotal() + "");
        basicTotal.setText(incomeData.getBasicTotal() + "");
        houseRentTotal.setText(incomeData.getHouseRentTotal() + "");
        medicalTotal.setText(incomeData.getMedicalTotal() + "");
        conveyanceTotal.setText(incomeData.getConveyanceTotal() + "");
        totalAmount.setText(incomeData.getTotalAmount() + "");
        eligibleInvestmentAmount.setText(incomeData.getEligibleInvestment() + "");
    }

    @Override
    public void onCalculate(IncomeData data) {
        if (incomeData.isPercenMatch())
            setValue();
        else Toast.makeText(this, "Please fill percentage with 100", Toast.LENGTH_SHORT).show();
    }
}