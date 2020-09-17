package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Prevalent.prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ConfirmFinalOrder extends AppCompatActivity {
EditText nameEditText,phoneEditText,addressEditText,cityEditText;
Button confirmOrderBtn;
String totalprice,savecurrentdate,savecurrenttime;
TextView txtmsg1;
DatabaseReference productbyref;
HashMap<String,Object> fulldetails;
Button home_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);
       confirmOrderBtn=findViewById(R.id.next_button);
        nameEditText=findViewById(R.id.shippment_enter_name);
        addressEditText=findViewById(R.id.shippment_enter_address);
       cityEditText=findViewById(R.id.shippment_enter_city);
        phoneEditText=findViewById(R.id.shippment_enter_mobile);
       txtmsg1=findViewById(R.id.msg1);
       home_btn=findViewById(R.id.home_button_confirm);
       productbyref=FirebaseDatabase.getInstance().getReference().child("MyProducts");
  fulldetails=new HashMap<>();
       totalprice=getIntent().getStringExtra("total price");
        Toast.makeText(this, totalprice, Toast.LENGTH_SHORT).show();

        confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                check();
            }
        });
        home_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(ConfirmFinalOrder.this,HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void check()
    {
        if(TextUtils.isEmpty(nameEditText.getText().toString()))
        {
            Toast.makeText(this, "Enter Name", Toast.LENGTH_SHORT).show();
        }
       else if(TextUtils.isEmpty(phoneEditText.getText().toString()))
        {
            Toast.makeText(this, "Enter Mobile number", Toast.LENGTH_SHORT).show();
        }
       else if(TextUtils.isEmpty(cityEditText.getText().toString()))
        {
            Toast.makeText(this, "Enter city", Toast.LENGTH_SHORT).show();
        }
       else if(TextUtils.isEmpty(addressEditText.getText().toString()))
        {
            Toast.makeText(this, "Enter Address", Toast.LENGTH_SHORT).show();
        }
       else
        {
            GotoproductbyOfAllProductsAndChangeStateToBooked();
            ConfirmOrder();
        }


    }
    private void GotoMyPRoductsAndhangeState(Object productby, Object pid)
    {
        final String productbyuser=productby.toString();
        final String productid=pid.toString();
        final HashMap<String,Object> rentbyinfo=new HashMap<>();
        rentbyinfo.put("phone",phoneEditText.getText().toString());
        rentbyinfo.put("address",addressEditText.getText().toString());
        rentbyinfo.put("name",nameEditText.getText().toString());
        productbyref.child(productbyuser).child(productid)
                .child("state").setValue("booked").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                FirebaseDatabase.getInstance().getReference().child("Rent By")
                        .child(productbyuser).child(productid).setValue(rentbyinfo);
                Toast.makeText(ConfirmFinalOrder.this, "Done", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void GotoproductbyOfAllProductsAndChangeStateToBooked()
    {

     DatabaseReference cartref=FirebaseDatabase.getInstance().getReference()
     .child("Cart List").child("user view").child(prevalent.currentonlineuser.getPhone())
     .child("Products");

     cartref.addListenerForSingleValueEvent(new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull DataSnapshot snapshot) {
             for (DataSnapshot snapshot1 : snapshot.getChildren())
             {
                   fulldetails.put(snapshot1.getKey().toString(),snapshot1.getValue());
                  HashMap<String, Object> mymap = (HashMap<String, Object>) snapshot1.getValue();
                 GotoMyPRoductsAndhangeState(mymap.get("productby"), mymap.get("pid"));
             }
         }

         @Override
         public void onCancelled(@NonNull DatabaseError error) {

         }
     });
    }


    private void ConfirmOrder()
    {

        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat currentdate=new SimpleDateFormat("MM dd,yyyy");
        savecurrentdate=currentdate.format(calendar.getTime());

        SimpleDateFormat currenttime=new SimpleDateFormat("HH:mm:ss a");
        savecurrenttime=currenttime.format(calendar.getTime());
        DatabaseReference orderref= FirebaseDatabase.getInstance().getReference().child("Orders");

        HashMap<String,Object> ordermap=new HashMap<>();
        ordermap.put("address",addressEditText.getText().toString());
        ordermap.put("name",nameEditText.getText().toString());
        ordermap.put("city",cityEditText.getText().toString());
        ordermap.put("phone",phoneEditText.getText().toString());
        ordermap.put("time",savecurrenttime);
        ordermap.put("date",savecurrentdate);
        ordermap.put("state","not shipped");

        ordermap.put("totalamount",totalprice);

        orderref.child(prevalent.currentonlineuser.getPhone()).setValue(ordermap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                            if(task.isSuccessful())
                            {
                               // Toast.makeText(ConfirmFinalOrder.this, "Orders made", Toast.LENGTH_SHORT).show();
                              FirebaseDatabase.getInstance().getReference()
                                      .child("Cart List").child("user view").child(prevalent.currentonlineuser.getPhone()).removeValue()
                                      .addOnCompleteListener(new OnCompleteListener<Void>() {
                                          @Override
                                          public void onComplete(@NonNull Task<Void> task) {
                                          if(task.isSuccessful())
                                          {
                                              Toast.makeText(ConfirmFinalOrder.this, "Order Placed Successfully", Toast.LENGTH_SHORT).show();
                                          Intent intent=new Intent(ConfirmFinalOrder.this,HomeActivity.class);
                                          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                          startActivity(intent);
                                          finish();
                                          }
                                          }
                                      });
                            }
                    }
                });

    }
    public void onBackPressed()
    {

    }
    @Override
    protected void onStop()
    {
        super.onStop();

        FirebaseDatabase.getInstance().getReference()
                .child("Orders").child(prevalent.currentonlineuser.getPhone()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                HashMap<String,Object> mymap= (HashMap<String, Object>) snapshot.getValue();
                mymap.put("productinthisorder",fulldetails);
                FirebaseDatabase.getInstance().getReference().child("Orders")
                        .child(prevalent.currentonlineuser.getPhone()).setValue(mymap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });


    }
}