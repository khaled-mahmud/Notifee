package com.pgdit101.iit.notifee;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

public class ListViewActivity extends AppCompatActivity {

    Activity activity;
    ProductDBHelper myDB;


    /*ArrayList<HashMap<String, String>> todayList = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> tomorrowList = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> upcomingList = new ArrayList<HashMap<String, String>>();*/

    ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();


    public static String KEY_ID = "id";
    public static String KEY_PRODUCT_NAME = "product_name";
    public static String KEY_DESCRIPTION = "description";
    public static String KEY_DATE = "date";

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        activity = ListViewActivity.this;
        myDB = new ProductDBHelper(activity);
        listView = findViewById(R.id.list_view);
        //scrollView = (NestedScrollView) findViewById(R.id.scrollView);
        //loader = (ProgressBar) findViewById(R.id.loader);
        //taskListToday = (NoScrollListView) findViewById(R.id.taskListToday);
        //taskListTomorrow = (NoScrollListView) findViewById(R.id.taskListTomorrow);
        //taskListUpcoming = (NoScrollListView) findViewById(R.id.taskListUpcoming);

        //todayText = (TextView) findViewById(R.id.todayText);
        //tomorrowText = (TextView) findViewById(R.id.tomorrowText);
        //upcomingText = (TextView) findViewById(R.id.upcomingText);
    }


    public void openAddProduct(View v)
    {
        Intent intent = new Intent(this, AddProduct.class);
        startActivity(intent);
    }


    public void scanBarcode(View v)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    public void populateData()
    {
        //myDB = new ProductDBHelper(activity);
        //scrollView.setVisibility(View.GONE);
        //loader.setVisibility(View.VISIBLE);

        LoadProduct loadProduct = new LoadProduct();
        loadProduct.execute();
    }


    @Override
    public void onResume() {
        super.onResume();
        populateData();
    }



    public void loadListView(ListView listView, final ArrayList<HashMap<String, String>> dataList)
    {
        ListProductAdapter adapter = new ListProductAdapter(activity, dataList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(activity, AddProduct.class);
                intent.putExtra("isUpdate", true);
                intent.putExtra("id", dataList.get(position).get(KEY_ID));
                startActivity(intent);
            }
        });
    }


    public void loadDataList(Cursor cursor, ArrayList<HashMap<String, String>> dataList)
    {
        if(cursor!=null ) {
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {

                HashMap<String, String> map = new HashMap<String, String>();
                map.put(KEY_ID, cursor.getString(0).toString());
                map.put(KEY_PRODUCT_NAME, cursor.getString(1).toString());
                map.put(KEY_DESCRIPTION, cursor.getString(2).toString());
                map.put(KEY_DATE, Function.Epoch2DateString(cursor.getString(3).toString(), "dd-MM-yyyy"));
                dataList.add(map);
                cursor.moveToNext();
            }
        }
    }




    class LoadProduct extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dataList.clear();
            /*todayList.clear();
            tomorrowList.clear();
            upcomingList.clear();*/
        }

        protected String doInBackground(String... args) {
            String xml = "";

            /* ===== TODAY ========
            Cursor today = myDB.getDataToday();
            loadDataList(today, todayList);
            /* ===== TODAY ========

            /* ===== TOMORROW ========
            Cursor tomorrow = myDB.getDataTomorrow();
            loadDataList(tomorrow, tomorrowList);
            /* ===== TOMORROW ========*/

            /* ===== UPCOMING ========
            Cursor upcoming = myDB.getDataUpcoming();
            loadDataList(upcoming, upcomingList);
            /* ===== UPCOMING ========*/

            Cursor getData = myDB.getData();
            loadDataList(getData, dataList);

            return xml;
        }

        @Override
        protected void onPostExecute(String xml) {

            loadListView(listView, dataList);

            /*loadListView(taskListToday,todayList);
            loadListView(taskListTomorrow,tomorrowList);
            loadListView(taskListUpcoming,upcomingList);


            if(todayList.size()>0)
            {
                todayText.setVisibility(View.VISIBLE);
            }else{
                todayText.setVisibility(View.GONE);
            }

            if(tomorrowList.size()>0)
            {
                tomorrowText.setVisibility(View.VISIBLE);
            }else{
                tomorrowText.setVisibility(View.GONE);
            }

            if(upcomingList.size()>0)
            {
                upcomingText.setVisibility(View.VISIBLE);
            }else{
                upcomingText.setVisibility(View.GONE);
            }


            loader.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);*/
        }
    }

}
