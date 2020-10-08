package com.technologyend.firebaseexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class Covid19Data extends AppCompatActivity {

    private DatabaseReference mCovidDatabaseReference, mUsersDatabaseReference;
    private TextView disName;
    public TextView disNameTV, confirmedTV, recoveredTV, activeTV, deathsTV;
    private Button btnGet;
    private districtAdapter mDistrictAdapter;
    private ListView diswiseRV;
    private ArrayList<districtWiseClass> alldata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid19_data);
        setTitle("Haryana");
        diswiseRV = findViewById(R.id.rvdis);
        List<districtWiseClass> myarr = new ArrayList<>();
        districtAdapter myadapter = new districtAdapter(Covid19Data.this, R.layout.district_wise_items, myarr);
        diswiseRV.setAdapter(myadapter);
        mCovidDatabaseReference = FirebaseDatabase.getInstance().getReference("covid19Data/Haryana");

                Toast.makeText(Covid19Data.this, "Data Coming", Toast.LENGTH_LONG).show();
                String covidapiurl = "https://api.covid19india.org/state_district_wise.json";
                RequestQueue requestQueue = Volley.newRequestQueue(Covid19Data.this);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, covidapiurl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject haryana = response.getJSONObject("Haryana");
                            JSONObject districtData = haryana.getJSONObject("districtData");
                            JSONArray districtinHR = districtData.names();

                            for(int i = 0; i < districtinHR.length(); i++){
                                String dName = districtinHR.getString(i);
                                int confirmed = (int) districtData.getJSONObject(dName).get("confirmed");
                                int recovered = (int) districtData.getJSONObject(dName).get("recovered");
                                int active = (int) districtData.getJSONObject(dName).get("active");
                                int dead = (int) districtData.getJSONObject(dName).get("deceased");
                                districtWiseClass disobj = new districtWiseClass(dName, confirmed +"", recovered +"", active +"", dead +"");
                                myadapter.add(disobj);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(Covid19Data.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }




                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Covid19Data.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

                requestQueue.add(jsonObjectRequest);

/***
        txtHR = findViewById(R.id.txtHR);
        btnGet = findViewById(R.id.btnGet);

        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String covidapiurl = "https://api.covid19india.org/state_district_wise.json";
                RequestQueue requestQueue = Volley.newRequestQueue(Covid19Data.this);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, covidapiurl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject haryana = response.getJSONObject("Haryana");
                            JSONObject districtData = haryana.getJSONObject("districtData");
                            JSONArray districtinHR = districtData.names();

                            for(int i = 0; i < districtinHR.length() ; i++){
                                String dName = districtinHR.getString(i);
                                int activeinRohtak = (int) districtData.getJSONObject(dName).get("active");
                                txtHR.setText(txtHR.getText() + dName + " - " +activeinRohtak+ "\n");
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(Covid19Data.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Covid19Data.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

                requestQueue.add(jsonObjectRequest);
            }
            });
*/


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.covid_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.dev_credits){
            Intent intent = new Intent(Covid19Data.this, JatinSinghroha.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}
