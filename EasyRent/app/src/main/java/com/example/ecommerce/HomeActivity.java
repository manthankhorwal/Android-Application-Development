package com.example.ecommerce;

import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Prevalent.prevalent;
import com.example.ecommerce.model.Product;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
  RecyclerView mrecycler;
  private DatabaseReference productref;
  String currentuserMobilenumber;
  CircleImageView profileimage;
  TextView name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        Paper.init(this);
        setSupportActionBar(toolbar);

        currentuserMobilenumber=Paper.book().read(prevalent.userphonekey);
        //Toast.makeText(this, currentuserMobilenumber, Toast.LENGTH_SHORT).show();
        mrecycler=findViewById(R.id.product_recycler);
        mrecycler.setLayoutManager(new GridLayoutManager(this,2));

productref=FirebaseDatabase.getInstance().getReference().child("Products");
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeActivity.this,Cart.class);
               // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
             //   finish();
            }
        });
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView=navigationView.getHeaderView(0);
         name=headerView.findViewById(R.id.user_profile_name);

      profileimage=headerView.findViewById(R.id.user_profile_image);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
      navigationView.bringToFront();

    navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item)
        {
           int id=item.getItemId();
           try {
               retrieveUserInfo();
           }
           catch (Exception e)
           {
               e.printStackTrace();
           }

            Toast.makeText(HomeActivity.this, id, Toast.LENGTH_SHORT).show();
           if(id==R.id.myProducts)
           {
               Intent intent=new Intent(HomeActivity.this,Myproducts.class);
             //  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
               startActivity(intent);
            //   finish();
           }
           else if(id==R.id.Add_new_products)
           {
               Intent intent=new Intent(HomeActivity.this,Admin_category.class);
              // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
               startActivity(intent);
              // finish();
           }
           else if(id==R.id.nav_orders)
           {
               Intent intent=new Intent(HomeActivity.this,Myorders.class);
             //  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
               startActivity(intent);

           }
           else if(id==R.id.nav_logout)
           {
               Paper.book().destroy();
               Intent intent=new Intent(HomeActivity.this,MainActivity.class);
               intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |  Intent.FLAG_ACTIVITY_CLEAR_TASK );
                startActivity(intent);
                finish();
           }
           else if(id==R.id.nav_settings)
           {
               Intent intent=new Intent(HomeActivity.this,Settings.class);
               intent.putExtra("Mobile number",currentuserMobilenumber);
            //   intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
           //    finish();
               startActivity(intent);
           }
           else if(id==R.id.nav_search)
           {
               Intent intent=new Intent(HomeActivity.this,SearchProducts.class);
           //    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
               startActivity(intent);
           //    finish();
           }
           drawer.closeDrawers();
            return true;
        }
    });
    }

    public void retrieveUserInfo()
    {
        DatabaseReference profilesetupref=FirebaseDatabase.getInstance().getReference().child("users");
        profilesetupref.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                HashMap<String,Object> map= (HashMap<String, Object>) snapshot.child(currentuserMobilenumber).getValue();
               if(map.containsKey("image"))
               {
                   Picasso.get().load(map.get("image").toString()).into(profileimage);
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
   try {
        name.setText(prevalent.currentonlineuser.getName());
   }catch (Exception e)
   {
       e.printStackTrace();
   }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onStart()
    {

        super.onStart();
        FirebaseRecyclerOptions<Product> options=new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(productref,Product.class).build();
        final FirebaseRecyclerAdapter<Product,FindProductsViewHolder> adapter=
                new FirebaseRecyclerAdapter<Product, FindProductsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull FindProductsViewHolder holder, int position, @NonNull final Product model)
                    {
                        holder.product_name.setText(model.getName());
                        holder.product_description.setText(model.getDescription());
                        Picasso.get().load(model.getImage()).into(holder.product_image);
                        holder.product_price.setText("Rs " +model.getPrice() +"/-");
                        if (model.getProductby().equals(prevalent.currentonlineuser.getPhone()))
                        {
                            holder.card.setCardBackgroundColor(getResources().getColor(R.color.FloralWhite));
                        }
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                             Intent intent=new Intent(HomeActivity.this,ProductDetails.class);
                             intent.putExtra("pid",model.getPid());
                             intent.putExtra("productby",model.getProductby());

                            //    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                             startActivity(intent);
                           //  finish();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public FindProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.products_items_layout,parent,false);

                        return new FindProductsViewHolder(view);


                    }
                };
          mrecycler.setAdapter(adapter);
          adapter.startListening();

    }

    public static class FindProductsViewHolder extends RecyclerView.ViewHolder {
   TextView product_name,product_description,product_price;
   ImageView product_image;
   CardView card;

        public FindProductsViewHolder(@NonNull View itemView)
        { super(itemView);
            product_price=itemView.findViewById(R.id.product_price1);
            product_name=itemView.findViewById(R.id.product_name1);
            product_description=itemView.findViewById(R.id.product_description1);
            product_image=itemView.findViewById(R.id.product_image1);
           card=itemView.findViewById(R.id.home_cardview);

        }
    }
}