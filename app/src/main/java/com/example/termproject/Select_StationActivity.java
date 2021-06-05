package com.example.termproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class Select_StationActivity extends AppCompatActivity {

    ListView listView;
    int citycode;

    Button btnBack;

    public static String stationName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_station);

        btnBack = (Button)findViewById(R.id.btnBack);

        Intent intent = getIntent();
        String city = intent.getStringExtra("city");
        citycode = Integer.parseInt(city);
        Toast.makeText(getApplicationContext(),"도시 코드는 "+citycode+"입니다", Toast.LENGTH_LONG).show();
        listView = (ListView)findViewById(R.id.listView);

        ArrayList<station> list = xmlParser();
        String[] data = new String[list.size()];
        for(int i=0;i<list.size();i++) {
            data[i] =  list.get(i).getName();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,data);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object vo = (Object)parent.getAdapter().getItem(position);
                station find = null;
                for(station s : list){
                    if(s.getName().equals(vo)){
                        find = s;
                        break;
                    }
                }
                Toast.makeText(getApplicationContext(),"역 코드는 "+find.getId()+"입니다", Toast.LENGTH_LONG).show();

                if(IntroActivity.DorA==0) {
                    IntroActivity.deptextView.setText(find.getName());
                    ViewActivity.dep = find.getId();
                }
                else{
                    IntroActivity.arrtextView.setText(find.getName());
                    ViewActivity.arr = find.getId();
                }


                finish();
            }
        });
    }

    private ArrayList<station> xmlParser()  {
        ArrayList<station> arrayList = new ArrayList<station>();
        InputStream is = null;
        //select_city(is, citycode);
        switch (citycode){
            case 11:
                is = getResources().openRawResource(R.raw.station_11);
                break;
            case 12:
                is = getResources().openRawResource(R.raw.station_12);
                break;
            case 21:
                is = getResources().openRawResource(R.raw.station_21);
                break;
            case 22:
                is = getResources().openRawResource(R.raw.station_22);
                break;
            case 23:
                is = getResources().openRawResource(R.raw.station_23);
                break;
            case 24:
                is = getResources().openRawResource(R.raw.station_24);
                break;
            case 25:
                is = getResources().openRawResource(R.raw.station_25);
                break;
            case 26:
                is = getResources().openRawResource(R.raw.station_26);
                break;
            case 31:
                is = getResources().openRawResource(R.raw.station_31);
                break;
            case 32:
                is = getResources().openRawResource(R.raw.station_32);
                break;
            case 33:
                is = getResources().openRawResource(R.raw.station_33);
                break;
            case 34:
                is = getResources().openRawResource(R.raw.station_34);
                break;
            case 35:
                is = getResources().openRawResource(R.raw.station_35);
                break;
            case 36:
                is = getResources().openRawResource(R.raw.station_36);
                break;
            case 37:
                is = getResources().openRawResource(R.raw.station_37);
                break;
            case 38:
                is = getResources().openRawResource(R.raw.station_38    );
                break;
        }
        //String queryUrl = "http://openapi.tago.go.kr/openapi/service/TrainInfoService/getCtyAcctoTrainSttnList?serviceKey=RD0BzjHbAJnBN1brAQq0R%2FJORqOCJU%2B56cy1%2F7blI1JiUoJFi%2FfEEbyFuYApB6DckZ19xn59cF52Sx1g9DsyHg%3D%3D&numOfRows=10&pageNo=1&cityCode=12";

        //--- xmlPullParser ---//
        try {
            //URL url= new URL(queryUrl);
            //InputStream is = url.openStream();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new InputStreamReader(is, "UTF-8"));

            station st = null;

            parser.next();
            int eventType = parser.getEventType();

            while(eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        String startTag = parser.getName();
                        if(startTag.equals("item")) {
                            st = new station();
                        }
                        if(startTag.equals("nodeid")) {
                            st.setId(parser.nextText());
                        }
                        if(startTag.equals("nodename")) {
                            st.setName(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        String endTag = parser.getName();
                        if(endTag.equals("item")) {
                            arrayList.add(st);
                        }
                        break;
                }
                eventType = parser.next();
            }

        }catch(XmlPullParserException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arrayList;
    }
}