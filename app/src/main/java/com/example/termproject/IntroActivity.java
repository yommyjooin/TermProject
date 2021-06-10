package com.example.termproject;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

    TextView trainText, busText;
    Button trainBtn, busBtn;
    LinearLayout trainll, busll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        arrtextView = findViewById(R.id.arrText);
        deptextView = findViewById(R.id.depText);
        traintextView = findViewById(R.id.trainText);
        bustextView = findViewById(R.id.busText);
        //c_context = this;

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
                textView_Date.setText(year + "년" + (monthOfYear+1 ) + "월" + dayOfMonth + "일");
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
        //버튼 배경색 바꿈 or 텍스트 크기 변경으로 알려줌

        trainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //trainText.setPaintFlags(trainText.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
                trainText.setTypeface(null, Typeface.BOLD);
                //busText.setPaintFlags(0);
                busText.setTypeface(null, Typeface.NORMAL);
                trainll.setBackgroundColor(Color.rgb(242,242,242));
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
        //버튼 배경색 바꿈 or 텍스트 크기 변경으로 알려줌
        busBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //busText.setPaintFlags(busText.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
                busText.setTypeface(null, Typeface.BOLD);
                //trainText.setPaintFlags(0);
                busll.setBackgroundColor(Color.rgb(242,242,242));
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