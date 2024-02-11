package com.example.evindistribution;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.evindistribution.Adapter.SoldAdpater;
import com.example.evindistribution.Adapter.StoreAdapter;
import com.example.evindistribution.Models.ItemDetails;
import com.example.evindistribution.Models.SaleDetails;
import com.example.evindistribution.Models.Search;
import com.example.evindistribution.Models.SoldItemDetails;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddSale extends AppCompatActivity {


    ImageView imgBack;
    FloatingActionButton btnAddItem, btnComplete;
    TextView textTotal,textDate;
    List<ItemDetails> mItemDetailsList;
    StoreAdapter storeAdapter;
    RecyclerView salerecycler;
    Context context = this;
    List<SoldItemDetails> mSoldItemDetailsList;
    SoldAdpater soldAdpater;
    AddSale activity = this;
    DbHandler db;
    DatabaseReference ref;
    Notification dialog;
    String todayDate;
    String saleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sale);

        imgBack = findViewById(R.id.hImage);
        btnAddItem = findViewById(R.id.hBtnAdd);
        btnComplete = findViewById(R.id.hBtnComplete);
        textTotal = findViewById(R.id.hTextTotal);
        textDate = findViewById(R.id.textDate);
        salerecycler = findViewById(R.id.addSaleRecycler);
        dialog = Notification.getInstance();
        db = DbHandler.getInstance(context);

        displayToday();

       ref = FirebaseDatabase.getInstance().getReference().child("ITEMS");

        salerecycler.setHasFixedSize(true);
        salerecycler.setLayoutManager(new LinearLayoutManager(context));
        mSoldItemDetailsList = new ArrayList<>();

        showSoldItem();

        textDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getCalanderDate();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showItems();
            }
        });

        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mSoldItemDetailsList.isEmpty()) {
                    dialog.createNotification("ADD SALE", "Item list is empty.Add items", false, "OK",context);
                } else {


                    saleId = todayDate.replaceAll("/", "");

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SALE").child(saleId);

                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {

                                dialog.createNotification("ADD SALE", "You Already Added Today Sale.", false, "OK",context);
                            } else {
                                Button btnYse = dialog.createNotification("ADD SALE", "Do You Want To Add Today Sale", true, "NO",context);

                                btnYse.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       yes();
                                   }
                               });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }


            }
        });

    }

    private void getCalanderDate() {

        Calendar calendar = Calendar.getInstance();

        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {

                month = month + 1;
                String date = year + "/" + String.format("%02d", month) + "/" +String.format("%02d",day);
                textDate.setText(date+" ");
                todayDate = date;
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void displayToday() {

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        todayDate = simpleDateFormat.format(date);
        textDate.setText(todayDate+" ");
    }


    public void yes() {



        dialog.dialogDismiss();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SALE").child(saleId);

        List<String> itemId = new ArrayList<>();
        List<String> itemName = new ArrayList<>();
        List<Integer> quantity = new ArrayList<>();
        List<Double> itemPrice = new ArrayList<>();
        List<Double> salePrice = new ArrayList<>();

        List<SoldItemDetails> itemList = db.getItems();
        for (SoldItemDetails item : itemList) {

            itemId.add(item.getId());
            itemName.add(item.getName());
            quantity.add(item.getQty());
            itemPrice.add(item.getItemPrice());
            salePrice.add(item.getSellPrice());
        }

        SaleDetails toadySale = new SaleDetails(todayDate, itemId, itemName, quantity, itemPrice, salePrice);
        reference.setValue(toadySale);
        db.deleteAll();
        showSoldItem();
        dialog.createNotification("COMPLETED","Today Sale Added Is Complete.",false,"OK",context);

    }


    private void showItems() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View saleAddView = getLayoutInflater().inflate(R.layout.single_sale_add, null);
        builder.setView(saleAddView);

        RecyclerView saleAddRecycler;
        Button btnClose;
        EditText wSearch;

        saleAddRecycler = saleAddView.findViewById(R.id.saleAddRecycler);
        btnClose = saleAddView.findViewById(R.id.saleAddClose);
        wSearch = saleAddView.findViewById(R.id.wSearch);
        mItemDetailsList = new ArrayList<>();

        AlertDialog alertDialog = builder.create();

        saleAddRecycler.setHasFixedSize(true);
        saleAddRecycler.setLayoutManager(new LinearLayoutManager(context));

        List<String> idList = db.getSearchID(null);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mItemDetailsList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    ItemDetails item = dataSnapshot.getValue(ItemDetails.class);
                    if (!idList.contains(item.getId())) {
                        Search searchData = new Search(item.getId(), item.getId()+" "+item.getPrice() + " " + item.getName());
                        db.createStore(searchData);

                    }
                    mItemDetailsList.add(item);
                }

                storeAdapter = new StoreAdapter(context, mItemDetailsList, 2, activity);
                saleAddRecycler.setAdapter(storeAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });

        wSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String search = s.toString().trim();

                List<String> idList = db.getSearchID(search);


                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        mItemDetailsList.clear();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            ItemDetails item = dataSnapshot.getValue(ItemDetails.class);
                            if (idList.contains(item.getId())) {
                                mItemDetailsList.add(item);
                            }
                        }
                        storeAdapter = new StoreAdapter(context, mItemDetailsList, 2, activity);
                        saleAddRecycler.setAdapter(storeAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        alertDialog.show();
    }


    public void showSoldItem() {

        mSoldItemDetailsList.clear();
        mSoldItemDetailsList = db.getItems();
        soldAdpater = new SoldAdpater(context, mSoldItemDetailsList, activity);
        salerecycler.setAdapter(soldAdpater);

        textTotal.setText(String.valueOf(getTotal(mSoldItemDetailsList)));

    }

    private Double getTotal(List<SoldItemDetails> mSoldItemDetailsList) {

        double total = 0;

        for (SoldItemDetails item : mSoldItemDetailsList) {

            total += item.getSellPrice();
        }
        return total;
    }


}