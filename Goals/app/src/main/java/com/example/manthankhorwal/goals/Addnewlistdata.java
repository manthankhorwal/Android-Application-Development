package com.example.manthankhorwal.goals;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashSet;

public class Addnewlistdata extends AppCompatActivity {
    EditText titlename;
    EditText totalques;
    public void fun(View view)
    {
      int flagtitle=0,flagtotal=0,flagpresent=0;
      String title=titlename.getText().toString();
      String total=totalques.getText().toString();
      if(!title.equals(""))
          flagtitle=1;
        if(!total.equals(""))
             flagtotal=1;
       if(flagtitle==1&&flagtotal==1)
       {
           for(int i=0;i<MainActivity.title.size();i++)
       {
           if(MainActivity.title.get(i).equals(title))
           {  flagpresent=1;
               Toast.makeText(this, "Entry already present", Toast.LENGTH_SHORT).show();
               break;
           }
       }
       if(flagpresent==0) {

           MainActivity.title.add(title);
           MainActivity.totalquestions.add(Integer.parseInt(total));
           MainActivity.donequestions.add(0);
           MainActivity.arrayAdapter.notifyDataSetChanged();
           Toast.makeText(this, "Added Successfully", Toast.LENGTH_SHORT).show();
           int size=MainActivity.title.size();
           String sql="INSERT INTO mygoals (indx,title,totalquestions,donequestions) VALUES (?,?,?,?)";
           SQLiteStatement statement=MainActivity.mydatabase.compileStatement(sql);
           statement.bindString(1,Integer.toString(size-1));
           statement.bindString(2,MainActivity.title.get(size-1));
           statement.bindString(3, String.valueOf(MainActivity.totalquestions.get(size-1)));
           statement.bindString(4, String.valueOf(MainActivity.donequestions.get(size-1)));
           statement.execute();

           if(MainActivity.title.size()==0)
           {
               MainActivity.textViewzero.setAlpha(1);
           }
           else
               MainActivity.textViewzero.setAlpha(0);

       }

       }
       else
           Toast.makeText(this, "Values Expected", Toast.LENGTH_SHORT).show();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnewlistdata);
        titlename=(EditText) findViewById(R.id.titlename);
        totalques=(EditText) findViewById(R.id.totalques);
    }
}
