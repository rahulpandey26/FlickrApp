package com.robosoft.flickrapp.helper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by rahul on 25/5/16.
 */
public class JSONParser {

    private JSONObject mJObj = null;
    private String mJson = "";

    public JSONObject getJSONFromUrl(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            // would return 200 if the connection is successful else throw an IO exception when no connection is available
            if (urlConnection.getResponseCode() == 200) {
                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());

                // Read the BufferedInputStream
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                mJson = stringBuilder.toString();
                urlConnection.setConnectTimeout(5000);
                urlConnection.disconnect();
            }
            try {
                mJObj = new JSONObject(mJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mJObj;
    }
}
