package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ecommerce.Prevalent.prevalent;
import com.example.ecommerce.model.Carts;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AdminUserProducts extends AppCompatActivity
{
RecyclerView user_product_recycler;
DatabaseReference cartref;
String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_products);
        user_product_recycler=findViewById(R.id.user_products_recycler);
        cartref= FirebaseDatabase.getInstance().getReference().child("Cart List");
        user_product_recycler.setLayoutManager(new LinearLayoutManager(this));
        userid=getIntent().getStringExtra("uid");

    }
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<Carts> options=
                new FirebaseRecyclerOptions.Builder<Carts>().setQuery(cartref.
                        child("Admin view").child(userid).child("Products"),Carts.class).build();
        final FirebaseRecyclerAdapter<Carts, Cart.FindCartviewHolder> adatper=new FirebaseRecyclerAdapter<Carts, Cart.FindCartviewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final Cart.FindCartviewHolder holder, int position, @NonNull final Carts model)
            {
                holder.Cart_product_price.setText("Price - Rs "+model.getPrice()+" -/");
                holder.Cart_product_quantity.setText("Quantity - "+model.getQuantity());
                holder.Cart_product_name.setText(model.getPname());
                Picasso.get().load(model.getImage()).into(holder.Cart_product_image);
            }

            @NonNull
            @Override
            public Cart.FindCartviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout,parent,false);

                return new Cart.FindCartviewHolder(view);
            }
        };
        user_product_recycler.setAdapter(adatper);
        adatper.startListening();

    }
}