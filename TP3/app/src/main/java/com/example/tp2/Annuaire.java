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

    //attribut instances
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

    String TAG = "annuaire";


    Annuaire(Context context) {
        Log.d(TAG, "Annuaire: création de l'annuaire et ouverture de la base de donnée");
        dbAdapter = new DBAdapter(context);
        Log.d(TAG, "Annuaire: création de l'adaptateur");
        dbAdapter.open();
        Log.d(TAG, "Annuaire: ajout de 2 contacts");
        dbAdapter.loadBD();
        Log.d(TAG, "Annuaire: ouverture de la base de donnée");
        set_liste(new ArrayList<Contact>());
        Log.d(TAG, "Annuaire: chargement de la liste des contacts");
        //dbAdapter.close();
    }

    public ArrayList<Contact> get_liste() {
        Log.d(TAG, "get_liste: vidé la liste existante...");
        liste.clear();
        Log.d(TAG, "get_liste: ...réussi donc création d'un curseur pour récupérer les contacts...");
        Cursor cursor = dbAdapter.getAllContacts();
        Log.d(TAG, "get_liste: ...curseur réussi");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Log.d(TAG, "get_liste: création d'un contact...");
                List<String> l = dbAdapter.getTableColumns(DBAdapter.TABLE_CONTACTS);
                Log.d(TAG, "get_liste: liste d'attributs : " + l);
                Contact contact = new Contact(cursor);
                Log.d(TAG, "get_liste: réussi donc ajout à la liste...");
                liste.add(contact);
                Log.d(TAG, "get_liste: ...ajout réussi");
            } while (cursor.moveToNext());
        }
        return liste;
    }

    public void ajout(Contact contact) {
        dbAdapter.insertContact(contact);
        inc_num();
    }

    public void supprimer(int id) {
        dbAdapter.deleteContact(id);
        dec_num();
    }

    public void close() {
        dbAdapter.close();
    }


    // permet d'ajouter 1 contact
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