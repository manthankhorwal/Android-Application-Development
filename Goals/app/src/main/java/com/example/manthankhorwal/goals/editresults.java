package com.example.manthankhorwal.goals;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;

public class editresults extends AppCompatActivity {
 int positionid;
 TextView donequestionstext,topicshow,stilldoto;

    public void clickpositive(View view)
    {
           int data=MainActivity.donequestions.get(positionid);
           if(data<MainActivity.totalquestions.get(positionid)) {
               data++;
               MainActivity.donequestions.add(positionid, data);
               donequestionstext.setText(Integer.toString(data));
               int todo = MainActivity.totalquestions.get(positionid) - data;
               stilldoto.setText("Remaining-> "+Integer.toString(todo));


           }

        if(data==MainActivity.totalquestions.get(positionid))
        {
            Toast.makeText(this, "Congratulations!You acheived your goal", Toast.LENGTH_SHORT).show();

        }



    }
    public void clicknegative(View view)
    {
        int data=MainActivity.donequestions.get(positionid);
        if(data>0)
        {
        data--;
        MainActivity.donequestions.add(positionid,data);
        donequestionstext.setText(Integer.toString(data));
        int todo=MainActivity.totalquestions.get(positionid)-data;
        stilldoto.setText("Remaining-> "+Integer.toString(todo));
        }
    }
    public void editprogress(View view)
    { int data=MainActivity.donequestions.get(positionid);
      String check=MainActivity.title.get(positionid);
      String tocheck="Done";
      boolean presentornot=check.contains(tocheck);
       if(data==MainActivity.totalquestions.get(positionid)&&presentornot==false)
        {

           String previous=MainActivity.title.get(positionid)+"(Done)";
           MainActivity.title.set(positionid,previous);
           MainActivity.arrayAdapter.notifyDataSetChanged();
            ContentValues cv=new ContentValues();
           cv.put("title",MainActivity.title.get(positionid));
           MainActivity.mydatabase.update("mygoals",cv,"indx="+positionid,null);
        }
        else if(data<MainActivity.totalquestions.get(positionid)&&presentornot==true)
       {
                  String stringwithdone=MainActivity.title.get(positionid);
                  String toremovedone="";
                  for(int i=0;i<stringwithdone.length();i++)
                  {
                      if(stringwithdone.charAt(i)=='(')
                          break;
                      else
                          toremovedone+=stringwithdone.charAt(i);
                  }
           MainActivity.title.set(positionid,toremovedone);
           MainActivity.arrayAdapter.notifyDataSetChanged();
           ContentValues cv=new ContentValues();
           cv.put("title",MainActivity.title.get(positionid));
           MainActivity.mydatabase.update("mygoals",cv,"indx="+positionid,null);

       }
        ContentValues cv = new ContentValues();
      cv.put("donequestions",Integer.toString(data));
      MainActivity.mydatabase.update("mygoals",cv,"indx ="+positionid,null);
        Toast.makeText(this, "Updated Successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editresults);
        donequestionstext=(TextView)findViewById(R.id.donequesshowtext);
        topicshow=(TextView)findViewById(R.id.topicshown);
        stilldoto=(TextView)findViewById(R.id.stilldoto);
        Intent intent=getIntent();
        positionid=intent.getIntExtra("positionid",-1);
        int data=MainActivity.donequestions.get(positionid);
        donequestionstext.setText(Integer.toString(data));
        String check=MainActivity.title.get(positionid);
        String tocheck="Done";
        String show="";
        boolean presentornot=check.contains(tocheck);
        if(presentornot==true)
        {
            for(int i=0;i<check.length();i++)
            {
                if(check.charAt(i)=='(')
                    break;
                else
                    show+=check.charAt(i);
            }
            topicshow.setText(show);
        }
        else
        {
            topicshow.setText(check);
        }

        int todo=MainActivity.totalquestions.get(positionid)-data;
        stilldoto.setText("Remaining-> "+Integer.toString(todo));


    }
}
