package com.arthur.redcup.View;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import com.arthur.redcup.R;

import java.util.Calendar;

public class DatePickerFragment  extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private Calendar calendar;
    private int year, month, day;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                }
            };



    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        TextView tv = (TextView) getActivity().findViewById(R.id.text_view_event_date);

        //Display the user changed time on TextView
        tv.setText(String.valueOf(dayOfMonth) + "/" +  String.valueOf(month) + "/" + String.valueOf(year));


    }
}
