package com.example.manthankhorwal.guessthecelebrity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> celebrityurls=new ArrayList<String>();
    ArrayList<String> celebritynames=new ArrayList<String>();
    int chosenceleb=0;
    String[] answers=new String[4];
    int locationanswer=0;
    int incorrectlocation;
    Button button0;
    Button button1;
    Button button2;
    Button button3;
    ImageView imageView;

    public void getanswer(View view)
    {
         Button counter=(Button)view;
          String buttonpressed=counter.getTag().toString();
         if(buttonpressed.equals(Integer.toString(locationanswer)))
         {
             Toast.makeText(this, "Correct Answer", Toast.LENGTH_SHORT).show();
         }
         else
             Toast.makeText(this, "Incorrect! Correct is "+ celebritynames.get(chosenceleb), Toast.LENGTH_SHORT).show();
         nextquestion();
    }
   public class Downloadimage extends AsyncTask<String,Void,Bitmap>
   {

       @Override
       protected Bitmap doInBackground(String... urls) {
           try {
               URL url=new URL(urls[0]);
               HttpURLConnection connection=(HttpURLConnection) url.openConnection();
               InputStream in=connection.getInputStream();
               Bitmap mybitmap= BitmapFactory.decodeStream(in);
               return mybitmap;
           } catch (Exception e) {
               e.printStackTrace();
               return null;
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
              } catch (Exception e) {
                  e.printStackTrace();
                  return null;
              }
              return result;
          }

      }
      public void nextquestion()
      {
          try {
              Random rand = new Random();
              chosenceleb = rand.nextInt(celebrityurls.size());
              Downloadimage imagetask = new Downloadimage();
              Bitmap celebimage = imagetask.execute(celebrityurls.get(chosenceleb)).get();
              imageView.setImageBitmap(celebimage);

              locationanswer = rand.nextInt(4);


              for (int i = 0; i < 4; i++) {
                  if (locationanswer == i)
                      answers[i] = celebritynames.get(chosenceleb);
                  else {
                      incorrectlocation = rand.nextInt(celebrityurls.size());
                      while (incorrectlocation == chosenceleb)
                          incorrectlocation = rand.nextInt(celebrityurls.size());
                      answers[i] = celebritynames.get(incorrectlocation);
                  }

              }
              button0.setText(answers[0]);
              button1.setText(answers[1]);
              button2.setText(answers[2]);
              button3.setText(answers[3]);

          }catch (Exception e)
          {
              e.printStackTrace();
          }
      }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView=(ImageView)findViewById(R.id.imageView);

        button0=(Button)findViewById(R.id.button0);
        button1=(Button)findViewById(R.id.button1);
        button2=(Button)findViewById(R.id.button2);
        button3=(Button)findViewById(R.id.button3);
        String result="";
        Downloadtext task=new Downloadtext();
        try {
            result=task.execute("http://www.posh24.se/kandisar").get();
            String[] splitresult=result.split("<div id=\"webx_most_popular\">");
            Pattern p=Pattern.compile("img src=\"(.*?)\"");
            Matcher matcher=p.matcher(splitresult[0]);
            while(matcher.find())
            {
                celebrityurls.add(matcher.group(1));
            }
            p=Pattern.compile("alt=(.*?)/>");
            matcher=p.matcher(splitresult[0]);
            while(matcher.find())
            {
                celebritynames.add(matcher.group(1));
            }
            nextquestion();

        }
         catch (Exception e) {
            e.printStackTrace();
        }

    }
    }

