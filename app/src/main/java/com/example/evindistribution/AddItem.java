package com.example.evindistribution;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.evindistribution.Models.ItemDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AddItem extends AppCompatActivity {

    EditText textName,textPrice,textQty;
    TextView textID;
    Button btnAdd;
    Context context = this;
    int itemId;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        textID = findViewById(R.id.textID);
        textName = findViewById(R.id.textName);
        textPrice = findViewById(R.id.textPrice);
        textQty = findViewById(R.id.textQuantity);
        btnAdd = findViewById(R.id.btnAdd);
        back = findViewById(R.id.tImage);

        generateId();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isConnected()){
                    String name = textName.getText().toString();
                    String id = textID.getText().toString();

                    try {
                        double price = Double.parseDouble(textPrice.getText().toString());
                        int qty = Integer.parseInt(textQty.getText().toString());

                        if(name.isEmpty()){
                            Toast.makeText(context,"Add Item Name",Toast.LENGTH_SHORT);
                        }
                        else {
                            ItemDetails itemDetails = new ItemDetails(id,name,price,qty);

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                            reference.child("ITEMS").child(id).setValue(itemDetails);
                            reference.child("ID").setValue(itemId);
                            Toast.makeText(context,"Item Added Successfull",Toast.LENGTH_SHORT).show();
                            generateId();

                            textName.setText("");
                            textPrice.setText("");
                            textQty.setText("");
                        }


                    }catch (NumberFormatException e){
                        Notification notifiDialog = Notification.getInstance();
                        notifiDialog.createNotification("Error","Please Input Valid Data",false,"OK",context);
                    }
                }



            }
        });
    }

    private void generateId() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query query = ref.child("ID");


        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(!snapshot.exists()){
                    itemId = 0;
                    ref.child("ID").setValue(itemId);
                    textID.setText("ED"+String.format("%03d",itemId));
                }
                else{
                    itemId = snapshot.getValue(Integer.class);
                    itemId+=1;
                    textID.setText("ED"+String.format("%03d",itemId));
                }

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