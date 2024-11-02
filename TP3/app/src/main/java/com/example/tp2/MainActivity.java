package com.example.tp2;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


import androidx.appcompat.app.AppCompatActivity;



public class MainActivity extends AppCompatActivity {
    DBAdapter bd ;
    String TAG = "annuaire";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: je suis mainActivity");
        setContentView(R.layout.activity_main2);
        Log.d(TAG, "onCreate: je charge le layout");
        startActivity(new Intent(MainActivity.this, ScrollableTabsActivity.class));
    }

}
