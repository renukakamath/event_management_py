package com.example.event;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class Updateprofile extends AppCompatActivity implements JsonResponse {
    EditText e1,e2,e3,e4,e5,e6;
    Button b1;
    String name,place,phone,email,licence,password;
    SharedPreferences sh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateprofile);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        e1=(EditText) findViewById(R.id.fname);
        e2=(EditText) findViewById(R.id.place);
        e3=(EditText) findViewById(R.id.phone);
        e4=(EditText) findViewById(R.id.email);
        e5=(EditText)findViewById(R.id.licencedetails) ;
        b1=(Button) findViewById(R.id.update);
        e6=(EditText)findViewById(R.id.password);
        JsonReq JR = new JsonReq();

        JR.json_response = (JsonResponse) Updateprofile.this;
        String q = "/viewusers?lid="+sh.getString("log_id", "");
        q = q.replace(" ", "%20");
        JR.execute(q);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name=e1.getText().toString();
                place=e2.getText().toString();
                phone=e3.getText().toString();
                email=e4.getText().toString();
                licence=e5.getText().toString();
                password=e6.getText().toString();


                JsonReq JR=new JsonReq();
                JR.json_response=(JsonResponse) Updateprofile.this;
                String q = "/updateuser?login_id="+sh.getString("log_id", "")+"&name="+name+"&place="+place+"&Phone="+phone+ "&email="+email +"&licence="+licence+"&password="+password;
                q=q.replace(" ","%20");
                JR.execute(q);
            }
        });

    }

    @Override
    public void response(JSONObject jo) {
        try {
            String method=jo.getString("method");

            if(method.equalsIgnoreCase("viewusers")) {
                String status = jo.getString("status");
                Log.d("pearl", status);

                if (status.equalsIgnoreCase("success")) {
                    JSONArray ja1 = (JSONArray) jo.getJSONArray("data");
                    e1.setText(ja1.getJSONObject(0).getString("fname"));
                    e2.setText(ja1.getJSONObject(0).getString("place"));
                    e3.setText(ja1.getJSONObject(0).getString("phone"));
                    e4.setText(ja1.getJSONObject(0).getString("email"));
                    e5.setText(ja1.getJSONObject(0).getString("address"));
                    e6.setText(ja1.getJSONObject(0).getString("password"));
                    
                    SharedPreferences.Editor e = sh.edit();
                    //e.putString("log_id", logid);
                    e.commit();
                }
            }
            else if(method.equalsIgnoreCase("updateuser"))
            {
                try {
                    String status=jo.getString("status");
                    Log.d("pearl",status);


                    if(status.equalsIgnoreCase("success")){

                        Toast.makeText(getApplicationContext(), "UPDATED SUCCESSFULLY", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(),Updateprofile.class));

                    }
                    else
                    {
                        startActivity(new Intent(getApplicationContext(),Updateprofile.class));
                        Toast.makeText(getApplicationContext(), " failed.TRY AGAIN!!", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }

            }
//            else {
//                Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_LONG).show();
//                startActivity(new Intent(getApplicationContext(), Login.class));
//            }
        } catch (Exception e) {
            // TODO: handle exception

            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}