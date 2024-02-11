package com.example.evindistribution;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.evindistribution.Adapter.SaleAdapter;
import com.example.evindistribution.Models.SaleDetails;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Sales extends AppCompatActivity {

    ImageView back;
    EditText search;
    RecyclerView saleRecycler;
    Context context = this;
    DatabaseReference reference;
    SaleAdapter saleAdapter;
    List<SaleDetails> saleDetailsList;
    FloatingActionButton saleReport;

    Notification dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);


        back = findViewById(R.id.hImage);
        search = findViewById(R.id.hSearch);
        saleRecycler = findViewById(R.id.hRecycler);
        saleReport = findViewById(R.id.saleReport);
        dialog = Notification.getInstance();

        reference = FirebaseDatabase.getInstance().getReference().child("SALE");
        saleDetailsList = new ArrayList<>();

        saleRecycler.setHasFixedSize(true);
        saleRecycler.setLayoutManager(new LinearLayoutManager(context));

        System.out.println("Hello");

        readSales();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String search = s.toString();
                Query query = reference.orderByChild("saleId").startAt(search).endAt(search + "\uf8ff");
                searchSale(query);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        saleReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!saleDetailsList.isEmpty()) {
                    Button yes = dialog.createNotification("Create Sheet", "Do you want to create sale report", true, "NO", context);

                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            createSaleReportSheet();

                        }
                    });
                }

            }
        });
    }


    private void searchSale(Query query) {

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                saleDetailsList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    SaleDetails saleDetails = dataSnapshot.getValue(SaleDetails.class);
                    saleDetailsList.add(saleDetails);


                }

                saleAdapter = new SaleAdapter(context, saleDetailsList);
                saleRecycler.setAdapter(saleAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void readSales() {

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                saleDetailsList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    SaleDetails saleDetails = dataSnapshot.getValue(SaleDetails.class);
                    saleDetailsList.add(saleDetails);


                }

                saleAdapter = new SaleAdapter(context, saleDetailsList);
                saleRecycler.setAdapter(saleAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void createSaleReportSheet() {

        dialog.dialogDismiss();
        String[] header = {"Date", "Item Id", "Item Name ", "Quantity", "Item Price", "Cost", "Sell Price", "Profit"};
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("EVIN DISTRIBUTERS SALE SHEET"); //Creating a sheet


        // Create a Row
        Row titleRow = sheet.createRow(0);
        Row dateRow = sheet.createRow(1);

        String txt = search.getText().toString();
        String fileDefultName = !txt.isEmpty() ? txt.replaceAll("/",".") : "defult";

        titleRow.createCell(0).setCellValue("EVIN DISTRIBUTERS");
        dateRow.createCell(0).setCellValue(fileDefultName);

        Row headerRow = sheet.createRow(4);

        for (int i = 0; i < header.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(header[i]);

        }

        int rowNum = 6;

        double totalCost = 0, totalPrice = 0, totalProfit = 0;


        for (int i = 0; i < saleDetailsList.size(); i++) {

            SaleDetails sale = saleDetailsList.get(i);


            Row row = sheet.createRow(rowNum);
            row.createCell(0).setCellValue(sale.getSaleId());

            for (int k = 0; k < sale.getItemId().size(); k++) {

                double price = sale.getItemPrice().get(k);
                int qty = sale.getQuantity().get(k);
                double cost = price * qty;
                double salePrice = sale.getSalePrice().get(k);
                double profit = salePrice - cost;


                row.createCell(1).setCellValue(sale.getItemId().get(k));
                row.createCell(2).setCellValue(sale.getItemName().get(k));
                row.createCell(3).setCellValue(qty);
                row.createCell(4).setCellValue(price);
                row.createCell(5).setCellValue(price * qty);
                row.createCell(6).setCellValue(salePrice);
                row.createCell(7).setCellValue(profit);

                totalCost += cost;
                totalPrice += salePrice;
                totalProfit += profit;

                rowNum += 1;
                row = sheet.createRow(rowNum);
            }

            rowNum = rowNum + 1;

        }

        Row bottomRow = sheet.createRow(rowNum + 2);
        bottomRow.createCell(1).setCellValue("TOTAL");
        bottomRow.createCell(5).setCellValue(totalCost);
        bottomRow.createCell(6).setCellValue(totalPrice);
        bottomRow.createCell(7).setCellValue(totalProfit);

        String fileName = fileDefultName + ".xlsx"; //Name of the file

        String extStorageDirectory = Environment.getExternalStorageDirectory()
                .toString() + "/EDReports";


        File folder = new File(extStorageDirectory);// Name of the folder you want to keep your file in the local storage.

        folder.mkdir(); //creating the folder

        File file = new File(folder, fileName);

        try {
            folder.createNewFile(); // creating the file inside the folder

        } catch (IOException e1) {
            dialog.createNotification("Error",  "Erooorr", false, "OK", context);
        }

        try {
            FileOutputStream fileOut = new FileOutputStream(file); //Opening the file
            workbook.write(fileOut); //Writing all your row column inside the file
            fileOut.close(); //closing the file and done
            dialog.createNotification("Completed", "Sale report created", false, "OK", context);


        } catch (Exception e) {
            dialog.createNotification("Error",  "Erooorr", false, "OK", context);

        }


    }
}