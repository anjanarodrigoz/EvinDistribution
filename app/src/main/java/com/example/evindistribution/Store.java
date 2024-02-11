package com.example.evindistribution;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.evindistribution.Adapter.StoreAdapter;
import com.example.evindistribution.Models.ItemDetails;
import com.example.evindistribution.Models.Search;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Store extends AppCompatActivity {

    RecyclerView storeRecycler;
    List<ItemDetails> itemDetails;
    StoreAdapter storeAdapter;
    Context context = this;
    DatabaseReference reference;
    ImageView imgBack;
    EditText tSearch;
    DbHandler db;
    TextView tTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);


        db = DbHandler.getInstance(context);
        storeRecycler = findViewById(R.id.storeRecycler);
        imgBack = findViewById(R.id.tImage);
        tSearch = findViewById(R.id.tSearch);
        tTotal = findViewById(R.id.tTotal);

        storeRecycler.setHasFixedSize(true);
        storeRecycler.setLayoutManager(new LinearLayoutManager(context));


        itemDetails = new ArrayList<>();
        readItems();


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        tSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String search = s.toString().trim();

                List<String> idList = db.getSearchID(search);
                searchItem(idList);


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void searchItem(List<String> idList) {

        reference = FirebaseDatabase.getInstance().getReference("ITEMS");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemDetails.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ItemDetails item = dataSnapshot.getValue(ItemDetails.class);
                    if (idList.contains(item.getId())) {
                        itemDetails.add(item);
                    }
                }
                storeAdapter = new StoreAdapter(context, itemDetails, 1);
                storeRecycler.setAdapter(storeAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void readItems() {



        reference = FirebaseDatabase.getInstance().getReference().child("ITEMS");
        List<String> idList = db.getSearchID(null);


        reference.addValueEventListener(new ValueEventListener() {
            double total = 0;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemDetails.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    ItemDetails item = dataSnapshot.getValue(ItemDetails.class);
                    if (!idList.contains(item.getId())) {
                        Search searchData = new Search(item.getId(), item.getId()+" "+item.getPrice() +" "+ item.getName());
                        db.createStore(searchData);

                    }
                    itemDetails.add(item);
                    total += (item.getPrice()*item.getStock());
                }

                tTotal.setText("Total = Rs."+total);

                storeAdapter = new StoreAdapter(context, itemDetails, 1);
                storeRecycler.setAdapter(storeAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public boolean isConnected(){

        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

                return true;
        }
        else{

            new AlertDialog.Builder(context)
                    .setTitle("Connection Failure")
                    .setMessage("Please Connect to the Internet")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return false;
        }
    }
}