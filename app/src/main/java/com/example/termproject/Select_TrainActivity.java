package com.example.termproject;

import android.app.Activity;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import java.util.List;

public class Select_TrainActivity extends AppCompatActivity{

    ListView listView;
    train findtrain;

    Button Back;

    @Override

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_train);

        Back = (Button)findViewById(R.id.Back);

        Back.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View view){
               Intent intent = new Intent(getApplicationContext(),IntroActivity.class);
               startActivity(intent);
           }
        });

        listView = (ListView)findViewById(R.id.listView);

        ArrayList<train> list = xmlParser();
        String[] data = new String[list.size()];

        for(int i=0; i<list.size(); i++){
            data[i] = list.get(i).getName();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,data);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object vo = (Object)parent.getAdapter().getItem(position);
                train find=null;
                for(train t : list){
                    if(t.getName().equals(vo)){
                        find = t;
                        break;
                    }
                }

                if(IntroActivity.DorA==2){
                    IntroActivity.traintextView.setText(find.getName());
                    ViewActivity.train=find.getId();
                }

                finish();
            }
        });
    }

    private ArrayList<train> xmlParser(){
        ArrayList<train> arrayList = new ArrayList<train>();
        InputStream is = null;

        if(IntroActivity.TorB==0){
            is = getResources().openRawResource(R.raw.trains);
        }
        else{
            is = getResources().openRawResource(R.raw.bus);
        }

        //http://openapi.tago.go.kr/openapi/service/TrainInfoService/getVhcleKndList?serviceKey=RD0BzjHbAJnBN1brAQq0R%2FJORqOCJU%2B56cy1%2F7blI1JiUoJFi%2FfEEbyFuYApB6DckZ19xn59cF52Sx1g9DsyHg%3D%3D&numOfRows=10&pageNo=1&cityCode=12";

        try{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new InputStreamReader(is, "UTF-8"));

            train t = null;

            parser.next();
            int eventType = parser.getEventType();

            while(eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        String startTag = parser.getName();
                        if (startTag.equals("item")) {
                            t = new train();
                        }

                        if(IntroActivity.TorB==0) {
                            if (startTag.equals("vehiclekndid")) {
                                t.setId(parser.nextText());
                            }
                            if (startTag.equals("vehiclekndnm")) {
                                t.setName(parser.nextText());
                            }
                        }
                        else{
                            if (startTag.equals("gradeId")) {
                                t.setId(parser.nextText());
                            }
                            if (startTag.equals("gradeNm")) {
                                t.setName(parser.nextText());
                            }
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        String endTag = parser.getName();
                        if (endTag.equals("item")) {
                            arrayList.add(t);
                        }
                        break;
                }
                eventType = parser.next();
            }
        }
        catch(XmlPullParserException e){
            e.printStackTrace();
        }
        catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return arrayList;

    }
}
