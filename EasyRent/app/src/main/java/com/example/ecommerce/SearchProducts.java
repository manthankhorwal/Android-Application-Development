package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.model.Product;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SearchProducts extends AppCompatActivity {
    com.rey.material.widget.EditText SearchProductEdittext;
    Button search_btn;
    RecyclerView searchRecycler;
    String searchproductname="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_products);
        SearchProductEdittext = findViewById(R.id.editsearch);
        search_btn = findViewById(R.id.search_btn);
        searchRecycler = findViewById(R.id.search_recycler);
        searchRecycler.setLayoutManager(new GridLayoutManager(this, 2));

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchproductname = SearchProductEdittext.getText().toString();
                Toast.makeText(SearchProducts.this, searchproductname, Toast.LENGTH_SHORT).show();
                onStart();
            }
        });

    }

    @Override
    protected void onStart() {
        DatabaseReference searchref = FirebaseDatabase.getInstance().getReference().child("Products");
        super.onStart();

        FirebaseRecyclerOptions<Product> options = new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(searchref.orderByChild("name").startAt(searchproductname), Product.class).build();

        FirebaseRecyclerAdapter<Product, SearchViewHolder> adapter = new FirebaseRecyclerAdapter<Product, SearchViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SearchViewHolder holder, int position, @NonNull final Product model) {
                holder.product_name.setText(model.getName());
                holder.product_description.setText(model.getDescription());
                Picasso.get().load(model.getImage()).into(holder.product_image);
                holder.product_price.setText("Rs " + model.getPrice() + "/-");
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SearchProducts.this, ProductDetails.class);
                        intent.putExtra("pid", model.getPid());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                });
            }
            @NonNull
            @Override
            public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_items_layout, parent, false);

                return new SearchViewHolder(view);
            }
        };
        searchRecycler.setAdapter(adapter);

        adapter.startListening();
    }
    public static class SearchViewHolder extends RecyclerView.ViewHolder
    {
        TextView product_name,product_description,product_price;
        ImageView product_image;
        public SearchViewHolder(@NonNull View itemView)
        {

            super(itemView);
            product_price=itemView.findViewById(R.id.product_price1);
            product_name=itemView.findViewById(R.id.product_name1);
            product_description=itemView.findViewById(R.id.product_description1);
            product_image=itemView.findViewById(R.id.product_image1);


        }
    }
}

