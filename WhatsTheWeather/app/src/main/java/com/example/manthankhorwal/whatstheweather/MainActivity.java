package com.example.manthankhorwal.whatstheweather;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

     AutoCompleteTextView at;
     TextView resulttemperature;
    TextView resultdescription;
    TextView resultmain;
    String[] country_list = { "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh",
            "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jammu and Kashmir", "Jharkhand", "Karnataka",
            "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha",
            "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttarakhand",
            "Uttar Pradesh", "West Bengal", "Andaman and Nicobar Islands", "Chandigarh",
            "Dadra and Nagar Haveli", "Daman and Diu", "Delhi", "Lakshadweep",
            "Puducherry","India","Afghanistan","Albania","Algeria","Andorra","Angola",
            "Anguilla","Antigua &amp; Barbuda","Argentina","Armenia","Aruba","Australia"
            ,"Austria","Azerbaijan","Bahamas","Bahrain","Bangladesh","Barbados",
            "Belarus","Belgium","Belize","Benin","Bermuda","Bhutan","Bolivia","Bosnia &amp; " +
            "Herzegovina","Botswana","Brazil","British Virgin Islands","Brunei","Bulgaria",
            "Burkina Faso","Burundi","Cambodia","Cameroon","Cape Verde","Cayman Islands","Chad",
            "Chile","China","Colombia","Congo","Cook Islands","Costa Rica","Cote D Ivoire","Croatia",
            "Cruise Ship","Cuba","Cyprus","Czech Republic","Denmark","Djibouti","Dominica","" +
            "Dominican Republic","Ecuador","Egypt","El Salvador","Equatorial Guinea","Estonia","Ethiopia",
            "Falkland Islands","Faroe Islands","Fiji","Finland","France","French Polynesia","French West Indies","Gabon","Gambia","Georgia","Germany","Ghana","Gibraltar","Greece","Greenland","Grenada","Guam","Guatemala","Guernsey","Guinea","Guinea Bissau","Guyana","Haiti","Honduras","Hong Kong","Hungary","Iceland","India","Indonesia","Iran","Iraq","Ireland","Isle of Man","Israel","Italy","Jamaica","Japan","Jersey","Jordan","Kazakhstan","Kenya","Kuwait","Kyrgyz Republic","Laos","Latvia","Lebanon","Lesotho","Liberia","Libya","Liechtenstein","Lithuania","Luxembourg","Macau","Macedonia","Madagascar","Malawi","Malaysia",
            "Maldives","Mali","Malta","Mauritania","Mauritius","Mexico","Moldova","Monaco","Mongolia","Montenegro","Montserrat","Morocco","Mozambique","Namibia","Nepal","Netherlands","Netherlands Antilles","New Caledonia","New Zealand","Nicaragua","Niger","Nigeria","Norway","Oman","Pakistan","Palestine","Panama","Papua New Guinea","Paraguay","Peru","Philippines","Poland","Portugal","Puerto Rico","Qatar","Reunion","Romania","Russia","Rwanda","Saint Pierre &amp; Miquelon","Samoa","San Marino","Satellite","Saudi Arabia","Senegal","Serbia","Seychelles","Sierra Leone","Singapore","Slovakia","Slovenia","South Africa","South Korea","Spain","Sri Lanka","St Kitts &amp; Nevis","St Lucia","St Vincent","St. Lucia","Sudan","Suriname","Swaziland","Sweden","Switzerland","Syria","Taiwan","Tajikistan","Tanzania","Thailand","Timor L'Este","Togo","Tonga","Trinidad &amp; Tobago","Tunisia","Turkey","Turkmenistan","Turks &amp; Caicos","Uganda","Ukraine","United Arab Emirates","United Kingdom","Uruguay","Uzbekistan","Venezuela","Vietnam","Virgin Islands (US)","Yemen","Zambia","Zimbabwe"};

     public void getWeather(View view) {
         if(at.getText().toString().equals(""))
             Toast.makeText(this, "Enter a Valid Name", Toast.LENGTH_SHORT).show();
         else
         {
             String city = at.getText().toString();
             Downloadtext task = new Downloadtext();
         try {
             String encoded = URLEncoder.encode(at.getText().toString(), "UTF-8");
             task.execute("https://openweathermap.org/data/2.5/weather?q=" + city + "&appid=b6907d289e10d714a6e88b30761fae22");
             InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
             mgr.hideSoftInputFromWindow(at.getWindowToken(), 0);
         } catch (Exception e) {
             Toast.makeText(getApplicationContext(), "Can't find Weather!Try Again", Toast.LENGTH_SHORT).show();

             e.printStackTrace();

         }
     }
     }
    public class Downloadtext extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... urls) {

            String result="";
            URL url;
            HttpURLConnection urlConnection=null;
            try {
                url=new URL(urls[0]);
                urlConnection=(HttpURLConnection)url.openConnection();
                InputStream in=urlConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);
                int data=reader.read();
                while(data!=-1)
                {
                    char current=(char)data;
                    result+=current;
                    data=reader.read();
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Can't find Weather!Try Again", Toast.LENGTH_SHORT).show();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                String message="";
                JSONObject jsonObject=new JSONObject(s);
                String weatherinfo=jsonObject.getString("weather");
                JSONArray arr=new JSONArray(weatherinfo);
                String main="";
                String description="";
                for(int i=0;i<arr.length();i++)
                {
                    JSONObject jsonpart=arr.getJSONObject(i);

                     main=jsonpart.getString("main");
                     description=jsonpart.getString("description");

                }
                String temperature=jsonObject.getString("main");
                JSONObject tempobj=new JSONObject(temperature);
                String temp=tempobj.getString("temp");
                message=temp+" "+main+" "+description;
                if(!message.equals("")&&!main.equals("")&&!description.equals(""))
                {
                    resultdescription.setText("Description -> "+ description);
                    resulttemperature.setText("Temperature -> "+temp+"°C");
                    resultmain.setText("Weather -> "+main);
                }
                else
                    Toast.makeText(getApplicationContext(), "Can't find Weather!Try Again", Toast.LENGTH_SHORT).show();


            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Can't find Weather!Try Again", Toast.LENGTH_SHORT).show();
            }

        }
    }
       @Override
    protected void onCreate(Bundle savedInstanceState) {
           super.onCreate(savedInstanceState);
           setContentView(R.layout.activity_main);
           at=(AutoCompleteTextView)findViewById(R.id.autoCompleteTextView2);
           resulttemperature=(TextView)findViewById(R.id.temperature);
           resultmain=(TextView)findViewById(R.id.main);
           resultdescription=(TextView)findViewById(R.id.Description);
           ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item,country_list);
        at.setAdapter(arrayAdapter);
     }
}

