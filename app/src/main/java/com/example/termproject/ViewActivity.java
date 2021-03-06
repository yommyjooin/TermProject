package com.example.termproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ViewActivity extends AppCompatActivity {

    TextView text,arrTextView,depTextView;
    ListView listView;
    ImageButton btnBack;

    String TrainKey= "RD0BzjHbAJnBN1brAQq0R%2FJORqOCJU%2B56cy1%2F7blI1JiUoJFi%2FfEEbyFuYApB6DckZ19xn59cF52Sx1g9DsyHg%3D%3D";
    String BusKey = "K%2FhtglJ%2BZ0HWSHrD7sSotR0wXupBxdrOjcW8XN31U3HKGwA4f5E0ziTlQzUux9vN0htlydvoGMpHZcw17NX%2Btw%3D%3D";
    String data;

    ListViewAdapter adapter;

    private TableLayout tableLayout;

    public static String dep, arr, train;
    public static int Year, Month, Day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        text= (TextView)findViewById(R.id.text);
        arrTextView = (TextView)findViewById(R.id.arrText);
        depTextView = (TextView)findViewById(R.id.depText);
        btnBack = (ImageButton)findViewById(R.id.btnBack);
        listView = (ListView)findViewById(R.id.listView03);

        arrTextView.setText(IntroActivity.arrtextView.getText());
        depTextView.setText(IntroActivity.deptextView.getText());

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
                startActivity(intent);
            }
        });

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                ArrayList<Info> list = xmlParser();;//?????? ???????????? ???????????? XML data??? ???????????? String ????????? ????????????


                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        adapter = new ListViewAdapter();
                        listView.setAdapter(adapter);
                        for(int i=0;i<list.size();i++) {
                            adapter.addItem(list.get(i).getTrain(), list.get(i).getdepTime(), list.get(i).getarrTime(), list.get(i).getCharge());
                        }
                    }
                });

            }
        }).start();
    }

    /*//Button??? ???????????? ??? ???????????? ???????????? callback method
    public void mOnClick(View v){

        switch( v.getId() ){
            case R.id.button:

                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        data= getXmlData();//?????? ???????????? ???????????? XML data??? ???????????? String ????????? ????????????


                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                text.setText(data); //TextView??? ?????????  data ??????
                            }
                        });

                    }
                }).start();

                break;
        }

    }*/

    String setTime(int num){
        String String_num = Integer.toString(num);
        if(num<10){
            String_num = "0"+String_num;
        }
        return String_num;
    }

    private ArrayList<Info> xmlParser()  {
        ArrayList<Info> arrayList = new ArrayList<Info>();

        String departure = URLEncoder.encode(dep);
        String location = URLEncoder.encode(arr);//????????? ?????? ????????? ???????????? utf-8 ???????????? encoding     //?????? ?????? ?????? ??????
        String queryUrl = "";
        String trains = URLEncoder.encode(train);

        //?????? ?????? ????????????
        if(IntroActivity.TorB==0){

            queryUrl="http://openapi.tago.go.kr/openapi/service/TrainInfoService/getStrtpntAlocFndTrainInfo?serviceKey="//?????? URL
                    + TrainKey +"&numOfRows=100&pageNo=1&arrPlaceId="+location+"&depPlaceId="+departure+"&depPlandTime="+Year+setTime(Month)+setTime(Day)+"&trainGradeCode="+trains;
        }
        // ?????? ?????? ????????????
        else{
            queryUrl="http://openapi.tago.go.kr/openapi/service/ExpBusInfoService/getStrtpntAlocFndExpbusInfo?serviceKey="//?????? URL
                    + BusKey +"&numOfRows=100&pageNo=1&depTerminalId="+location+"&arrTerminalId="+departure+"&depPlandTime="+Year+setTime(Month)+setTime(Day)+"&busGradeId="+trains;
        }
        //--- xmlPullParser ---//
        try {
            URL url= new URL(queryUrl);
            InputStream is = url.openStream();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new InputStreamReader(is, "UTF-8"));

            Info c = null;

            parser.next();
            int eventType = parser.getEventType();

            while(eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        String startTag = parser.getName();
                        if(startTag.equals("item")) {
                            c = new Info();
                        }

                        // ?????? ????????? ??????
                        if(IntroActivity.TorB==0){
                            if(startTag.equals("adultcharge")) {
                                String charge = parser.nextText();
                                if(Integer.parseInt(charge)==0) c.setCharge("??????");
                                else c.setCharge(charge);
                            }
                            if(startTag.equals("arrplandtime")) {
                                String arrplandtime = parser.nextText();
                                String arrHour = arrplandtime.substring(8,10);
                                String arrMin = arrplandtime.substring(10,12);
                                String arrTime = arrHour+":"+arrMin;
                                c.setarrTime(arrTime);
                            }
                            if(startTag.equals("depplandtime")) {
                                String depplandtime = parser.nextText();
                                String depHour = depplandtime.substring(8,10);
                                String depMin = depplandtime.substring(10,12);
                                String depTime = depHour+":"+depMin;
                                c.setdepTime(depTime);
                            }
                            if(startTag.equals("traingradename")) {
                                c.setTrain(parser.nextText());
                            }
                            break;
                        }

                        // ?????? ????????? ??????
                        else{
                            if(startTag.equals("arrPlandTime")) {
                                String arrplandtime = parser.nextText();
                                String arrHour = arrplandtime.substring(8,10);
                                String arrMin = arrplandtime.substring(10,12);
                                String arrTime = arrHour+":"+arrMin;
                                c.setarrTime(arrTime);
                            }
                            if(startTag.equals("charge")) {
                                c.setCharge(parser.nextText());
                            }
                            if(startTag.equals("depPlandTime")) {
                                String depplandtime = parser.nextText();
                                String depHour = depplandtime.substring(8,10);
                                String depMin = depplandtime.substring(10,12);
                                String depTime = depHour+":"+depMin;
                                c.setdepTime(depTime);
                            }
                            if(startTag.equals("gradeNm")) {
                                c.setTrain(parser.nextText());
                            }
                            break;
                        }

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



    // ?????? x //
    String getXmlData(){

        StringBuffer buffer=new StringBuffer();

        String departure = URLEncoder.encode(dep);
        String location = URLEncoder.encode(arr);//????????? ?????? ????????? ???????????? utf-8 ???????????? encoding     //?????? ?????? ?????? ??????
        String trains = URLEncoder.encode(train);

        String queryUrl="http://openapi.tago.go.kr/openapi/service/TrainInfoService/getStrtpntAlocFndTrainInfo?serviceKey="//?????? URL
                + TrainKey +"&numOfRows=100&pageNo=1&arrPlaceId="+location+"&depPlaceId="+departure+"&depPlandTime="+Year+setTime(Month)+setTime(Day)+"&trainGradeCode="+trains;

        try {
            URL url= new URL(queryUrl);//???????????? ??? ?????? url??? URL ????????? ??????.
            InputStream is= url.openStream(); //url????????? ??????????????? ??????

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream ???????????? xml ????????????

            String tag;

            xpp.next();
            int eventType= xpp.getEventType();

            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("?????? ??????...\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//?????? ?????? ????????????
                        /*
                        if(tag.equals("item")) ;// ????????? ????????????
                        else if(tag.equals("adultcharge")){
                            buffer.append("?????? : ");
                            xpp.next();
                            buffer.append(xpp.getText());//addr ????????? TEXT ???????????? ?????????????????? ??????
                            buffer.append("\n"); //????????? ?????? ??????
                        }
                        else if(tag.equals("arrplacename")){
                            buffer.append("????????? : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        else if(tag.equals("arrplandtime")){
                            buffer.append("?????? ?????? :");
                            xpp.next();
                            String arrplandtime = xpp.getText();
                            String arrYear = arrplandtime.substring(0,4);
                            buffer.append(arrYear+"???");
                            for(int i = 4; i < arrplandtime.length(); i=i+2){
                                String temp = arrplandtime.substring(i, i+2);
                                String str = " ";
                                if(i == 4) str = "???";
                                else if(i==6) str = "???";
                                else if(i==8) str = "???";
                                else if(i==10) str = "???";
                                else if(i==12) str = "???";
                                buffer.append(temp+str);
                            }
                            buffer.append("\n");
                        }
                        else if(tag.equals("depplacename")){
                            buffer.append("????????? :");
                            xpp.next();
                            buffer.append(xpp.getText());//depplacename
                            buffer.append("\n");
                        }

                        else if(tag.equals("depplandtime")){
                            buffer.append("?????? ?????? :");
                            xpp.next();
                            buffer.append(xpp.getText());//depplandtime
                            buffer.append("\n");
                        }
                        else if(tag.equals("depplandtime")) {
                            buffer.append("?????? ?????? :");
                            xpp.next();
                            String depplandtime = xpp.getText();
                            String depYear = depplandtime.substring(0, 4);
                            buffer.append(depYear + "???");
                            for (int i = 4; i < depplandtime.length(); i = i + 2) {
                                String temp = depplandtime.substring(i, i + 2);
                                String str = " ";
                                if (i == 4) str = "???";
                                else if (i == 6) str = "???";
                                else if (i == 8) str = "???";
                                else if (i == 10) str = "???";
                                else if (i == 12) str = "???";
                                buffer.append(temp + str);
                            }
                            buffer.append("\n");
                        }
                        else if(tag.equals("traingradename")){
                            buffer.append("?????? :");
                            xpp.next();
                            buffer.append(xpp.getText());//traingradename
                            buffer.append("\n");
                        }
                        else if(tag.equals("trainno")){
                            buffer.append("?????? ?????? :");
                            xpp.next();
                            buffer.append(xpp.getText());//csId
                            buffer.append("\n");
                        }

                        break;
                        */

                        Map<String, String> info = new HashMap<String, String>();

                        if(tag.equals("item"));
                        else if(tag.equals("adultcharge")) {
                            xpp.next();
                            info.put("??????",xpp.getText());
                        }
                        else if(tag.equals("arrplacename")) {
                            xpp.next();
                            info.put("?????????", xpp.getText());
                        }
                        else if(tag.equals("arrplandtime")) {
                            xpp.next();
                            //info.put("????????????", xpp.getText());
                            String arrplandtime = xpp.getText();
                            String arrYear = arrplandtime.substring(0,4);
                            String arrMonth = arrplandtime.substring(4,6);
                            String arrDay = arrplandtime.substring(6,8);
                            String arrDate = arrYear+"/"+arrMonth+"/"+arrDay;

                            String arrHour = arrplandtime.substring(8,10);
                            String arrMin = arrplandtime.substring(10,12);
                            String arrSec = arrplandtime.substring(12,14);
                            String arrTime = arrHour+":"+arrMin+":"+arrSec;
                            info.put("????????????", arrDate);
                            info.put("arrHour", arrHour);
                            info.put("????????????", arrTime);

                        }
                        else if(tag.equals("depplacename")) {
                            xpp.next();
                            info.put("?????????", xpp.getText());
                        }
                        else if(tag.equals("depplandtime")) {
                            xpp.next();
                            //info.put("????????????", xpp.getText());
                            String depplandtime = xpp.getText();
                            String depYear = depplandtime.substring(0,4);
                            String depMonth = depplandtime.substring(4,6);
                            String depDay = depplandtime.substring(6,8);
                            String depDate = depYear+"/"+depMonth+"/"+depDay;

                            String depHour = depplandtime.substring(8,10);
                            String depMin = depplandtime.substring(10,12);
                            String depSec = depplandtime.substring(12,14);
                            String depTime = depHour+":"+depMin+":"+depSec;
                            info.put("????????????", depDate);
                            //int numdepHour = Integer.parseInt(depHour);
                            info.put("dephour", depHour);
                            info.put("????????????", depTime);
                        }
                        else if(tag.equals("traingradename")){
                            xpp.next();
                            info.put("??????", xpp.getText());
                        }
                        else if(tag.equals("trainno")){
                            xpp.next();
                            info.put("????????????", xpp.getText());
                        }




                        Set<String> keys = info.keySet();
                        for(String key:keys){
                            buffer.append(key+":"+info.get(key));
                            buffer.append("\n");
                        }



                        /*
                        String temp = new String(info.get("depHour"));
                        String temphour = new String("06");

                        buffer.append("n?????????"+info.get("?????????"));
                        buffer.append("n????????????"+info.get("????????????"));
                        buffer.append("n????????????"+info.get("????????????"));
                        buffer.append("n?????????"+info.get("?????????"));
                        buffer.append("n????????????"+info.get("????????????"));
                        buffer.append("n????????????"+info.get("????????????"));
                        buffer.append("n??????"+info.get("??????")+" ????????????:"+info.get("????????????"));
                        buffer.append("n??????"+info.get("??????"));


                        break;*/


                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); //?????? ?????? ????????????

                        if(tag.equals("item")) buffer.append("\n");// ????????? ??????????????????..?????????

                        break;
                }

                eventType= xpp.next();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch blocke.printStackTrace();
        }

        buffer.append("?????? ???\n");

        return buffer.toString();//StringBuffer ????????? ?????? ??????

    }
}