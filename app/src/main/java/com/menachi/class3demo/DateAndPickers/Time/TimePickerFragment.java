package com.menachi.class3demo.DateAndPickers.Time;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{
    interface onTimeSetListener{
        public void OnTimeSet(int hour, int minute);
    }

    private onTimeSetListener listener;

    int hour;
    int minute;

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        listener.OnTimeSet(hourOfDay,minute);
    }

    public void setOnTimeSetListener(onTimeSetListener ls){
        listener = ls;
    }

    public void setTime(int hour, int minute){
        this.hour=hour;
        this.minute=minute;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new TimePickerDialog(getActivity(), this, hour, minute, true);
        return dialog;
    }

}


