package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Prevalent.prevalent;
import com.example.ecommerce.model.Myproductsclass;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.zip.Inflater;

public class Myproducts extends AppCompatActivity {
RecyclerView myproductsRecycler;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myproducts);
        myproductsRecycler=findViewById(R.id.myproducts_recycler);
        myproductsRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference myproductsref= FirebaseDatabase.getInstance().getReference()
                .child("MyProducts").child(prevalent.currentonlineuser.getPhone());
       FirebaseRecyclerOptions<Myproductsclass> options=new FirebaseRecyclerOptions.Builder<Myproductsclass>()
               .setQuery(myproductsref,Myproductsclass.class).build();

        FirebaseRecyclerAdapter<Myproductsclass,FindMyproductsViewHolder> adapter=new FirebaseRecyclerAdapter<Myproductsclass, FindMyproductsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final FindMyproductsViewHolder holder, int position, @NonNull final Myproductsclass model)
            {
                holder.name.setText("Name -" +model.getName());
                holder.price.setText("Rs- "+model.getPrice()+"/-");
                Picasso.get().load(model.getImage()).into(holder.myproductImage);
                final String uid=getRef(position).getKey();
                final String state;

               // Toast.makeText(Myproducts.this, uid, Toast.LENGTH_SHORT).show();
             holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                 @Override
                 public boolean onLongClick(View v)
                 {
                //     Toast.makeText(Myproducts.this, uid, Toast.LENGTH_SHORT).show();

                     AlertDialog.Builder builder=new AlertDialog.Builder(Myproducts.this);
                     builder.setTitle("Options");
                     builder.setMessage("Do you want to delete this product?");
                     builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which)
                         {
                             FirebaseDatabase.getInstance().getReference().child("MyProducts").child(prevalent.currentonlineuser.getPhone()).child(uid)
                                     .child("state").addListenerForSingleValueEvent(new ValueEventListener() {
                                 @Override
                                 public void onDataChange(@NonNull DataSnapshot snapshot)
                                   {
                                    if(snapshot.getValue().toString().equals("booked"))
                                    {
                                        Toast.makeText(Myproducts.this, "You Cannot delete product which is already booked", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        removeproducts(uid);
                                    }
                                 }

                                 @Override
                                 public void onCancelled(@NonNull DatabaseError error) {

                                 }
                             });

                         }
                     });
                     builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {
                         finish();
                         }
                     });
                     builder.show();
                 return false;
                 }
             });
                if(model.getState().equals("not booked"))
                {
                    holder.state_image.setImageResource(R.drawable.reddot);
                    holder.state.setText("Not Booked");

                }
                else if(model.getState().equals("booked"))
                {
                    holder.booked_by_details.setVisibility(View.VISIBLE);
                    FirebaseDatabase.getInstance().getReference().child("Rent By")
                            .child(prevalent.currentonlineuser.getPhone()).child(model.getPid())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot)
                        {
                            holder.booked_by_details.setText("Booked By -"+snapshot.child("name").getValue().toString()
                            +" "+ snapshot.child("phone").getValue().toString()
                            );
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error)
                        {

                        }
                    });
                    holder.state_image.setImageResource(R.drawable.greendot);
                    holder.state.setText("Booked");

                }
            }

            @NonNull
            @Override
            public FindMyproductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
              View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_my_products,parent,false);
              return new FindMyproductsViewHolder(view);
            }
        };
        myproductsRecycler.setAdapter(adapter);
        adapter.startListening();
    }

    private void removeproducts(String uid)
    {
        FirebaseDatabase.getInstance().getReference().child("MyProducts").child(prevalent.currentonlineuser.getPhone())
                .child(uid).removeValue();
        FirebaseDatabase.getInstance().getReference().child("Products").child(uid).removeValue();
        Toast.makeText(this, "Product Removed", Toast.LENGTH_SHORT).show();
    }

    public static class FindMyproductsViewHolder extends RecyclerView.ViewHolder
    {
        ImageView myproductImage,state_image;
        TextView price,name,state,booked_by_details;
        public FindMyproductsViewHolder(@NonNull View itemView)
        {
            super(itemView);
            myproductImage=itemView.findViewById(R.id.myproducts_product_image);
            state_image=itemView.findViewById(R.id.state_image);
            price=itemView.findViewById(R.id.myproducts_product_price);
            name=itemView.findViewById(R.id.myproducts_product_name);
            state=itemView.findViewById(R.id.myproducts_state);
            booked_by_details=itemView.findViewById(R.id.bookedby_details);
        }
    }
}