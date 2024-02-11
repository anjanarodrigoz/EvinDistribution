package com.example.evindistribution.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evindistribution.AddSale;
import com.example.evindistribution.DbHandler;
import com.example.evindistribution.Models.ItemDetails;
import com.example.evindistribution.Models.SoldItemDetails;
import com.example.evindistribution.Notification;
import com.example.evindistribution.R;
import com.example.evindistribution.Store;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.zip.Inflater;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder> {

    Context context;
    AddSale key;
    List<ItemDetails> itemDetailsList;
    int where;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("ITEMS");
    StoreAdapter storeAdapter = this;
    DbHandler dbHandler;


    public StoreAdapter(Context context, List<ItemDetails> itemDetailsList, int where) {
        this.context = context;
        this.itemDetailsList = itemDetailsList;
        this.where = where;


    }

    public StoreAdapter(Context context, List<ItemDetails> itemDetailsList, int where, AddSale activity) {
        this.context = context;
        this.itemDetailsList = itemDetailsList;
        this.where = where;
        this.key = activity;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View row = LayoutInflater.from(context).inflate(R.layout.single_item, parent, false);
        return new StoreAdapter.ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ItemDetails item = itemDetailsList.get(position);

        double itemPrice = item.getPrice();
        int qty = item.getStock();

        holder.sName.setText(item.getName());
        holder.sPrice.setText("Rs." + itemPrice);
        holder.sQty.setText(qty+ "");
        if(where==1){
            holder.sitemTotalCost.setVisibility(View.VISIBLE);
            holder.sitemTotalCost.setText("Total item cost = Rs."+(itemPrice*qty));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (where) {

                    case 1:
                        viewItemForStore(item);
                        break;

                    case 2:
                        viewItemForAddSale(item);
                        break;
                }


            }
        });

    }


    @Override
    public int getItemCount() {
        return itemDetailsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView sPrice, sName, sQty,sitemTotalCost;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            sPrice = itemView.findViewById(R.id.sItemPrice);
            sName = itemView.findViewById(R.id.sItemName);
            sQty = itemView.findViewById(R.id.sStock);
            sitemTotalCost = itemView.findViewById(R.id.sItemTotalCost);
        }
    }


    private void viewItemForStore(ItemDetails item) {



        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        View itemView = LayoutInflater.from(context).inflate(R.layout.single_item_view, null);
        dialog.setView(itemView);

        Notification notifiDialog = Notification.getInstance();

        TextView id, name, price, qty, title;
        EditText addStock, removeStock;
        Button update, cancle;
        ImageView delete;
        String itemID = item.getId();


        id = itemView.findViewById(R.id.vCode);
        price = itemView.findViewById(R.id.vPrice);
        name = itemView.findViewById(R.id.vName);
        qty = itemView.findViewById(R.id.vQty);
        title = itemView.findViewById(R.id.vTitle);
        addStock = itemView.findViewById(R.id.vStock);
        removeStock = itemView.findViewById(R.id.vRemoveStock);
        update = itemView.findViewById(R.id.vUpdate);
        cancle = itemView.findViewById(R.id.vClose);
        delete = itemView.findViewById(R.id.img);

        id.setText(item.getId());
        price.setText("Rs." + item.getPrice());
        name.setText(item.getName());
        qty.setText(item.getStock() + "");
        title.setText(item.getName());

        AlertDialog alertDialog = dialog.create();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(isConnected()){

                    String txtAdd = addStock.getText().toString();
                    String txtRemove = removeStock.getText().toString();


                    if (!txtAdd.isEmpty() || !txtRemove.isEmpty()) {

                        int addValue = 0, removeValue = 0;

                        try {

                            if (!txtAdd.isEmpty()) {
                                addValue = Integer.parseInt(txtAdd);
                            }
                            if (!txtRemove.isEmpty()) {
                                removeValue = Integer.parseInt(txtRemove);
                            }


                            int finalStock = item.getStock() + addValue - removeValue;

                            if ((finalStock) < 0) {

                                notifiDialog.createNotification("Out Of Stock", "your Stock Is " + item.getStock(), false, "OK",context);

                            } else {
                                ref.child(itemID).child("stock").setValue(finalStock);
                                Toast.makeText(context, "Updated Successfull", Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                            }

                        } catch (NumberFormatException e) {
                            Toast.makeText(context,"Invalid Value",Toast.LENGTH_SHORT).show();

                        }


                    }
                }


            }
        });


        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isConnected()){
                    Button btnYes = notifiDialog.createNotification("Delete Item", "Do You Want To Delete This Item?", true, "No",context);
                    btnYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dbHandler = DbHandler.getInstance(context);
                            ref.child(itemID).removeValue();
                            dbHandler.deleteItem(itemID);
                            Toast.makeText(context, "Item Deleted", Toast.LENGTH_SHORT).show();
                            notifiDialog.dialogDismiss();
                            alertDialog.dismiss();

                        }
                    });
                }


            }
        });

        alertDialog.show();
    }


    private void viewItemForAddSale(ItemDetails item) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View inputView = LayoutInflater.from(context).inflate(R.layout.single_sale_details, null);
        builder.setView(inputView);


        int itemQty = item.getStock();
        String id = item.getId();
        EditText textQty, textPrice;
        Button add, close;

        textQty = inputView.findViewById(R.id.jQty);
        textPrice = inputView.findViewById(R.id.jSell);
        add = inputView.findViewById(R.id.jAdd);
        close = inputView.findViewById(R.id.jClose);

        DbHandler db = DbHandler.getInstance(context);

        AlertDialog alertDialog = builder.create();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isConnected()){

                    String tQty = textQty.getText().toString();
                    String tPrice = textPrice.getText().toString();

                    if(!tQty.isEmpty() && !tPrice.isEmpty()){

                        try {
                            int qty = Integer.parseInt(tQty);

                            if(item.getStock()>=qty){
                                double price = Double.parseDouble(tPrice);
                                double profit = price - (item.getPrice() * qty);
                                List<String> idList = db.getIds();

                                if (idList.contains(id)) {

                                    List<SoldItemDetails> soldItem = db.getItems();
                                    SoldItemDetails itemDetails = soldItem.get(idList.indexOf(id));

                                    price += itemDetails.getSellPrice();
                                    profit += itemDetails.getProfit();
                                    int newQty = qty + itemDetails.getQty();

                                    SoldItemDetails soldItemDetails = new SoldItemDetails(id, item.getName(), newQty, item.getPrice(), price, profit);
                                    db.updateItem(soldItemDetails);
                                } else {
                                    SoldItemDetails soldItemDetails = new SoldItemDetails(id, item.getName(), qty, item.getPrice(), price, profit);
                                    db.addItem(soldItemDetails);

                                }
                                key.showSoldItem();
                                ref.child(item.getId()).child("stock").setValue(itemQty - qty);
                                alertDialog.dismiss();
                            }else {
                                Toast.makeText(context,"Out Of Stock",Toast.LENGTH_SHORT).show();
                            }

                        }catch (NumberFormatException e){
                            Toast.makeText(context,"Invalid Value",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(context,"Please Add Value",Toast.LENGTH_SHORT).show();
                    }
                }




            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }


    public void yes() {

    }

    public boolean isConnected(){

        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            return true;
        }
        else{

            new androidx.appcompat.app.AlertDialog.Builder(context)
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


