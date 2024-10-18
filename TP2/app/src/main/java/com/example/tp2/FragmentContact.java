package com.example.tp2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;


public class FragmentContact extends Fragment {

    private Contact c;

    public FragmentContact(Contact c1) {
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

        View rootView = inflater.inflate(R.layout.activity_main2, container, false);

        EditText et = rootView.findViewById(R.id.editTextSPrenom);
        et.setText(c.get_prenom());

        return rootView;
    }

}
