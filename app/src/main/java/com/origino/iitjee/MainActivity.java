package com.origino.iitjee;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker;
import com.treebo.internetavailabilitychecker.InternetConnectivityListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity  {
    Typeface typeface;
    ArrayAdapter<String> arrayAdapter;
    ListView listView;
    ArrayList<String> arrayList;
    InternetAvailabilityChecker mInternetAvailabilityChecker;
    String subject;
    public void move()
    {
        Intent intent=new Intent(this, Main2Activity.class);
        intent.putExtra("subject",subject);
        startActivity(intent);
        overridePendingTransition(R.transition.slide_left_enter,R.transition.slide_left_exit);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);



       //sharedprefwork

       sharedpref();


        listView=findViewById(R.id.listview);
        arrayList=new ArrayList<>();
        String object = readJSON();


        arrayList.clear();
        arrayList.add("Chemistry");
        arrayList.add("Maths");
        arrayList.add("Physics");
        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_activated_1, arrayList){
            @SuppressLint("WrongConstant")
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Cast the list view each item as text view
                TextView item = (TextView) super.getView(position,convertView,parent);
                typeface = ResourcesCompat.getFont(MainActivity.this, R.font.basic);
                item.setTypeface(typeface);

                // Set the typeface/font for the current item

                // item.setTypeface(Typeface.createFromAsset(parent
                // .getContext().getAssets(), "Manuale-Bold.ttf"));

                // Set the list view item's text color
                item.setTextColor(Color.parseColor("#01579B"));

                // Set the item text style to bold
                // item.setTypeface(Typeface.DEFAULT);

                // Change the item text size
                item.setTextSize(TypedValue.COMPLEX_UNIT_DIP,50);

                // return the view
                return item;
            }
        };
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                subject=arrayList.get(position);


                move();
            }
        });





    }

    public String readJSON() {
        String json = null;
        try {
            // Opening data.json file
            InputStream inputStream = getAssets().open("app.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];

            // read values in the byte array
            inputStream.read(buffer);
            inputStream.close();
            // convert byte to string
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return json;
        };
        return json;
    }
    public void sharedpref() {
        SharedPreferences sharedPref = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("clickcount", 0);
        editor.apply();



    }



}
