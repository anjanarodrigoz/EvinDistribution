package com.example.evindistribution.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evindistribution.Models.SaleDetails;
import com.example.evindistribution.Notification;
import com.example.evindistribution.R;
import com.example.evindistribution.TodaySale;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.io.Serializable;
import java.util.List;
import java.util.zip.Inflater;

public class SaleAdapter  extends RecyclerView.Adapter<SaleAdapter.ViewHolder> {

    Context mContext;
    List<SaleDetails> mSaleDetailList;

    public SaleAdapter(Context mContext, List<SaleDetails> mSaleDetailList) {
        this.mContext = mContext;
        this.mSaleDetailList = mSaleDetailList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_today,parent,false);

        return new SaleAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SaleDetails sale = mSaleDetailList.get(position);

        double total = sale.getTotal();

        holder.txtDate.setText(sale.getSaleId());
        holder.txtTotal.setText("Rs."+total);
        holder.txtProfit.setText("Rs."+sale.getProfit(total));



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, TodaySale.class);
                intent.putExtra("SALE", sale);
                mContext.startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String saleId = sale.getSaleId().replaceAll("/", "");
                 Notification dialog = Notification.getInstance();
                Button yes = dialog.createNotification("Delete Sale","Do You Want to Delete Sale?",true,"No",mContext);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                        int i = 0;
                        for(String id :sale.getItemId()){
                            ref.child("ITEMS").child(id).child("stock").setValue(ServerValue.increment(sale.getQuantity().get(i)));
                            i+=1;
                        }
                        ref.child("SALE").child(saleId).removeValue();
                        dialog.dialogDismiss();
                    }
                });
                return false;
            }
        });
 }

    @Override
    public int getItemCount() {
        return mSaleDetailList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtDate,txtTotal,txtProfit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtDate = itemView.findViewById(R.id.uDate);
            txtTotal = itemView.findViewById(R.id.uDisplayTotal);
            txtProfit = itemView.findViewById(R.id.uDisplayProfit);



        }
    }
}
