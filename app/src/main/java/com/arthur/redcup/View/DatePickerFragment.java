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

//    @SuppressWarnings("deprecation")
//    public void setDate(View view) {
//        //showDialog(999);
//
//    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        //DATE
        //dateView = (TextView) findViewById(R.id.textView3);
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
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day

                }
            };



    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        TextView tv = (TextView) getActivity().findViewById(R.id.text_view_event_date);
        //Set a message for user

        //Display the user changed time on TextView
        tv.setText(String.valueOf(dayOfMonth) + "/" +  String.valueOf(month) + "/" + String.valueOf(year));


    }
}
