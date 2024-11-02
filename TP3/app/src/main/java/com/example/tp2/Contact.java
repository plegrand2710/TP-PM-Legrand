package com.example.tp2;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class Contact {

    private int numC = 0;
    private String nom ;
    private String prenom ;
    private String tel ;
    private String adresse ;
    private String cp ;
    private String email ;
    private String metier ;
    private String situation ;
    private int miniature ;
    private ArrayList<String> libelleC = new ArrayList<String>();
    private ArrayList<String> donneeC = new ArrayList<String>();
    String TAG = "annuaire";

    public int get_numC() { return this.numC ; }
    public void set_numC(int i) { this.numC = i ; }
    public String get_nom()	{ return this.nom ; }
    public void set_nom(String s) { this.nom = s ; }
    public String get_prenom() { return this.prenom ; }
    public void set_prenom(String s) { this.prenom = s ; }
    public String get_tel()	{ return this.tel ; }
    public void set_tel(String s)	{ this.tel = s ; }
    public String get_adresse()	{ return this.adresse ; }
    public void set_adresse(String s)	{ this.adresse = s ; }
    public String get_cp()	{ return this.cp ; }
    public void set_cp(String s)	{ this.cp = s ; }
    public String get_email()	{ return this.email ; }
    public void set_email(String s)	{ this.email = s ; }
    public String get_metier()	{ return this.metier ; }
    public void set_metier(String s)	{ this.metier = s ; }
    public String get_situation()	{ return this.situation ; }
    public void set_situation(String s)	{ this.situation = s ; }
    public int get_miniature()	{ return this.miniature ; }
    public void set_miniature(int i) { this.miniature = i ; }
    public ArrayList<String> get_libelleC() {
        return this.libelleC != null ? this.libelleC : new ArrayList<>();  // Vérification que la liste n'est pas nulle
    }

    public void set_libelleC(ArrayList<String> a) {
        this.libelleC = a != null ? a : new ArrayList<>();  // Initialisation si null
    }

    public ArrayList<String> get_donneeC() {
        return this.donneeC != null ? this.donneeC : new ArrayList<>();  // Vérification que la liste n'est pas nulle
    }

    public void set_donneeC(ArrayList<String> a) {
        this.donneeC = a != null ? a : new ArrayList<>();  // Initialisation si null
    }


    // constructeurs
    Contact(){
        set_nom("");
        set_prenom("");
        set_tel("");
        set_adresse("");
        set_cp("");
        set_email("");
        set_metier("");
        set_situation("");
        set_miniature(1);
        set_donneeC(new ArrayList<>());
        set_libelleC(new ArrayList<>());
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(DBAdapter.KEY_NOM, this.nom);
        values.put(DBAdapter.KEY_PRENOM, this.prenom);
        values.put(DBAdapter.KEY_TEL, this.tel);
        values.put(DBAdapter.KEY_ADRESSE, this.adresse);
        values.put(DBAdapter.KEY_CP, this.cp);
        values.put(DBAdapter.KEY_EMAIL, this.email);
        values.put(DBAdapter.KEY_METIER, this.metier);
        values.put(DBAdapter.KEY_SITUATION, this.situation);
        values.put(DBAdapter.KEY_MINIATURE, this.miniature);
        return values;
    }

    public Contact(Cursor cursor) {
        Log.d(TAG, "Contact: création d'un contact");
        Log.d(TAG, "Contact: récupération du nom...");
        int i = cursor.getColumnIndex(DBAdapter.KEY_NOM);
        Log.d(TAG, "Contact: ...réussi donc affectation au nom...");
        this.nom = cursor.getString(i);
        Log.d(TAG, "Contact: ...réussi");
        int j = cursor.getColumnIndex(DBAdapter.KEY_PRENOM);
        this.prenom = cursor.getString(j);
        int k = cursor.getColumnIndex(DBAdapter.KEY_TEL);
        this.tel = cursor.getString(k);
        int l = cursor.getColumnIndex(DBAdapter.KEY_ADRESSE);
        this.adresse = cursor.getString(l);
        int m = cursor.getColumnIndex(DBAdapter.KEY_CP);
        this.cp = cursor.getString(m);
        int n = cursor.getColumnIndex(DBAdapter.KEY_EMAIL);
        this.email = cursor.getString(n);
        int o = cursor.getColumnIndex(DBAdapter.KEY_METIER);
        this.metier = cursor.getString(o);
        int p = cursor.getColumnIndex(DBAdapter.KEY_SITUATION);
        this.situation = cursor.getString(p);
        Log.d(TAG, "Contact: parametre principaux récupéré");
        int q = cursor.getColumnIndex(DBAdapter.KEY_MINIATURE);
        Log.d(TAG, "Contact: récupération dans la base");
        String c = cursor.getString(q);
        Log.d(TAG, "Contact: récupération de c : " + c);

        //this.miniature = Integer.parseInt(cursor.getString(q));
        Log.d(TAG, "Contact: toutes les parametres récupéré");
    }

    public void ajouterChamp(String libelle, String donnee){
        get_libelleC().add(libelle);
        get_donneeC().add(donnee);
    }


    public String toString() {
        StringBuilder chaine = new StringBuilder("num : " + get_numC() + " ; nom : " + nom + " ; prenom : " + prenom + " ; tel : " + tel + " ; adresse : " + adresse + " ; cp : " + cp + " ; email : " + email + " ; metier : " + metier + " ; situation : " + situation + " ; miniature : " + miniature);
        for(int i = 0; i < get_libelleC().size() && i < get_donneeC().size(); i++) {
            chaine.append(" ; ").append(get_libelleC().get(i)).append(" : ").append(get_donneeC().get(i));
        }
        chaine.append("\n");
        return chaine.toString();
    }

}