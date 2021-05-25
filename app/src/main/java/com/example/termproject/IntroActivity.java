package com.example.termproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

public class IntroActivity extends AppCompatActivity {

    public TextView textView_test;
    public TextView textView_Date;
    public DatePickerDialog.OnDateSetListener callbackMethod;
    //public static Context c_context;

    // 시간표 화면으로 넘기는 변수들입니다
    int Year;
    int Month;
    int Day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        //c_context = this;

        this.InitializeView();
        this.InitializeListener();
    }

    public void InitializeView()
    {
        textView_Date = (TextView)findViewById(R.id.date_button);
        textView_test = (TextView)findViewById(R.id.test);

    }

    public void InitializeListener()
    {
        callbackMethod = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                textView_Date.setText(year + "년" + (monthOfYear+1 ) + "월" + dayOfMonth + "일");
                Year = year; Month = monthOfYear+1; Day = dayOfMonth;
                textView_test.setText("Year : "+Year+"   Month : "+Month+"   Day : "+Day);
            }
        };
    }

    public void OnClickHandler(View view)
    {
        DatePickerDialog dialog = new DatePickerDialog(this, callbackMethod, 2021, 4, 24);
        dialog.show();
    }

}