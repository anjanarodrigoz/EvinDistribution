package com.example.evindistribution;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import static android.os.Build.VERSION.SDK_INT;


public class MainActivity extends AppCompatActivity {

    CardView store, addItem, addSale, sales;
    Context context = this;
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        store = findViewById(R.id.cardStore);
        addItem = findViewById(R.id.cardAddItem);
        addSale = findViewById(R.id.cardAddSale);
        sales = findViewById(R.id.cardSales);

        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE) +
                ContextCompat.checkSelfPermission(context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) +
                ContextCompat.checkSelfPermission(context,
                        Manifest.permission.MANAGE_EXTERNAL_STORAGE) +
                ContextCompat.checkSelfPermission(context,
                        Manifest.permission.SEND_SMS) !=
                PackageManager.PERMISSION_GRANTED)

            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                            Manifest.permission.MANAGE_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                            Manifest.permission.SEND_SMS)) {

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(
                        context
                );
                builder.setTitle("Grant those Permission");
                builder.setMessage("Read Strorage");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(
                                (Activity) context,
                                new String[]{
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                                        Manifest.permission.SEND_SMS
                                }, 123
                        );
                    }
                });
                builder.setNegativeButton("Cancel", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else {
                ActivityCompat.requestPermissions(
                        (Activity) context,
                        new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                                Manifest.permission.SEND_SMS
                        }, 123
                );

            }

        if (SDK_INT >= Build.VERSION_CODES.R) {

            if (!Environment.isExternalStorageManager()) {

                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", this.getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        }

        Intent storeActivity = new Intent(context, Store.class);
        Intent addItemActivity = new Intent(context, AddItem.class);
        Intent addSaleActivity = new Intent(context, AddSale.class);
        Intent salesActivity = new Intent(context, Sales.class);


        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(storeActivity);
            }
        });

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(addItemActivity);
            }
        });

        addSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(addSaleActivity);
            }
        });

        sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(salesActivity);
            }
        });

    }
}