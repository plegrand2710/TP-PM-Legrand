package com.example.tp2;


import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;


import androidx.appcompat.app.AppCompatActivity;



public class MainActivity extends AppCompatActivity {
    DBAdapter bd ;
    String TAG = "annuaire";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(MainActivity.this, ScrollableTabsActivity.class));
    }

}
