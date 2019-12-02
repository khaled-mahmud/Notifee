package com.pgdit101.iit.notifee;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpGetRequest extends AsyncTask<String, Void, String> {

    public static final String REQUEST_METHOD = "GET";
    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;
    //String result = "";
    String description = "";


    @Override
    protected String doInBackground(String... params) {

        String stringUrl = params[0];
        String result = "";
        String inputLine;

        try {
            //Create a URL object holding our url
            URL myUrl = new URL(stringUrl);

            //Create a connection
            HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();

            //Set methods and timeouts
            connection.setRequestMethod(REQUEST_METHOD);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);

            //Connect to our url
            connection.connect();


            //Create a new InputStreamReader
            InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());

            //Create a new buffered reader and String Builder
            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();

            //Check if the line we are reading is not null
            while((inputLine = reader.readLine()) != null){
                stringBuilder.append(inputLine);
            }
            //Close our InputStream and Buffered reader
            reader.close();
            streamReader.close();

            //Set our result equal to our stringBuilder
            result = stringBuilder.toString();


            /*InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null){
                line = bufferedReader.readLine();
                result = result + line;
            }*/

            /*JSONArray jsonArray = new JSONArray(result);
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                //imageView.setImageURI((Uri) JO.getString("image"));
                //textView2.setText((String) JO.get("description"));

                description = (String) jsonObject.get("description");
                //imagelink = jsonObject.getString("image");
                //textView2.setText(description);
                //imageView.setImageURI((Uri) imagelink);
                //Picasso.get().load(imagelink).into(imageView);
            }*/


            /*JSONObject jsonObject = new JSONObject(result);
            JSONObject objA = jsonObject.getJSONObject("gcp");
            description = objA.getString("company");*/



        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } /*catch (JSONException e) {
            e.printStackTrace();
            //ApiActivity.textViewJson.setText("JSONException Exception");
        }*/


        return result;
    }




    protected void onPostExecute(String result){
        super.onPostExecute(result);
        //ApiActivity.textViewJson.setText(this.description);
    }


}
