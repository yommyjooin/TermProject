package com.example.termproject;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class IntroActivity extends AppCompatActivity {

    public TextView textView_test;
    public TextView textView_Date;
    public DatePickerDialog.OnDateSetListener callbackMethod;

    public static TextView deptextView, arrtextView, traintextView, bustextView;
    public static int DorA = 0, TorB = 0;
    //public static Context c_context;

    TextView trainText, busText, selectTV;
    Button trainBtn, busBtn;
    LinearLayout trainll, busll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        arrtextView = findViewById(R.id.arrText);
        deptextView = findViewById(R.id.depText);
        traintextView = findViewById(R.id.trainText);
        bustextView = findViewById(R.id.busText);
        //c_context = this;
        selectTV = findViewById(R.id.selectTV);

        trainBtn = (Button)findViewById(R.id.trainBtn);
        busBtn = (Button)findViewById(R.id.busBtn);

        trainText = findViewById(R.id.trainText);
        busText = findViewById(R.id.busText);

        trainll = findViewById(R.id.trainll);
        busll = findViewById(R.id.busll);

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
                textView_Date.setText(year + "???" + (monthOfYear+1 ) + "???" + dayOfMonth + "???");
                ViewActivity.Year = year; ViewActivity.Month = monthOfYear+1; ViewActivity.Day = dayOfMonth;
            }
        };
    }

    public void OnClickHandler(View view)
    {
        DatePickerDialog dialog = new DatePickerDialog(this, callbackMethod, 2021, 5, 11);
        dialog.show();
    }

    public void SelectdepStation(View view) {
        DorA = 0;
        //deptextView.setPaintFlags(trainText.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
        deptextView.setTypeface(null, Typeface.BOLD);
        Intent intent = new Intent(IntroActivity.this, MainActivity.class);
        startActivity(intent);
    }
    public void SelectarrStation(View view) {
        DorA = 1;
        //arrtextView.setPaintFlags(trainText.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
        arrtextView.setTypeface(null, Typeface.BOLD);
        Intent intent = new Intent(IntroActivity.this, MainActivity.class);
        startActivity(intent);
    }
    /*
    public void SelectTrain(View view){
        DorA = 2;
        Intent intent = new Intent(IntroActivity.this, Select_TrainActivity.class);
        startActivity(intent);
    }

     */

    public void mOnClick(View view) {
        Intent intent = new Intent(IntroActivity.this, ViewActivity.class);
        startActivity(intent);
    }

    public void setTrain(View view) {
        //?????? ????????? ?????? or ????????? ?????? ???????????? ?????????

        trainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Select_TrainActivity.selectTV.setText("?????? ?????? ??????");
                //selectTV.setText("??????");
                //trainText.setPaintFlags(trainText.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
                trainText.setTypeface(null, Typeface.BOLD);
                //busText.setPaintFlags(0);
                busText.setTypeface(null, Typeface.NORMAL);
                trainll.setBackgroundColor(Color.rgb(232,232,232));
                busll.setBackgroundColor(Color.WHITE);
                //busBtn.setTextSize(10);
                DorA=2;
                TorB=0;
                Intent intent = new Intent(IntroActivity.this, Select_TrainActivity.class);
                startActivity(intent);
            }
        });

    }

    public void setBus(View view) {
        //?????? ????????? ?????? or ????????? ?????? ???????????? ?????????
        busBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //selectTV.setText("?????? ?????? ??????");
                //busText.setPaintFlags(busText.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
                busText.setTypeface(null, Typeface.BOLD);
                //trainText.setPaintFlags(0);
                busll.setBackgroundColor(Color.rgb(232,232,232));
                trainll.setBackgroundColor(Color.WHITE);
                trainText.setTypeface(null, Typeface.NORMAL);
                DorA=3;
                TorB=1;

                Intent intent = new Intent(IntroActivity.this, Select_TrainActivity.class);
                startActivity(intent);
            }
        });

    }
}