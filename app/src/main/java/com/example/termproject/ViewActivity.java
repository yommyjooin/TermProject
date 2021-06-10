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
                ArrayList<Info> list = xmlParser();;//아래 메소드를 호출하여 XML data를 파싱해서 String 객체로 얻어오기


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

    /*//Button을 클릭했을 때 자동으로 호출되는 callback method
    public void mOnClick(View v){

        switch( v.getId() ){
            case R.id.button:

                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        data= getXmlData();//아래 메소드를 호출하여 XML data를 파싱해서 String 객체로 얻어오기


                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                text.setText(data); //TextView에 문자열  data 출력
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
        String location = URLEncoder.encode(arr);//한글의 경우 인식이 안되기에 utf-8 방식으로 encoding     //지역 검색 위한 변수
        String queryUrl = "";
        String trains = URLEncoder.encode(train);

        //기차 정보 받아오기
        if(IntroActivity.TorB==0){

            queryUrl="http://openapi.tago.go.kr/openapi/service/TrainInfoService/getStrtpntAlocFndTrainInfo?serviceKey="//요청 URL
                    + TrainKey +"&numOfRows=100&pageNo=1&arrPlaceId="+location+"&depPlaceId="+departure+"&depPlandTime="+Year+setTime(Month)+setTime(Day)+"&trainGradeCode="+trains;
        }
        // 버스 정보 받아오기
        else{
            queryUrl="http://openapi.tago.go.kr/openapi/service/ExpBusInfoService/getStrtpntAlocFndExpbusInfo?serviceKey="//요청 URL
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

                        // 기차 데이터 추출
                        if(IntroActivity.TorB==0){
                            if(startTag.equals("adultcharge")) {
                                String charge = parser.nextText();
                                if(Integer.parseInt(charge)==0) c.setCharge("매진");
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

                        // 버스 데이터 추출
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



    // 사용 x //
    String getXmlData(){

        StringBuffer buffer=new StringBuffer();

        String departure = URLEncoder.encode(dep);
        String location = URLEncoder.encode(arr);//한글의 경우 인식이 안되기에 utf-8 방식으로 encoding     //지역 검색 위한 변수
        String trains = URLEncoder.encode(train);

        String queryUrl="http://openapi.tago.go.kr/openapi/service/TrainInfoService/getStrtpntAlocFndTrainInfo?serviceKey="//요청 URL
                + TrainKey +"&numOfRows=100&pageNo=1&arrPlaceId="+location+"&depPlaceId="+departure+"&depPlandTime="+Year+setTime(Month)+setTime(Day)+"&trainGradeCode="+trains;

        try {
            URL url= new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is= url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기

            String tag;

            xpp.next();
            int eventType= xpp.getEventType();

            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("파싱 시작...\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//태그 이름 얻어오기
                        /*
                        if(tag.equals("item")) ;// 첫번째 검색결과
                        else if(tag.equals("adultcharge")){
                            buffer.append("요금 : ");
                            xpp.next();
                            buffer.append(xpp.getText());//addr 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        }
                        else if(tag.equals("arrplacename")){
                            buffer.append("도착역 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        else if(tag.equals("arrplandtime")){
                            buffer.append("도착 시간 :");
                            xpp.next();
                            String arrplandtime = xpp.getText();
                            String arrYear = arrplandtime.substring(0,4);
                            buffer.append(arrYear+"년");
                            for(int i = 4; i < arrplandtime.length(); i=i+2){
                                String temp = arrplandtime.substring(i, i+2);
                                String str = " ";
                                if(i == 4) str = "월";
                                else if(i==6) str = "일";
                                else if(i==8) str = "시";
                                else if(i==10) str = "분";
                                else if(i==12) str = "초";
                                buffer.append(temp+str);
                            }
                            buffer.append("\n");
                        }
                        else if(tag.equals("depplacename")){
                            buffer.append("출발역 :");
                            xpp.next();
                            buffer.append(xpp.getText());//depplacename
                            buffer.append("\n");
                        }

                        else if(tag.equals("depplandtime")){
                            buffer.append("출발 시간 :");
                            xpp.next();
                            buffer.append(xpp.getText());//depplandtime
                            buffer.append("\n");
                        }
                        else if(tag.equals("depplandtime")) {
                            buffer.append("출발 시간 :");
                            xpp.next();
                            String depplandtime = xpp.getText();
                            String depYear = depplandtime.substring(0, 4);
                            buffer.append(depYear + "년");
                            for (int i = 4; i < depplandtime.length(); i = i + 2) {
                                String temp = depplandtime.substring(i, i + 2);
                                String str = " ";
                                if (i == 4) str = "월";
                                else if (i == 6) str = "일";
                                else if (i == 8) str = "시";
                                else if (i == 10) str = "분";
                                else if (i == 12) str = "초";
                                buffer.append(temp + str);
                            }
                            buffer.append("\n");
                        }
                        else if(tag.equals("traingradename")){
                            buffer.append("열차 :");
                            xpp.next();
                            buffer.append(xpp.getText());//traingradename
                            buffer.append("\n");
                        }
                        else if(tag.equals("trainno")){
                            buffer.append("열차 번호 :");
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
                            info.put("요금",xpp.getText());
                        }
                        else if(tag.equals("arrplacename")) {
                            xpp.next();
                            info.put("도착역", xpp.getText());
                        }
                        else if(tag.equals("arrplandtime")) {
                            xpp.next();
                            //info.put("도착정보", xpp.getText());
                            String arrplandtime = xpp.getText();
                            String arrYear = arrplandtime.substring(0,4);
                            String arrMonth = arrplandtime.substring(4,6);
                            String arrDay = arrplandtime.substring(6,8);
                            String arrDate = arrYear+"/"+arrMonth+"/"+arrDay;

                            String arrHour = arrplandtime.substring(8,10);
                            String arrMin = arrplandtime.substring(10,12);
                            String arrSec = arrplandtime.substring(12,14);
                            String arrTime = arrHour+":"+arrMin+":"+arrSec;
                            info.put("도착날짜", arrDate);
                            info.put("arrHour", arrHour);
                            info.put("도착시간", arrTime);

                        }
                        else if(tag.equals("depplacename")) {
                            xpp.next();
                            info.put("출발역", xpp.getText());
                        }
                        else if(tag.equals("depplandtime")) {
                            xpp.next();
                            //info.put("출발시간", xpp.getText());
                            String depplandtime = xpp.getText();
                            String depYear = depplandtime.substring(0,4);
                            String depMonth = depplandtime.substring(4,6);
                            String depDay = depplandtime.substring(6,8);
                            String depDate = depYear+"/"+depMonth+"/"+depDay;

                            String depHour = depplandtime.substring(8,10);
                            String depMin = depplandtime.substring(10,12);
                            String depSec = depplandtime.substring(12,14);
                            String depTime = depHour+":"+depMin+":"+depSec;
                            info.put("출발날짜", depDate);
                            //int numdepHour = Integer.parseInt(depHour);
                            info.put("dephour", depHour);
                            info.put("출발시간", depTime);
                        }
                        else if(tag.equals("traingradename")){
                            xpp.next();
                            info.put("열차", xpp.getText());
                        }
                        else if(tag.equals("trainno")){
                            xpp.next();
                            info.put("열차번호", xpp.getText());
                        }




                        Set<String> keys = info.keySet();
                        for(String key:keys){
                            buffer.append(key+":"+info.get(key));
                            buffer.append("\n");
                        }



                        /*
                        String temp = new String(info.get("depHour"));
                        String temphour = new String("06");

                        buffer.append("n출발역"+info.get("출발역"));
                        buffer.append("n출발날짜"+info.get("출발날짜"));
                        buffer.append("n출발시간"+info.get("출발시간"));
                        buffer.append("n도착역"+info.get("도착역"));
                        buffer.append("n도착날짜"+info.get("도착날짜"));
                        buffer.append("n도착시간"+info.get("도착시간"));
                        buffer.append("n열차"+info.get("열차")+" 열차번호:"+info.get("열차번호"));
                        buffer.append("n요금"+info.get("요금"));


                        break;*/


                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); //태그 이름 얻어오기

                        if(tag.equals("item")) buffer.append("\n");// 첫번째 검색결과종료..줄바꿈

                        break;
                }

                eventType= xpp.next();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch blocke.printStackTrace();
        }

        buffer.append("파싱 끝\n");

        return buffer.toString();//StringBuffer 문자열 객체 반환

    }
}