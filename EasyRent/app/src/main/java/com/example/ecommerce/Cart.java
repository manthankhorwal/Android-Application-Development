package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Prevalent.prevalent;
import com.example.ecommerce.model.Carts;
import com.example.ecommerce.model.Product;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart extends AppCompatActivity
{
RecyclerView recyclerView;
Button NextProcessBtn;
TextView textTotalAmt,txtmsg;
DatabaseReference cartref,calculatepriceref;
int alltypeproduct=0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        recyclerView=findViewById(R.id.cart_recycler);
      textTotalAmt=findViewById(R.id.total_price);
      recyclerView.setLayoutManager(new LinearLayoutManager(this));
      cartref=FirebaseDatabase.getInstance().getReference().child("Cart List");
      txtmsg=findViewById(R.id.msg1);
    NextProcessBtn=findViewById(R.id.next_btn);
        checkordestate();
    NextProcessBtn.setOnClickListener(new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            Intent intent=new Intent(Cart.this,ConfirmFinalOrder.class);
            intent.putExtra("total price",Integer.toString(alltypeproduct));
            startActivity(intent);
        }
    });

    }
    @Override
    protected void onStart()
    {
        super.onStart();
        textTotalAmt.setText(Integer.toString(alltypeproduct));
        FirebaseRecyclerOptions<Carts> options=
                new FirebaseRecyclerOptions.Builder<Carts>().setQuery(cartref.
                        child("user view").child(prevalent.currentonlineuser.getPhone()).child("Products"),Carts.class).build();
        final FirebaseRecyclerAdapter<Carts,FindCartviewHolder> adatper=new FirebaseRecyclerAdapter<Carts, FindCartviewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final FindCartviewHolder holder, int position, @NonNull final Carts model)
            {
             holder.Cart_product_price.setText("Price - Rs "+model.getPrice()+" -/");
             holder.Cart_product_quantity.setText("Quantity - "+model.getQuantity());
             holder.Cart_product_name.setText(model.getPname());
                Picasso.get().load(model.getImage()).into(holder.Cart_product_image);
                int onetypeproduct=Integer.parseInt(model.getPrice()) * Integer.parseInt(model.getQuantity());
                alltypeproduct+=onetypeproduct;
              holder.itemView.setOnClickListener(new View.OnClickListener()
              {
                  @Override
                  public void onClick(View v)
                  {
                   CharSequence options[]=new CharSequence[]
                           {
                              "Edit"
                              ,"Remove"
                           } ;

                      AlertDialog.Builder builder=new AlertDialog.Builder(Cart.this);
                      builder.setIcon(R.mipmap.ic_launcher);
                      builder.setTitle(model.getPname());
                     // builder.setMessage("Choose Option:");
                      builder.setItems(options, new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialog, int i)
                          {
                            if(i==0) //Edit
                            {
                                Intent intent=new Intent(Cart.this,ProductDetails.class);
                                intent.putExtra("pid",model.getPid());
                                startActivity(intent);
                            }
                            else if(i==1)
                            {
                             cartref.child("user view").child(prevalent.currentonlineuser.getPhone())
                                     .child("Products").child(model.getPid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                 @Override
                                 public void onComplete(@NonNull Task<Void> task)
                                 {
                                   if(task.isSuccessful())
                                   {
                                       Toast.makeText(Cart.this, "Item Removed", Toast.LENGTH_SHORT).show();
                                       cartref.child("Admin view").child(prevalent.currentonlineuser.getPhone())
                                               .child("Products").child(model.getPid()).removeValue();
                                       }
                                 }
                             });
                            }
                          }
                      });
                      builder.show();

                  }
              });


            }

            @NonNull
            @Override
            public FindCartviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout,parent,false);

                return new FindCartviewHolder(view);
            }
        };
        recyclerView.setAdapter(adatper);

        adatper.startListening();

    }
    public static class FindCartviewHolder extends RecyclerView.ViewHolder
    {
        TextView Cart_product_name,Cart_product_quantity,Cart_product_price;
        ImageView Cart_product_image;
        public FindCartviewHolder(@NonNull View itemView)
        { super(itemView);
           Cart_product_image=itemView.findViewById(R.id.cart_product_image);
           Cart_product_quantity=itemView.findViewById(R.id.cart_product_quantity);
            Cart_product_price=itemView.findViewById(R.id.cart_product_price);
            Cart_product_name=itemView.findViewById(R.id.cart_product_name);
        }
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
                        txtmsg.setText("Congratulations , your final order has been placed successfully,Soon you will receive your order as your doorstep");

                        txtmsg.setVisibility(View.VISIBLE);
                             textTotalAmt.setText("Dear " +username+ " \n order " +
                                     "is shipped successfully");
                           recyclerView.setVisibility(View.GONE);
                           NextProcessBtn.setActivated(false);
                        Toast.makeText(Cart.this, "You can purchase more products once your previous order is delivered", Toast.LENGTH_SHORT).show();
                    }
                    else if(shippingstate.equals("not shipped"))
                    {
                        txtmsg.setVisibility(View.VISIBLE);
                      textTotalAmt.setText("Not Shipped");
                      recyclerView.setVisibility(View.GONE);
                      NextProcessBtn.setActivated(false);
                        Toast.makeText(Cart.this, "You can purchase more products once your previous order is delivered", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}