package com.example.event;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class staffviewevent extends AppCompatActivity implements JsonResponse, AdapterView.OnItemClickListener {
    EditText e1;
    ListView l1;
    String[] event,eventtype,name,venue,amount,addressdetails,uploadeddate,eventdate,details,statu,value,event_id;
    String search,status;
    public static  String pid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staffviewevent);
        e1=(EditText)findViewById(R.id.search);
        l1=(ListView) findViewById(R.id.list);
        l1.setOnItemClickListener(this);
        JsonReq JR = new JsonReq();
        JR.json_response = (JsonResponse) staffviewevent.this;
        String q = "/sviewevent";
        q = q.replace(" ", "%20");
        JR.execute(q);

        e1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                search=e1.getText().toString();

                JsonReq JR = new JsonReq();
                JR.json_response = (JsonResponse) staffviewevent.this;
                String q = "/ssearchevent?&search=" + search ;
                q = q.replace(" ", "%20");
                JR.execute(q);

            }
        });
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
                eventtype=new String[ja1.length()];
                name = new String[ja1.length()];
                venue=new String[ja1.length()];
                amount = new String[ja1.length()];
                addressdetails=new String[ja1.length()];
                uploadeddate = new String[ja1.length()];
                eventdate=new String[ja1.length()];
                details = new String[ja1.length()];
                statu=new String[ja1.length()];




                value = new String[ja1.length()];


                value = new String[ja1.length()];

                for (int i = 0; i < ja1.length(); i++) {
                    event[i] = ja1.getJSONObject(i).getString("event");
                    eventtype[i] = ja1.getJSONObject(i).getString("event_type");
                    name[i] = ja1.getJSONObject(i).getString("name");
                    venue[i] = ja1.getJSONObject(i).getString("venue");
                    amount[i] = ja1.getJSONObject(i).getString("amount");

                    addressdetails[i] = ja1.getJSONObject(i).getString("address");
                    uploadeddate[i] = ja1.getJSONObject(i).getString("upload_date");
                    eventdate[i] = ja1.getJSONObject(i).getString("event_date");
                    details[i] = ja1.getJSONObject(i).getString("detail");
                    statu[i]=ja1.getJSONObject(i).getString("status");
                    value[i] = "event:" + event[i]+"\n eventtype:" + eventtype[i]+"\n name:" + name[i] +"\n venue:" + venue[i] +"\n amount:" + amount[i] +"\n addressdetails:" + addressdetails[i] +"\n uploadeddate:" + uploadeddate[i] +"\n eventdate:" + eventdate[i]+"\n details:" + details[i] +"\n status:" + statu[i]   ;

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
        final CharSequence[] items = {"view Book","Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(staffviewevent.this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("view Book")) {


                    startActivity(new Intent(getApplicationContext(), staffViewbookings.class));


                }


                else if (items[item].equals("Cancel")) {


                    dialog.dismiss();
                }



            }

        });
        builder.show();
    }
    }
