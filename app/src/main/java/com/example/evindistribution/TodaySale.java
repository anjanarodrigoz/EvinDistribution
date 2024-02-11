package com.example.evindistribution;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.TextView;

import com.example.evindistribution.Adapter.TodayAdapter;
import com.example.evindistribution.Models.SaleDetails;
import com.example.evindistribution.Models.SoldItemDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TodaySale extends AppCompatActivity {

    Context context= this;
    RecyclerView todayRecycler;
    List<SoldItemDetails> itemList;
    TextView txtCost,txtSellPrice,txtProfit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_sale);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        txtCost= findViewById(R.id.totalCost);
        txtSellPrice = findViewById(R.id.totalSellPrice);
        txtProfit = findViewById(R.id.totalProfit);

        todayRecycler = findViewById(R.id.todaySaleRecycler);
        todayRecycler.setHasFixedSize(true);
        todayRecycler.setLayoutManager(new LinearLayoutManager(context));

        SaleDetails sale = (SaleDetails) getIntent().getSerializableExtra("SALE");
        itemList = new ArrayList<>();


        readItems(sale);
    }

    private void readItems(SaleDetails sale) {

        itemList.clear();

        double tPrice = 0,tCost=0,tSellPrice=0,tProfit = 0 ;

        for(int i = 0;i<sale.getItemId().size();i++){

            String id,name;
            int qty;
            double itemPrice,cost,sellPrice,profit;

            id = sale.getItemId().get(i);
            name = sale.getItemName().get(i);
            itemPrice = sale.getItemPrice().get(i);
            qty = sale.getQuantity().get(i);
            sellPrice = sale.getSalePrice().get(i);
            cost = itemPrice*qty;
            profit = sellPrice - cost;

            tCost +=cost;
            tSellPrice +=sellPrice;
            tProfit +=profit;

            SoldItemDetails soldItemDetails =  new SoldItemDetails(id,name,qty,itemPrice,cost,sellPrice,profit);
            itemList.add(soldItemDetails);
        }

        TodayAdapter adapter = new TodayAdapter(context,itemList);
        todayRecycler.setAdapter(adapter);


        txtCost.setText(""+tCost);
        txtSellPrice.setText(""+tSellPrice);
        txtProfit.setText(""+tProfit);

    }
}