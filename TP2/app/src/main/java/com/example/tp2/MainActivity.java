package com.example.tp2;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button btnSimpleTabs, btnScrollableTabs, btnIconTextTabs, btnIconTabs, btnCustomIconTextTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Contact c1 = new Contact("legrand", "pauline", "061539473", "piece du houx", "98765", "pau@gmail.com", "etu", "suel", 3);
        Contact c2 = new Contact("legrand", "camille", "061539473", "piece du houx", "98765", "pau@gmail.com", "etu", "suel", 3);
        Contact c3 = new Contact("legrand", "corinne", "061539473", "piece du houx", "98765", "pau@gmail.com", "etu", "suel", 3);
        Contact c4 = new Contact("legrand", "anthony", "061539473", "piece du houx", "98765", "pau@gmail.com", "etu", "suel", 3);
        Annuaire a1 = new Annuaire();
        a1.ajout(c1);
        a1.ajout(c2);
        a1.ajout(c3);
        a1.ajout(c4);

        // Pass Annuaire object to ScrollableTabsActivity
        Intent intent = new Intent(MainActivity.this, ScrollableTabsActivity.class);
        intent. // Pass the annuaire object
        startActivity(intent);




    }

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        startActivity(new Intent(MainActivity.this, ScrollableTabsActivity.class));

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        //---get the current display info---
        DisplayMetrics display = this.getResources().getDisplayMetrics();

        int width = display.widthPixels;
        int height = display.heightPixels;
        if (width> height)
        {
            //---landscape mode---
            Fragment1 fragment1 = new Fragment1();
            // android.R.id.content refers to the content
            // view of the activity
            fragmentTransaction.replace(
                    android.R.id.content, fragment1);
        }
        else
        {
            //---portrait mode---
            Fragment2 fragment2 = new Fragment2();
            fragmentTransaction.replace(
                    android.R.id.content, fragment2);
        }
        fragmentTransaction.commit();
    }*/

}
