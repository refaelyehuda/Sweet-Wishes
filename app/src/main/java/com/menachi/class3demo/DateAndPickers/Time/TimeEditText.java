package com.menachi.class3demo.DateAndPickers.Time;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import java.util.Calendar;

public class TimeEditText extends EditText implements TimePickerFragment.onTimeSetListener {
    int hour;
    int minute;

    public TimeEditText(Context context) {
        super(context);
    }

    public TimeEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TimeEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setInputType(0);
        Calendar cal = Calendar.getInstance();
        hour = cal.get(Calendar.HOUR_OF_DAY);
        minute = cal.get(Calendar.MINUTE);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            TimePickerFragment tpf = new TimePickerFragment();
            tpf.setOnTimeSetListener(this);
            tpf.setTime(hour, minute);
            tpf.show(((Activity)getContext()).getFragmentManager(), "TAG");
        }
        return true;
    }


    @Override
    public void OnTimeSet(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
        setText(String.format("%02d:%02d", this.hour, this.minute));
    }
}
