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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.ecommerce.model.AdminNewOrderclass;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminNewOrders extends AppCompatActivity
{

RecyclerView newordersrecycler;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);
         newordersrecycler=findViewById(R.id.admin_orders_recycler);
        newordersrecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart()
    {
        DatabaseReference admincartref;
        admincartref= FirebaseDatabase.getInstance().getReference().child("Orders");

        super.onStart();

        FirebaseRecyclerOptions<AdminNewOrderclass> options=new FirebaseRecyclerOptions.Builder<AdminNewOrderclass>()
             .setQuery(admincartref,AdminNewOrderclass.class).build();
        FirebaseRecyclerAdapter<AdminNewOrderclass,FindAdminnewOrder> adapter=new FirebaseRecyclerAdapter<AdminNewOrderclass, FindAdminnewOrder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FindAdminnewOrder holder, final int position, @NonNull AdminNewOrderclass model)
            {
              holder.name.setText(model.getName());
              holder.address.setText(model.getAddress());
              holder.phonenumber.setText(model.getPhone());
              holder.orderat.setText(model.getDate() +" "+ model.getTime());
              holder.price.setText(model.getTotalamount());
              holder.see_product_details.setOnClickListener(new View.OnClickListener()
              {
                  @Override
                  public void onClick(View v)
                  {
                        String uid=getRef(position).getKey();
                      Intent intent=new Intent(AdminNewOrders.this,AdminUserProducts.class);
                      intent.putExtra("uid",uid);
                      startActivity(intent);finish();
                  }
              });
              holder.itemView.setOnLongClickListener(new View.OnLongClickListener()
              {
                  @Override
                  public boolean onLongClick(View v)
                  {

                      AlertDialog.Builder builder=new AlertDialog.Builder(AdminNewOrders.this);
                      builder.setTitle("Order State");
                      builder.setMessage("Have you shipped the product the order" );
                      builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
                      {
                          @Override
                          public void onClick(DialogInterface dialog, int which)
                          {
                              String uid=getRef(position).getKey();
                                   RemoveOrder(uid);
                          }
                      }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialog, int which) {
                              finish();
                          }
                      });
                      builder.show();
                      return  true;
                  }
              });


            }

            @NonNull
            @Override
            public FindAdminnewOrder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_admin_new_orders,parent,false);

                return new FindAdminnewOrder(view);
            }
        };

newordersrecycler.setAdapter(adapter);
adapter.startListening();
    }
    public static class FindAdminnewOrder extends RecyclerView.ViewHolder
{   TextView price,name,phonenumber,address,orderat;
Button see_product_details;
    public FindAdminnewOrder(@NonNull View itemView)
    {
        super(itemView);
  see_product_details=itemView.findViewById(R.id.show_product_btn);
        price=itemView.findViewById(R.id.new_order_amount);
        name=itemView.findViewById(R.id.new_order_name);
        address=itemView.findViewById(R.id.new_order_address);
        orderat=itemView.findViewById(R.id.new_order_orderat);
        phonenumber=itemView.findViewById(R.id.new_order_phone);

    }
}
public void RemoveOrder(String uid)
{
    FirebaseDatabase.getInstance().getReference().child("Orders").child(uid).removeValue();
}
}