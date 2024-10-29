package com.example.tp2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;


public class EightFragment extends Fragment{

    private Contact c;

    public EightFragment(Contact c1) {
        // Required empty public constructor
        c = c1;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        LinearLayoutCompat.LayoutParams params =
                new LinearLayoutCompat.LayoutParams(
                        LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
                        LinearLayoutCompat.LayoutParams.WRAP_CONTENT);

        //---create a layout---
        LinearLayout layout = new LinearLayout(container.getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        TextView t1 = new TextView(container.getContext());
        t1.setText(c.get_prenom());
        t1.setLayoutParams(params);
        TextView t2 = new TextView(container.getContext());
        t2.setText(c.get_nom());
        t2.setLayoutParams(params);



        // Add the TextViews to the layout
        layout.addView(t1);
        layout.addView(t2);

        // Return the dynamic layout
        return layout;
    }

}
