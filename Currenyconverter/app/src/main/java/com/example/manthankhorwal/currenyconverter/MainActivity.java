package com.example.manthankhorwal.currenyconverter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
   public void convertrs(View view)
   {
       EditText edit=(EditText)findViewById(R.id.editText);
      String amount=edit.getText().toString();
      Double a=Double.parseDouble(amount);
       a= a*70;
    String amoun=String.format("%.2f",a);
       Toast.makeText(this,amoun, Toast.LENGTH_SHORT).show();
   }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
