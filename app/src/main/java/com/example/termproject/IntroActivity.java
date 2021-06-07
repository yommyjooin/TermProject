package com.example.termproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

public class IntroActivity extends AppCompatActivity {

    public TextView textView_test;
    public TextView textView_Date;
    public DatePickerDialog.OnDateSetListener callbackMethod;

    public static TextView deptextView, arrtextView, traintextView;
    public static int DorA = 0;
    //public static Context c_context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        arrtextView = findViewById(R.id.arrbutton);
        deptextView = findViewById(R.id.depbutton);
        traintextView = findViewById(R.id.train);
        //c_context = this;

        this.InitializeView();
        this.InitializeListener();

    }

    public void InitializeView()
    {
        textView_Date = (TextView)findViewById(R.id.date_button);
    }

    public void InitializeListener()
    {
        callbackMethod = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                textView_Date.setText(year + "년" + (monthOfYear+1 ) + "월" + dayOfMonth + "일");
                ViewActivity.Year = year; ViewActivity.Month = monthOfYear+1; ViewActivity.Day = dayOfMonth;
            }
        };
    }

    public void OnClickHandler(View view)
    {
        DatePickerDialog dialog = new DatePickerDialog(this, callbackMethod, 2021, 4, 24);
        dialog.show();
    }

    public void SelectdepStation(View view) {
        DorA = 0;
        Intent intent = new Intent(IntroActivity.this, MainActivity.class);
        startActivity(intent);
    }
    public void SelectarrStation(View view) {
        DorA = 1;
        Intent intent = new Intent(IntroActivity.this, MainActivity.class);
        startActivity(intent);
    }
    public void SelectTrain(View view){
        DorA = 2;
        Intent intent = new Intent(IntroActivity.this, Select_TrainActivity.class);
        startActivity(intent);
    }

    public void mOnClick(View view) {
        Intent intent = new Intent(IntroActivity.this, ViewActivity.class);
        startActivity(intent);
    }
}