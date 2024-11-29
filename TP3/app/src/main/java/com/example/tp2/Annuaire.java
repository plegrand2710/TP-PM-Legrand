package com.example.tp2;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;


@SuppressWarnings("serial")

public class Annuaire {


    private int num = 0 ;
    private int nbChampBase = 10;
    private DBAdapter dbAdapter;
    private ArrayList<Contact> liste ;

    public void set_liste(ArrayList<Contact> a){ 
        this.liste = a ;
    }

    public int get_num() { return num ; }
    public void set_num(int i) { num = i; }
    public void inc_num() { num++ ; }
    public void dec_num() { num-- ; }

    String TAG = "TP3";


    Annuaire(Context context) {
        dbAdapter = new DBAdapter(context);
        dbAdapter.open();
        //dbAdapter.loadBD();
        set_liste(new ArrayList<Contact>());
    }

    public ArrayList<Contact> get_liste() {
        liste.clear();
        Cursor cursor = dbAdapter.getAllContacts();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int x = cursor.getColumnIndex(DBAdapter.KEY_NUMERO);
                int numero = cursor.getInt(x);
                Contact contact = new Contact(cursor, dbAdapter.getChampsAddContact(numero));
                liste.add(contact);
                inc_num();
            } while (cursor.moveToNext());
        }
        return liste;
    }

    public void ajout(Contact contact) {
        long nb = dbAdapter.insertContact(contact);
        contact.set_numC((int) nb);
        inc_num();
    }

    public void supprimer(int id) {
        long nb = dbAdapter.deleteContact(id);
        dec_num();
    }

    public void close() {
        dbAdapter.close();
    }

    public void ecritureContact(Context c, String s, Contact ctt) {
        try {
            FileOutputStream fichier = c.openFileOutput(s, Context.MODE_APPEND);
            fichier.write(ctt.toString().getBytes());
            fichier.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sauvegarder(Context c, String s) {
        try {
            FileOutputStream fichier = c.openFileOutput(s, c.MODE_PRIVATE);
            for(int i = 0 ; i < get_num() ; i++){
                ecritureContact(c, s, get_liste().get(i));
                get_liste().get(i).set_numC(i);
            }
            fichier.close();
            Toast.makeText(c.getApplicationContext(),"fichier créé",Toast.LENGTH_LONG).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}