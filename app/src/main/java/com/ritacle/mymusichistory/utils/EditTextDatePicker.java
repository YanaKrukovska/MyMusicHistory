package com.ritacle.mymusichistory.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;
import java.util.TimeZone;

import static com.ritacle.mymusichistory.utils.DataUtils.convertToString;
import static com.ritacle.mymusichistory.utils.DataUtils.createDate;

public class EditTextDatePicker implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    private EditText editText;
    private int day;
    private int month;
    private int year;
    private View view;
    public boolean dateChosen = false;
    private Context context;

    public EditTextDatePicker(View view, Context context, int editTextID) {
        this.context = context;
        this.view = view;
        this.editText = view.findViewById(editTextID);
        this.editText.setOnClickListener(this);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        month = monthOfYear;
        day = dayOfMonth;
        dateChosen = true;
        updateDisplay();
    }

    @Override
    public void onClick(View v) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        DatePickerDialog dialog = new DatePickerDialog(context, this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();

    }

    public void updateDisplay() {


        returnDate();
        editText.setText(new StringBuilder()
                .append(day).append("/").append(month + 1).append("/").append(year).append(" "));

    }


    public String returnDate() {
        System.out.println(convertToString(createDate(year, month+1, day)));
        return convertToString(createDate(year, month + 1, day));
    }


    public boolean isDateChosen() {
        return dateChosen;
    }
}