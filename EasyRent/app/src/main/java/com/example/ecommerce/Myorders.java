package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.ecommerce.Prevalent.prevalent;
import com.example.ecommerce.model.MyOrdersclass;
import com.example.ecommerce.model.Myproductsclass;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Myorders extends AppCompatActivity
{

RecyclerView myorderRcycler;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myorders);

            myorderRcycler=findViewById(R.id.myorders_recycler);

       myorderRcycler.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onStart()
    {
        super.onStart();
        DatabaseReference myordersref= FirebaseDatabase.getInstance().getReference()
                .child("Orders").child(prevalent.currentonlineuser.getPhone())
                .child("productinthisorder");
        FirebaseRecyclerOptions<MyOrdersclass> options=new FirebaseRecyclerOptions.Builder<MyOrdersclass>()
                .setQuery(myordersref,MyOrdersclass.class).build();

        FirebaseRecyclerAdapter<MyOrdersclass, FindMyordersViewHolder> adapter=
                new FirebaseRecyclerAdapter<MyOrdersclass, FindMyordersViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull FindMyordersViewHolder holder, int position, @NonNull final MyOrdersclass model)
                    {

                        holder.name.setText("Name- " +model.getPname());
                        holder.price.setText("Price- Rs"+model.getPrice()+"/-");
                        Picasso.get().load(model.getImage()).into(holder.myproductImage);
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(Myorders.this,ProductDetails.class);
                                intent.putExtra("pid",model.getPid());
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public FindMyordersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_my_orders,parent,false);
                        return new FindMyordersViewHolder(view);
                    }
                };
        myorderRcycler.setAdapter(adapter);
        adapter.startListening();
    }
    public static class FindMyordersViewHolder extends RecyclerView.ViewHolder
    {
        ImageView myproductImage;
        TextView price,name;
        public FindMyordersViewHolder(@NonNull View itemView) {
            super(itemView);
            myproductImage=itemView.findViewById(R.id.myorders_product_image);
            price=itemView.findViewById(R.id.myorders_product_price);
            name=itemView.findViewById(R.id.myorders_product_name);

        }
    }
}