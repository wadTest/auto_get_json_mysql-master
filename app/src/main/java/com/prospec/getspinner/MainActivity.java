package com.prospec.getspinner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {

    //    ประกาศตัวแปร
    private AutoCompleteTextView tambon;
    private EditText amphur, province, code;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gitEvent();

        checkVersion();

        getData();

    }//Method

    //    get event
    private void gitEvent() {

        tambon = (AutoCompleteTextView) findViewById(R.id.tambon);
        amphur = (EditText) findViewById(R.id.amphur);
        province = (EditText) findViewById(R.id.province);
        code = (EditText) findViewById(R.id.code);

    }//end gitEvent

    //    Permission StrictMode
    private void checkVersion() {

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }//ena checkVersion

//    get JSON AutoComplate
    private void getData() {

        String url = "http://119.59.103.121/app_mobile/get%20spinner.php";

        try {

            JSONArray data = new JSONArray(getJSONUrl(url));

            final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> map;

            for(int i = 0; i < data.length(); i++){

                JSONObject c = data.getJSONObject(i);

                map = new HashMap<String, String>();

                map.put("tambon_th", c.getString("tambon_th") + "\n");
                map.put("amphur_th", c.getString("amphur_th") + "\n");
                map.put("province_th", c.getString("province_th"));
                MyArrList.add(map);
            }

            SimpleAdapter sAdap;

            sAdap = new SimpleAdapter(MainActivity.this, MyArrList, R.layout.activity_column,
                    new String[] {"tambon_th", "amphur_th", "province_th"},//, "sdist_code"
                    new int[] {R.id.ColMemberID, R.id.ColName, R.id.ColTel});//, R.id.Code
            tambon.setAdapter(sAdap);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }//end getData


    public String getJSONUrl(String url) {
        StringBuilder str = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) { // Download OK
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    str.append(line);
                }
            } else {
                Log.e("Log", "Failed to download result..");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return str.toString();
    }
}//Main Class