package com.chabrier.tablayoutviewpager;

import android.content.Context;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


@SuppressWarnings("serial")

public class Annuaire {

    //attribut instances
    private int num = 0 ;

    private ArrayList<Contact> liste ;

    private ArrayList<Fragment> fragments ;

    public ArrayList<Contact> get_liste(){ return this.liste ; }
    public void set_liste(ArrayList<Contact> a){ this.liste = a ; }


    // accès attribut de classe
    public int get_num() { return num ; }
    public void set_num(int i) { num = i; }
    public void inc_num() { num++ ; }
    public void dec_num() { num-- ; }

    // constructeur
    Annuaire(){
        set_liste(new ArrayList<Contact>()) ;
    }

    void creerFragment(Context c, Contact c1){
        int l = fragments.size();
    }

}