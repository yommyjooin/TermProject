package com.example.termproject;

import android.app.Activity;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    city findCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.listView);

        ArrayList<city> list = xmlParser();

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
                findCity = null;
                for(city s : list){
                    if(s.getName().equals(vo)){
                        findCity = s;
                        break;
                    }
                }
                //Toast.makeText(getApplicationContext(),"도시 코드는 "+findCity.getId()+"입니다", Toast.LENGTH_LONG).show();
                NextSelect();
                finish();
            }
        });
    }

    public void NextSelect(){
        Intent intent = new Intent(MainActivity.this, Select_StationActivity.class);
        intent.putExtra("city", findCity.getId());
        startActivity(intent);
    }

    private ArrayList<city> xmlParser()  {
        ArrayList<city> arrayList = new ArrayList<city>();
        InputStream is = getResources().openRawResource(R.raw.city_code);
        //String queryUrl = "http://openapi.tago.go.kr/openapi/service/TrainInfoService/getCtyAcctoTrainSttnList?serviceKey=RD0BzjHbAJnBN1brAQq0R%2FJORqOCJU%2B56cy1%2F7blI1JiUoJFi%2FfEEbyFuYApB6DckZ19xn59cF52Sx1g9DsyHg%3D%3D&numOfRows=10&pageNo=1&cityCode=12";

        //--- xmlPullParser ---//
        try {
            //URL url= new URL(queryUrl);
            //InputStream is = url.openStream();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new InputStreamReader(is, "UTF-8"));

            city c = null;

            parser.next();
            int eventType = parser.getEventType();

            while(eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        String startTag = parser.getName();
                        if(startTag.equals("item")) {
                            c = new city();
                        }
                        if(startTag.equals("citycode")) {
                            c.setId(parser.nextText());
                        }
                        if(startTag.equals("cityname")) {
                            c.setName(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        String endTag = parser.getName();
                        if(endTag.equals("item")) {
                            arrayList.add(c);
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
