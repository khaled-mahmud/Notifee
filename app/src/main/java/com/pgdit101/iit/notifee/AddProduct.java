package com.pgdit101.iit.notifee;



import android.content.Intent;
import android.database.Cursor;

import android.support.v4.app.FragmentManager;
//import android.support.v7.app.ActionBarActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

//import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class AddProduct extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    String upc_code;
    String auth_key = "Ca84J5r0q3Ec1Io1";
    String app_key = "/4bliPUjRivm";
    String signature;
    String result;
    String productTitle;
    //public static TextView textViewJson;
    EditText product_name;

    ProductDBHelper myDB;
    DatePickerDialog dpd;
    int startYear = 0, startMonth = 0, startDay = 0;
    String dateFinal,descriptionFinal, nameFinal, id;
    Intent intent;
    Boolean isUpdate;
    Boolean isBarCodeScan;
    ImageView deleteProduct;
    //FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        product_name = findViewById(R.id.product_name);
        EditText product_description = findViewById(R.id.description);
        //textViewJson = findViewById(R.id.textView2);
        //ImageView imageView = findViewById(R.id.imageView);

        myDB = new ProductDBHelper(getApplicationContext());
        intent = getIntent();
        isUpdate = intent.getBooleanExtra("isUpdate", false);
        isBarCodeScan = intent.getBooleanExtra("isBarCodeScan", false);
        deleteProduct = (ImageView) findViewById(R.id.deleteProduct);
        deleteProduct.setVisibility(View.INVISIBLE);

        dateFinal = todayDateString();
        Date your_date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(your_date);
        startYear = cal.get(Calendar.YEAR);
        startMonth = cal.get(Calendar.MONTH);
        startDay = cal.get(Calendar.DAY_OF_MONTH);

        if(isUpdate){
            init_update();
            deleteProduct.setVisibility(View.VISIBLE);
        }


        if(isBarCodeScan){
            barCodeScan();
            //deleteProduct.setVisibility(View.VISIBLE);
        }


        /*upc_code = getIntent().getExtras().getString("upc_code");
        // $signature = base64_encode(hash_hmac('sha1', $upc_code, $auth_key, $raw_output = true));
        try {
            signature = hash_hmac(upc_code, auth_key);
        } catch (Exception e) {
            Log.wtf("HMAC:","stop");
            //textView.setText("Error!");
            e.printStackTrace();
        }


        // $url = 'https://digit-eyes.com/gtin/v2_0/?upc_code='. $upc_code .'&app_key='. $app_key .'&language=en&field_names=all&signature='. $signature .'';
        String url = "https://digit-eyes.com/gtin/v2_0/?upc_code=" + upc_code + "&app_key=" + app_key + "&language=en&field_names=all&signature=" + signature + "";
        //textView.setText(signature);

        //String description;
        //String imagelink;

        // IMplementing AsyncTask
        HttpGetRequest getRequest = new HttpGetRequest();
        //getRequest.execute(url);
        try {
            result = getRequest.execute(url).get();

            JSONObject jsonObject = new JSONObject(result);
            Toast.makeText(getApplicationContext(), jsonObject.toString(), Toast.LENGTH_LONG).show();
            //JSONObject objA = jsonObject.getJSONObject("gcp");
            productTitle = jsonObject.getString("description");
            product_name.setText(productTitle, TextView.BufferType.EDITABLE);

            //Toast.makeText(getApplicationContext(), jsonObject.toString(), Toast.LENGTH_LONG).show();

            /*JSONArray jsonArray = new JSONArray(result);
            textView2.setText(jsonArray.toString());
            /*for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                //imageView.setImageURI((Uri) JO.getString("image"));
                //textView2.setText((String) JO.get("description"));

                description = jsonObject.getString("description");
                //imagelink = jsonObject.getString("image");
                textView2.setText(description);
                //imageView.setImageURI((Uri) imagelink);
                //Picasso.get().load(imagelink).into(imageView);
            }*/
            //JSONObject JO = new JSONObject();
            //textView2.setText(jsonArray.toString());

        /*} catch (InterruptedException e) {
            e.printStackTrace();
            product_name.setText("Interrupted Exception");
        } catch (ExecutionException e) {
            e.printStackTrace();
            product_name.setText("ExecutionException Exception");
        } catch (JSONException e) {
            e.printStackTrace();
            product_name.setText("Enter Product Name");
        }*/


    }




    private String hash_hmac(String upc_code, String auth_key) throws Exception{

        Mac sha1_HMAC = Mac.getInstance("HmacSHA1");
        //byte[] string = upc_code.getBytes();
        //String stringInBase64 = Base64.encodeToString(string, Base64.DEFAULT);
        SecretKeySpec secretKey = new SecretKeySpec(auth_key.getBytes(), "HmacSHA1");
        sha1_HMAC.init(secretKey);
        String hash = Base64.encodeToString(sha1_HMAC.doFinal(upc_code.getBytes()), Base64.DEFAULT);
        return hash;

    }


    public void init_update()
    {
        id = intent.getStringExtra("id");
        TextView toolbar_product_add_title = findViewById(R.id.toolbar_product_add_title);
        EditText product_name = findViewById(R.id.product_name);
        EditText product_description = findViewById(R.id.description);
        EditText product_date = findViewById(R.id.date);
        toolbar_product_add_title.setText("Update");
        Cursor productExpiry = myDB.getDataSpecific(id);
        if(productExpiry != null){
            productExpiry.moveToFirst();

            product_name.setText(productExpiry.getString(1).toString());
            product_description.setText(productExpiry.getString(2).toString());
            Calendar cal = Function.Epoch2Calendar(productExpiry.getString(3).toString());
            startYear = cal.get(Calendar.YEAR);
            startMonth = cal.get(Calendar.MONTH);
            startDay = cal.get(Calendar.DAY_OF_MONTH);
            product_date.setText(Function.Epoch2DateString(productExpiry.getString(3).toString(), "dd/MM/yyyy"));
        }
    }



    public String todayDateString()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.toString();
    }


    public void closeAddProduct(View view)
    {
        finish();
    }



    public void doneAddProduct(View v)
    {
        int errorStep = 0;
        EditText product_name = findViewById(R.id.product_name);
        EditText product_description = findViewById(R.id.description);
        EditText date = findViewById(R.id.date);
        nameFinal = product_name.getText().toString();
        descriptionFinal = product_description.getText().toString();
        dateFinal = date.getText().toString();


        /* Checking */
        if (nameFinal.trim().length() < 1) {
            errorStep++;
            product_name.setError("Provide a product name.");
        }

        if (descriptionFinal.trim().length() < 1) {
            errorStep++;
            product_description.setError("Provide a product description.");
        }

        if (dateFinal.trim().length() < 4) {
            errorStep++;
            date.setError("Provide a specific date");
        }



        if (errorStep == 0) {
            if (isUpdate) {
                myDB.updateProduct(id, nameFinal, descriptionFinal, dateFinal);
                Toast.makeText(getApplicationContext(), "Product Updated.", Toast.LENGTH_SHORT).show();
            } else {
                myDB.insertProduct(nameFinal, descriptionFinal, dateFinal);
                Toast.makeText(getApplicationContext(), "Product Added.", Toast.LENGTH_SHORT).show();
            }
            startActivity(new Intent(getApplicationContext(),ListViewActivity.class));
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Try again", Toast.LENGTH_SHORT).show();
        }

    }



    public void deleteProduct(View v)
    {
        myDB.deleteProduct(id);
        Toast.makeText(getApplicationContext(), "Product Deleted.", Toast.LENGTH_SHORT).show();
        finish();
    }



    @Override
    public void onResume()
    {
        super.onResume();
        dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("startDatepickerdialog");
        if(dpd != null) dpd.setOnDateSetListener(this);
        //dpd = (DatePickerDialog) getSupportFragmentManager().findFragmentByTag("startDatepickerdialog");

    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        startYear = year;
        startMonth = monthOfYear;
        startDay = dayOfMonth;
        int monthAddOne = startMonth + 1;
        String date = (startDay < 10 ? "0" + startDay : "" + startDay) + "/" +
                (monthAddOne < 10 ? "0" + monthAddOne : "" + monthAddOne) + "/" +
                startYear;
        EditText product_date = findViewById(R.id.date);
        product_date.setText(date);
    }


    public void showStartDatePicker(View v)
    {
        dpd = DatePickerDialog.newInstance(AddProduct.this, startYear, startMonth, startDay);
        dpd.setOnDateSetListener(this);
        dpd.show(getFragmentManager(), "startDatepickerdialog");
        //dpd.show(getSupportFragmentManager(), "asd");
        //dpd.show(getSupportFragmentManager(), "startDatepickerdialog");

    }




    public void barCodeScan(){

        //id = intent.getStringExtra("id");
        /*TextView toolbar_product_add_title = findViewById(R.id.toolbar_product_add_title);
        EditText product_name = findViewById(R.id.product_name);
        EditText product_description = findViewById(R.id.description);
        EditText product_date = findViewById(R.id.date);
        toolbar_product_add_title.setText("Update");
        Cursor productExpiry = myDB.getDataSpecific(id);
        if(productExpiry != null){
            productExpiry.moveToFirst();

            product_name.setText(productExpiry.getString(1).toString());
            product_description.setText(productExpiry.getString(2).toString());
            Calendar cal = Function.Epoch2Calendar(productExpiry.getString(3).toString());
            startYear = cal.get(Calendar.YEAR);
            startMonth = cal.get(Calendar.MONTH);
            startDay = cal.get(Calendar.DAY_OF_MONTH);
            product_date.setText(Function.Epoch2DateString(productExpiry.getString(3).toString(), "dd/MM/yyyy"));
        }*/

        //EditText product_name = findViewById(R.id.product_name);

        upc_code = getIntent().getExtras().getString("upc_code");
        // $signature = base64_encode(hash_hmac('sha1', $upc_code, $auth_key, $raw_output = true));
        try {
            signature = hash_hmac(upc_code, auth_key);
        } catch (Exception e) {
            Log.wtf("HMAC:","stop");
            //textView.setText("Error!");
            e.printStackTrace();
        }


        // $url = 'https://digit-eyes.com/gtin/v2_0/?upc_code='. $upc_code .'&app_key='. $app_key .'&language=en&field_names=all&signature='. $signature .'';
        String url = "https://digit-eyes.com/gtin/v2_0/?upc_code=" + upc_code + "&app_key=" + app_key + "&language=en&field_names=all&signature=" + signature + "";
        //textView.setText(signature);
        apiRequest(url);
        //String description;
        //String imagelink;

        // IMplementing AsyncTask
        //HttpGetRequest getRequest = new HttpGetRequest();
        //getRequest.execute(url);
        /*try {
            result = getRequest.execute(url).get();

            JSONObject jsonObject = new JSONObject(result);
            Toast.makeText(getApplicationContext(), jsonObject.toString(), Toast.LENGTH_LONG).show();
            //JSONObject objA = jsonObject.getJSONObject("gcp");
            productTitle = jsonObject.getString("description");
            product_name.setText(productTitle, TextView.BufferType.EDITABLE);

            //Toast.makeText(getApplicationContext(), jsonObject.toString(), Toast.LENGTH_LONG).show();

            /*JSONArray jsonArray = new JSONArray(result);
            textView2.setText(jsonArray.toString());
            /*for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                //imageView.setImageURI((Uri) JO.getString("image"));
                //textView2.setText((String) JO.get("description"));

                description = jsonObject.getString("description");
                //imagelink = jsonObject.getString("image");
                textView2.setText(description);
                //imageView.setImageURI((Uri) imagelink);
                //Picasso.get().load(imagelink).into(imageView);
            }*/
            //JSONObject JO = new JSONObject();
            //textView2.setText(jsonArray.toString());

        /*} catch (InterruptedException e) {
            e.printStackTrace();
            product_name.setText("Interrupted Exception");
        } catch (ExecutionException e) {
            e.printStackTrace();
            product_name.setText("Execution Exception");
        } catch (JSONException e) {
            e.printStackTrace();
            product_name.setText("Enter Product Name");
        }*/


    }



    protected void apiRequest(String URL){
        final String savedURL = URL;

        //Log.d(TAG+" 82",data);
        //Log.d(TAG+" 84", URL);

        //String URL = "http://sms.optimistix.work/api/smsreceive/index.php";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response){
                        try {
                            JSONObject objres = new JSONObject((String) response);
                            String desc = objres.getString("description");
                            Toast.makeText(getApplicationContext(), desc, Toast.LENGTH_LONG).show();
                            product_name.setText(desc, TextView.BufferType.EDITABLE);
                            product_name.setSelection(product_name.getText().length());
                        }catch (JSONException e){
                            Log.i("Shah: Error: ",e.getMessage()+"\n"+e.getCause());
                            Toast.makeText(getApplicationContext(), "Server Error: "+e.getMessage(), Toast.LENGTH_LONG).show();
                            //product_name.setText("Not found, Type to Enter");
                            //product_name.setSelection(product_name.getText().length());
                            product_name.setHint("Not found, Type to Enter");
                        }
                        Log.i("Shah: Volley",response);
                    }
                },
                new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Shah Error", String.valueOf(error.getCause()));
                        Toast.makeText(getApplicationContext(),"Shah\n"+error.getCause(),Toast.LENGTH_LONG).show();
                    }
                });
        queue.add(stringRequest);
    }

}
