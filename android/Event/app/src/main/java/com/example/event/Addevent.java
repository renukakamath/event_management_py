package com.example.event;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Calendar;

public class Addevent extends AppCompatActivity implements JsonResponse {
    EditText e1,e2,e3,e4,e7,e8,e9,e10,e11,e12;
    RadioButton r1,r2;
    Button b1;
    String date,even,details;


    private int mYear, mMonth, mDay, mHour, mMinute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addevent);
        e1 = (EditText) findViewById(R.id.dob);
        e2 = (EditText) findViewById(R.id.event);
        e3 = (EditText) findViewById(R.id.details);



        b1 = (Button) findViewById(R.id.button);



        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(calendar.YEAR);
        final int month = calendar.get(calendar.MONTH);
        final int day = calendar.get(calendar.DAY_OF_MONTH);


        e1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog dialog = new DatePickerDialog(Addevent.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        e1.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
                dialog.show();
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date=e1.getText().toString();
                even=e2.getText().toString();
                details=e3.getText().toString();

                JsonReq JR = new JsonReq();
                JR.json_response = (JsonResponse) Addevent.this;
                String q ="/Addevent?&login_id="+Login.logid+"&date="+date+"&event="+even+"&details="+details;
                q = q.replace(" ","%20");
                JR.execute(q);
            }
        });




    }

    @Override
    public void response(JSONObject jo) {
        try {
            Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_LONG).show();
            String status = jo.getString("status");
            Log.d("pearl", status);


            if (status.equalsIgnoreCase("success")) {
                Toast.makeText(getApplicationContext(), " SUCCESS", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), Userhome.class));

            } else {
                startActivity(new Intent(getApplicationContext(), Addevent.class));

                Toast.makeText(getApplicationContext(), " failed.TRY AGAIN!!", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}