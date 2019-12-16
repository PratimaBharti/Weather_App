package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    Button gobutton;
    TextView result;
    EditText cityname;
    public class downloadjson extends AsyncTask<String,Void,String >{
        @Override
        protected String doInBackground(String... urls) {
            String result="";
            HttpsURLConnection connection=null;
            URL url;
            try {
                url=new URL(urls[0]);
                connection=(HttpsURLConnection) url.openConnection();
                InputStream in=connection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);
                int data=reader.read();
                while (data!=-1)
                {
                    char current=(char)data;
                    result+=current;
                    data=reader.read();
                }
                return result;
            }catch (Exception e)
            {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                String msg="";
                JSONObject jsonObject=new JSONObject(s);
                String weatherinfo=jsonObject.getString("weather");
                JSONArray array=new JSONArray(weatherinfo);
                String tempinfo=jsonObject.getString("main");
                JSONObject temp=new JSONObject(tempinfo);
                String temperature=temp.getString("temp");
                msg="Temperature : "+temperature+"C\r\n";
                for(int i=0;i<array.length();i++)
                {
                    JSONObject jsonpart=array.getJSONObject(i);
                    String main=jsonpart.getString("main");
                    String description=jsonpart.getString("description");
                    if(!main.equals("") && !description.equals("")){

                        msg+=" "+main+","+description;
                    }

                }
                if(!msg.equals("")){

                    result.setText(msg);
                }


            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityname=findViewById(R.id.cityname);
        gobutton=findViewById(R.id.gobutton);
        result=findViewById(R.id.result);





    }

    public void findweather(View view)
    {
        String s=cityname.getText().toString();
        downloadjson task=new downloadjson();
        task.execute("https://openweathermap.org/data/2.5/weather?q="+s+",India&appid=b6907d289e10d714a6e88b30761fae22");

        InputMethodManager mgr=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(cityname.getWindowToken(),0);

    }

}
