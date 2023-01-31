package com.rhidoy.taxcalculator;

import android.text.Editable;
import android.widget.EditText;
import android.widget.Toast;

public class TextWatcher implements android.text.TextWatcher {

    private final int type;
    private final IncomeData data;
    private final EditText editText;

    public TextWatcher(int type, IncomeData data, EditText editText) {
        this.type = type;
        this.data = data;
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable.toString().isEmpty())
            return;

        int value = Integer.parseInt(editable.toString());
        int available;
        switch (type) {
            case 0://salary
                if (value == data.getSalary())
                    return;
                if (value < 16000) {
                    Toast.makeText(editText.getContext(), "Min salary must be above 16,000", Toast.LENGTH_SHORT).show();
                    return;
                }
                data.setSalary(value);
                break;
            case 1://basic
                if (value == data.getBasicPercent())
                    return;
                available = data.getHouseRentPercent()
                        + data.getMedicalPercent()
                        + data.getConveyancePercent();
                data.setBasic(getEligibleValue(value, available));
                break;
            case 2://house rent
                if (value == data.getHouseRentPercent())
                    return;
                available = data.getBasicPercent()
                        + data.getMedicalPercent()
                        + data.getConveyancePercent();
                data.setHouseRent(getEligibleValue(value, available));
                break;
            case 3://medical
                if (value == data.getMedicalPercent())
                    return;
                available = data.getBasicPercent()
                        + data.getHouseRentPercent()
                        + data.getConveyancePercent();
                data.setMedical(getEligibleValue(value, available));
                break;
            case 4://conveyance
                if (value == data.getConveyancePercent())
                    return;
                available = data.getBasicPercent()
                        + data.getHouseRentPercent()
                        + data.getMedicalPercent();
                data.setConveyance(getEligibleValue(value, available));
                break;
            case 5://incentive
                if (value == data.getIncentive())
                    return;
                data.setIncentive(value);
                break;
            case 6://bonus
                if (value == data.getBonus())
                    return;
                data.setBonus(value);
                break;
            case 7://invested amount
                if (value == data.getInvestedAmount())
                    return;
                data.setInvestedAmount(value);
                break;
        }
    }

    private int getEligibleValue(int value, int available) {
        //calculate the possible value with current one
        if (available + value > 100) {
            value = 100 - available;
            editText.setText(value + "");
        }
        return value;
    }
}
