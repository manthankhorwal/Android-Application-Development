package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.ecommerce.Prevalent.prevalent;
import com.example.ecommerce.model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetails extends AppCompatActivity {
ImageView productimage;
ElegantNumberButton numberButton;
TextView productprice,productDescription,productname;
FloatingActionButton addtocardbtn;
String productid,state="normal",productby;
String imageuri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        addtocardbtn=findViewById(R.id.cart_btn);
        productimage=findViewById(R.id.product_image_details);
        numberButton=findViewById(R.id.number_btn);
        productprice=findViewById(R.id.product_price_details);
        productDescription=findViewById(R.id.product_description_details);
        productid=getIntent().getStringExtra("pid");
        productby=getIntent().getStringExtra("productby");
        productname=findViewById(R.id.product_name_details);
        if(productby.equals(prevalent.currentonlineuser.getPhone()))
            addtocardbtn.setEnabled(false);

        getproductdetails(productid);
        addtocardbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(state.equals("shipped") || state.equals("order placed"))
                {
                    Toast.makeText(ProductDetails.this, "You can purchase more products once your previous order is delivered", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    getuserbyproduct();

                }

            }
        });
    }

    private void getuserbyproduct()
    {

        DatabaseReference productbyuserref=FirebaseDatabase.getInstance().getReference().child("Products").child(productid);
        productbyuserref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                HashMap<String,Object> save= (HashMap<String, Object>) snapshot.getValue();
                addingtoCart(save.get("productby").toString());
            //    Toast.makeText(ProductDetails.this, productbyuser, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkordestate();
    }

    private void addingtoCart(String productbyuser)
    {
        String savecurrentTime,saveCurrentDate;

        Calendar calfordate=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate=currentDate.format(calfordate.getTime());

        SimpleDateFormat currenttime=new SimpleDateFormat("HH:mm:ss a");
        savecurrentTime=currentDate.format(calfordate.getTime());

     final  DatabaseReference cartListRef=FirebaseDatabase.getInstance().getReference().child("Cart List");
        final HashMap<String,Object> cartMap=new HashMap<>();
   //getting number of user whose product is this

        cartMap.put("pid",productid);
        cartMap.put("pname",productname.getText().toString());
        cartMap.put("price",productprice.getText().toString());
        cartMap.put("date",saveCurrentDate);
        cartMap.put("time",savecurrentTime);
        cartMap.put("quantity",numberButton.getNumber());
        cartMap.put("discount","");
        cartMap.put("image",imageuri);
        cartMap.put("productby",productbyuser);
        cartListRef.child("user view").child(prevalent.currentonlineuser.getPhone())
                .child("Products").child(productid)
                .setValue(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
              if(task.isSuccessful())
              {

                  cartListRef.child("Admin view").child(prevalent.currentonlineuser.getPhone())
                          .child("Products").child(productid).updateChildren(cartMap)
                          .addOnCompleteListener(new OnCompleteListener<Void>() {
                              @Override
                              public void onComplete(@NonNull Task<Void> task) {
                                  if(task.isSuccessful())
                                  {
                                      Toast.makeText(ProductDetails.this, "Product Added to Cart", Toast.LENGTH_SHORT).show();
                                  }
                              }
                          });

              }
            }
        });
    }

    private void getproductdetails(final String productid)
    {
        DatabaseReference productref=FirebaseDatabase.getInstance().getReference().child("Products");

        productref.child(productid).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                Product product=snapshot.getValue(Product.class);
             productname.setText(product.getName());
             productDescription.setText(product.getDescription());
             productprice.setText(product.getPrice());
             imageuri=product.getImage();
                Picasso.get().load(product.getImage()).placeholder(R.drawable.select_product_image).into(productimage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });

    }
    private void checkordestate()
    {
        DatabaseReference orderref;
        orderref=FirebaseDatabase.getInstance().getReference()
                .child("Orders").child(prevalent.currentonlineuser.getPhone());
        orderref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists())
                {
                    String shippingstate=snapshot.child("state").getValue().toString();
                    String username=snapshot.child("name").getValue().toString();

                    if(shippingstate.equals("shipped"))
                     {
                         state="shipped";
                     }
                    else if(shippingstate.equals("not shipped"))
                    {
                        state="order placed";
                       }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}