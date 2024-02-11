package com.example.evindistribution.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evindistribution.Models.SoldItemDetails;
import com.example.evindistribution.R;

import java.util.List;

public class TodayAdapter extends RecyclerView.Adapter<TodayAdapter.ViewHolder> {

    Context mContext;
    List<SoldItemDetails> itemList;

    public TodayAdapter(Context mContext, List<SoldItemDetails> itemList) {
        this.mContext = mContext;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.single_today_sale,parent,false);

        return new TodayAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SoldItemDetails soldItem = itemList.get(position);

        holder.id.setText(soldItem.getId());
        holder.name.setText(soldItem.getName());
        holder.qty.setText(""+soldItem.getQty());
        holder.price.setText(""+soldItem.getItemPrice());
        holder.cost.setText(""+soldItem.getCost());
        holder.sellPrice.setText(""+soldItem.getSellPrice());
        holder.profit.setText(""+soldItem.getProfit());

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView  id,name,price,qty,cost,sellPrice,profit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.iItemID);
            qty = itemView.findViewById(R.id.iQty);
            name = itemView.findViewById(R.id.iItemName);
            price = itemView.findViewById(R.id.iItemPrice);
            cost = itemView.findViewById(R.id.iCost);
            sellPrice = itemView.findViewById(R.id.iSellPrice);
            profit = itemView.findViewById(R.id.iProfit);

        }
    }
}
