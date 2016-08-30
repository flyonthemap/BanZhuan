package com.shuao.banzhuan.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.shuao.banzhuan.activity.PersonalInfoActivity;

import java.util.Calendar;

/**
 * Created by flyonthemap on 16/8/11.
 */
public  class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用当前的日期作为默认的生日
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DATE);
        // 创建一个DatePicker对话框
        return new DatePickerDialog(getActivity(),this,year,month,day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
         // 调用PersonalActivity中的方法来修改生日
        ((PersonalInfoActivity) getActivity()).changeBirthday(year,monthOfYear+1,dayOfMonth);
    }
}