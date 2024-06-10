package com.example.event;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class userViewbookings extends AppCompatActivity implements JsonResponse, AdapterView.OnItemClickListener {
    EditText e1;
    ListView l1;
    String[] event,fname,date,statu,cbooking_id,value,amount,event_id;
    SharedPreferences sh;
    String search,status;
    public static  String bid,eid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_viewbookings);
        e1 = (EditText) findViewById(R.id.search);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        l1 = (ListView) findViewById(R.id.list);
        l1.setOnItemClickListener(this);
        JsonReq JR = new JsonReq();
        JR.json_response = (JsonResponse) userViewbookings.this;
        String q = "/userViewbookings?log_id=" +sh.getString("log_id", "");
        q = q.replace(" ", "%20");
        JR.execute(q);
    }

    @Override
    public void response(JSONObject jo) {
        try {


            status = jo.getString("status");
            Log.d("pearlssssss", status);


            if (status.equalsIgnoreCase("success")) {
                l1.setVisibility(View.VISIBLE);
                JSONArray ja1 = (JSONArray) jo.getJSONArray("data");
                event = new String[ja1.length()];
                fname=new String[ja1.length()];
                date = new String[ja1.length()];
                cbooking_id=new String[ja1.length()];

                statu=new String[ja1.length()];
                amount=new String[ja1.length()];
                event_id=new String[ja1.length()];





                value = new String[ja1.length()];

                for (int i = 0; i < ja1.length(); i++) {
                    event[i] = ja1.getJSONObject(i).getString("event");
                    fname[i] = ja1.getJSONObject(i).getString("fname");
                    date[i] = ja1.getJSONObject(i).getString("date");
                    cbooking_id[i] = ja1.getJSONObject(i).getString("booking_id");
                    event_id[i]= ja1.getJSONObject(i).getString("event_id");
                    statu[i]=ja1.getJSONObject(i).getString("status");

                    amount[i]=ja1.getJSONObject(i).getString("amount");
                    value[i] = "event:" + event[i]+"\n name:" + fname[i]+"\n date:" + date[i] +"\n status:" + statu[i] +"\n amount:" + amount[i]  ;

                }
                ArrayAdapter<String> ar = new ArrayAdapter<String>(getApplicationContext(), R.layout.custtext, value);

                l1.setAdapter(ar);




            }
            else{
                Toast.makeText(getApplicationContext(),"No Data",Toast.LENGTH_LONG).show();
                l1.setVisibility(View.GONE);
            }




        }

        catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
eid=event_id[i];
        bid=cbooking_id[i];
        final CharSequence[] items = {"certificate","Qrcode","Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(userViewbookings.this);
        // builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("certificate")) {

                    startActivity(new Intent(getApplicationContext(), certificate.class));

                }

              else if (items[item].equals("Qrcode")) {
                    startActivity(new Intent(getApplicationContext(),View_my_qr.class));

                }
                else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }

            }

        });
        builder.show();
    }
    }
