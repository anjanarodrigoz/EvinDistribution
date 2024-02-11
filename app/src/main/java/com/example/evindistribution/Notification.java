package com.example.evindistribution;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;


public class Notification {

    private static Notification mInstance = null;
    private static AlertDialog dialog;


    private Notification() {


    }

    public static synchronized Notification getInstance() {


        if (mInstance == null) {
            mInstance = new Notification();
        }
        return mInstance;
    }

    public Button createNotification(String title, String des, boolean show, String text,Context context) {


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.single_notification, null, false);
        builder.setView(view);


        TextView mtitle, mdes;
        Button btnClose, btnYes;

        mtitle = view.findViewById(R.id.qTitle);
        mdes = view.findViewById(R.id.qdescription);
        btnClose = view.findViewById(R.id.qClose);
        btnYes = view.findViewById(R.id.qYes);


        mtitle.setText(title);
        mdes.setText(des);
        btnClose.setText(text);
        if (!show) {
            btnYes.setVisibility(View.INVISIBLE);
        }

        AlertDialog dialog = builder.create();
        this.dialog = dialog;





        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });


        dialog.show();




        return btnYes;

    }

    public void dialogDismiss(){
        if(dialog!=null){
            dialog.dismiss();
        }

    }


}
