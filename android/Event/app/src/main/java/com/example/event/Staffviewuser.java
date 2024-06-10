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
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class Staffviewuser extends AppCompatActivity implements JsonResponse, AdapterView.OnItemClickListener {
    ListView l1;
    SharedPreferences sh;
    String[] name,lname,place,phone,email,address,password,value,login_id;
    public static String amt,lid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staffviewuser);
        l1=(ListView) findViewById(R.id.list);
        l1.setOnItemClickListener(this);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        JsonReq JR = new JsonReq();
        JR.json_response = (JsonResponse) Staffviewuser.this;
        String q = "/Staffviewuser";
        q = q.replace(" ", "%20");
        JR.execute(q);
    }

    @Override
    public void response(JSONObject jo) {
        try {

            String status = jo.getString("status");
            Log.d("pearl", status);


            if (status.equalsIgnoreCase("success")) {
                JSONArray ja1 = (JSONArray) jo.getJSONArray("data");
                name = new String[ja1.length()];
                lname = new String[ja1.length()];

                phone = new String[ja1.length()];
                place = new String[ja1.length()];
                email = new String[ja1.length()];
                address= new String[ja1.length()];
                value = new String[ja1.length()];
                login_id = new String[ja1.length()];



                String[] value = new String[ja1.length()];

                for (int i = 0; i < ja1.length(); i++) {
                    name[i] = ja1.getJSONObject(i).getString("fname");

                    lname[i] = ja1.getJSONObject(i).getString("lname");
                    place[i] = ja1.getJSONObject(i).getString("place");

                    phone[i] = ja1.getJSONObject(i).getString("phone");
                    email[i] = ja1.getJSONObject(i).getString("email");

                    address[i] = ja1.getJSONObject(i).getString("address");
                    login_id[i] = ja1.getJSONObject(i).getString("login_id");



                    value[i] = "first name:" + name[i] + "\nlast name: " + lname[i]+"\nplace:" + place[i] + "\nphone: " + phone[i] +"\nemail:" + email[i] + "\naddress: " + address[i]  ;

                }
                ArrayAdapter<String> ar = new ArrayAdapter<String>(getApplicationContext(), R.layout.custtext, value);

                l1.setAdapter(ar);

            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        lid=login_id[i];
        final CharSequence[] items = {"Accept","Reject","Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Staffviewuser.this);
        // builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Accept")) {
                    JsonReq JR = new JsonReq();
                    JR.json_response = (JsonResponse) Staffviewuser.this;
                    String q = "/accept?lid="+lid;
                    q = q.replace(" ", "%20");
                    JR.execute(q);

                }
                else if (items[item].equals("Reject")) {
                    JsonReq JR = new JsonReq();
                    JR.json_response = (JsonResponse) Staffviewuser.this;
                    String q = "/reject?lid="+lid;
                    q = q.replace(" ", "%20");
                    JR.execute(q);
                }
                else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }

            }

        });
        builder.show();
    }
    }
