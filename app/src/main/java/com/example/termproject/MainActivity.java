package com.example.termproject;

import android.app.Activity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText edit1, edit2;
    TextView text;

    String TrainKey= "RD0BzjHbAJnBN1brAQq0R%2FJORqOCJU%2B56cy1%2F7blI1JiUoJFi%2FfEEbyFuYApB6DckZ19xn59cF52Sx1g9DsyHg%3D%3D";
    String BusKey = "K%2FhtglJ%2BZ0HWSHrD7sSotR0wXupBxdrOjcW8XN31U3HKGwA4f5E0ziTlQzUux9vN0htlydvoGMpHZcw17NX%2Btw%3D%3D";
    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit1 = (EditText)findViewById(R.id.EditDep);
        edit2 = (EditText)findViewById(R.id.EditLoc);
        text= (TextView)findViewById(R.id.text);
    }

    //Button을 클릭했을 때 자동으로 호출되는 callback method
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

    }


    String getXmlData(){

        StringBuffer buffer=new StringBuffer();

        String str1= edit1.getText().toString();//EditText에 작성된 Text얻어오기
        String str2= edit2.getText().toString();
        String departure = URLEncoder.encode(str1);
        String location = URLEncoder.encode(str2);//한글의 경우 인식이 안되기에 utf-8 방식으로 encoding     //지역 검색 위한 변수

        String queryUrl="http://openapi.tago.go.kr/openapi/service/TrainInfoService/getStrtpntAlocFndTrainInfo?serviceKey="//요청 URL
                + TrainKey +"&numOfRows=10&pageNo=1&arrPlaceId="+location+"&depPlaceId="+departure+"&depPlandTime=20201201";

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
                            buffer.append(xpp.getText());//arrplandtime
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
