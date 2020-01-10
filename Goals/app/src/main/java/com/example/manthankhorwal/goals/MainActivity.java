package com.example.manthankhorwal.goals;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.SQLClientInfoException;
import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {
    static ArrayList<String> title=new ArrayList<String>();
    static ArrayList<Integer> donequestions=new ArrayList<Integer>();
    static ArrayList<Integer> totalquestions=new ArrayList<Integer>();
    static TextView textViewzero;
    ListView listView;
    static ArrayAdapter arrayAdapter;
 static SQLiteDatabase mydatabase;
 public void reset()
 {
     title.clear();
     donequestions.clear();
     totalquestions.clear();
     arrayAdapter.notifyDataSetChanged();
     textViewzero.setAlpha(1);
 }
public void atstartsetlistview()
{
    Cursor c=mydatabase.rawQuery("SELECT * FROM mygoals",null);
    title.clear();
    donequestions.clear();
    totalquestions.clear();
    int index=c.getColumnIndex("indx");
    int titleindex=c.getColumnIndex("title");
    int totalquestionsindex=c.getColumnIndex("totalquestions");
    int donequestionsindex=c.getColumnIndex("donequestions");
    if(c.getCount()>0) {
        c.moveToFirst();
        do {
            title.add(c.getString(titleindex));
            donequestions.add(Integer.parseInt(c.getString(donequestionsindex)));
            totalquestions.add(Integer.parseInt(c.getString(totalquestionsindex)));
        } while (c.moveToNext());

arrayAdapter.notifyDataSetChanged();
    }

}
public void updatelistview()
{
    mydatabase.execSQL("DELETE FROM mygoals");
    Cursor c=mydatabase.rawQuery("SELECT * FROM mygoals",null);
    int index=c.getColumnIndex("indx");
    int titleindex=c.getColumnIndex("title");
    int totalquestionsindex=c.getColumnIndex("totalquestions");
    int donequestionsindex=c.getColumnIndex("donequestions");
    String sql="INSERT INTO mygoals (indx,title,totalquestions,donequestions) VALUES (?,?,?,?)";
    SQLiteStatement statement=mydatabase.compileStatement(sql);
    for(int i=0;i<title.size();i++)
    {
        statement.bindString(1,Integer.toString(i));
        statement.bindString(2,title.get(i));
        statement.bindString(3,Integer.toString(totalquestions.get(i)));
        statement.bindString(4,Integer.toString(donequestions.get(i)));
        statement.execute();


    }
   arrayAdapter.notifyDataSetChanged();

}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.addmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
            if(item.getItemId()==R.id.adddata)
            {Intent intent=new Intent(getApplicationContext(),Addnewlistdata.class);
        startActivity(intent);

        return true;
            }
            else if(item.getItemId()==R.id.resetit)
            {
                mydatabase.execSQL("DELETE FROM mygoals");
                reset();
            }
        return false;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mydatabase=getApplicationContext().openOrCreateDatabase("Goals",MODE_PRIVATE,null);
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS mygoals ( indx VARCHAR,title VARCHAR,totalquestions VARCHAR,donequestions VARCHAR)");
      // mydatabase.execSQL("DELETE FROM mygoals");
        listView=(ListView) findViewById(R.id.LISTVIEW);
        arrayAdapter=new ArrayAdapter(getApplicationContext(),R.layout.designlistview,title);
        listView.setAdapter(arrayAdapter);
        atstartsetlistview();
         textViewzero=findViewById(R.id.textViewzero);

        if(title.size()==0)
        {
            textViewzero.setAlpha(1);
        }
        else
            textViewzero.setAlpha(0);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                  Intent intent=new Intent(getApplicationContext(),editresults.class);
                  intent.putExtra("positionid",position);
                  startActivity(intent);

            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                       final int itemtodelete=position;
                new AlertDialog.Builder(MainActivity.this).setIcon(R.drawable.ic_launcher_background).setTitle("Are you sure")
                        .setMessage("Do you want to delete this entry?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            title.remove(itemtodelete);
                            donequestions.remove(itemtodelete);
                            totalquestions.remove(itemtodelete);
                        arrayAdapter.notifyDataSetChanged();
                        updatelistview();
                        if(title.size()==0)
                        {
                            textViewzero.setAlpha(1);
                        }
                        else
                            textViewzero.setAlpha(0);

                    }
                }).setNegativeButton("No",null).show();

                return true;
            }
        });

    }
}
