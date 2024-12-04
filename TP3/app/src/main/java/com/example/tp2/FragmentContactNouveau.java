package com.example.tp2;

import android.database.Cursor;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class FragmentContactNouveau extends FragmentContact{

    TextView tNum ;
    EditText eNom ;
    EditText ePrenom ;
    EditText eTel ;
    EditText eAdresse ;
    EditText eCp ;
    EditText eEmail ;
    EditText eMetier ;
    EditText eSituation ;
    ImageView fminiature ;
    Annuaire a1 ;
    int nImage = 1 ;
    View view ;
    ArrayList<String> libelleChamp = null;
    ArrayList<String> donneeChamp = null;
    ArrayList<Integer> idTextView = null;
    ArrayList<Integer> idEditText = null;

    DBAdapter dbAdapter;

    public FragmentContactNouveau() {
        super();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        tNum = (TextView) view.findViewById(R.id.textViewSNum);
        eNom = (EditText) view.findViewById(R.id.editTextSNom);
        ePrenom = (EditText) view.findViewById(R.id.editTextSPrenom);
        eTel = (EditText) view.findViewById(R.id.editTextSTel);
        eAdresse = (EditText) view.findViewById(R.id.editTextSAdresse);
        eCp = (EditText) view.findViewById(R.id.editTextSCp);
        eEmail = (EditText) view.findViewById(R.id.editTextSEmail);
        eMetier = (EditText) view.findViewById(R.id.editTextSMetier);
        eSituation = (EditText) view.findViewById(R.id.editTextSSituation);
        fminiature = (ImageView) view.findViewById(R.id.imageView);

        dbAdapter = new DBAdapter(getContext());
        dbAdapter.open();

        activity = (ScrollableTabsActivity) getActivity();
        idEditText = new ArrayList<>();
        idTextView = new ArrayList<>();

        tNum.setText("" + ((dbAdapter.getNbLigneTable("contacts") + 1)));
        eNom.setHint("saisir");
        ePrenom.setHint("saisir");
        eTel.setHint("saisir");
        eAdresse.setHint("saisir");
        eCp.setHint("saisir");
        eEmail.setHint("saisir");
        eMetier.setHint("saisir");
        eSituation.setHint("saisir");
        eNom.setText("");
        ePrenom.setText("");
        eTel.setText("");
        eAdresse.setText("");
        eCp.setText("");
        eMetier.setText("");
        eEmail.setText("");
        eSituation.setText("");
        /*fminiature.setImageResource(R.drawable.client1);
        nImage = 1;*/

        return view;
    }
}
