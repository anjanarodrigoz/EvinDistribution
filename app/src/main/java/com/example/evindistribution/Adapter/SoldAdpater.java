package com.example.evindistribution.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evindistribution.AddSale;
import com.example.evindistribution.DbHandler;
import com.example.evindistribution.Models.ItemDetails;
import com.example.evindistribution.Models.SoldItemDetails;
import com.example.evindistribution.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class SoldAdpater extends RecyclerView.Adapter<SoldAdpater.ViewHolder> {

    Context context;
    List<SoldItemDetails> mItemDetails;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("ITEMS");
    AddSale key;

    public SoldAdpater(Context context, List<SoldItemDetails> mItemDetails, AddSale key) {
        this.context = context;
        this.mItemDetails = mItemDetails;
        this.key = key;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View soldDetailsView = LayoutInflater.from(context).inflate(R.layout.single_sale,parent,false);
        return new SoldAdpater.ViewHolder(soldDetailsView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SoldItemDetails item = mItemDetails.get(position);
        String id = item.getId();
        int itemQty = item.getQty();

        holder.tName.setText(item.getName());
        holder.tSellPrice.setText(item.getSellPrice()+"");
        holder.tItemPrice.setText(item.getItemPrice()+"");
        holder.tQty.setText(item.getQty()+"");
        holder.tProfit.setText(item.getProfit()+"");

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbHandler db = DbHandler.getInstance(context);
                db.deleteItem(id);
                key.showSoldItem();
                ref.child(id).child("stock").setValue(ServerValue.increment(itemQty));

            }
        });

    }

    @Override
    public int getItemCount() {
        return mItemDetails.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tItemPrice,tQty,tSellPrice,tName,tProfit;
        ImageView imgDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tItemPrice = itemView.findViewById(R.id.kitemPrice);
            tQty = itemView.findViewById(R.id.kQty);
            tSellPrice = itemView.findViewById(R.id.kSell);
            tName = itemView.findViewById(R.id.kName);
            imgDelete = itemView.findViewById(R.id.kimg);
            tProfit = itemView.findViewById(R.id.kProfit);
        }
    }
}
