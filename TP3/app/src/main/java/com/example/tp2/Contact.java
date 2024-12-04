package com.example.tp2;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

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
    private Dictionary libelleDonne;
    String TAG = "TP3";
    private String cheminImage;

    public String getCheminImage() {
        return cheminImage;
    }

    public void setCheminImage(String cheminImage) {
        this.cheminImage = cheminImage;
    }

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
    public Dictionary get_libelleDonnee() {
        return (Hashtable) this.libelleDonne != null ? this.libelleDonne : new Hashtable();
    }

    public void set_libelleDonne(Dictionary d) {
        this.libelleDonne = d != null ? d : new Hashtable();
    }

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
        set_libelleDonne(new Hashtable());
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

    public Contact(Cursor cursor, Dictionary dic) {
        int h = cursor.getColumnIndex(DBAdapter.KEY_NUMERO);
        this.numC = Integer.parseInt(cursor.getString(h));
        int i = cursor.getColumnIndex(DBAdapter.KEY_NOM);
        this.nom = cursor.getString(i);
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
        int q = cursor.getColumnIndex(DBAdapter.KEY_MINIATURE);
        this.miniature = Integer.parseInt(cursor.getString(q));
        this.libelleDonne = dic ;
    }

    Contact(String nom, String prenom, String tel, String adresse, String cp, String email, String metier, String situation, int miniature, Dictionary libelleDonne){
        set_nom(nom);
        set_prenom(prenom);
        set_tel(tel);
        set_adresse(adresse);
        set_cp(cp);
        set_email(email);
        set_metier(metier);
        set_situation(situation);
        set_miniature(miniature);
        set_libelleDonne(libelleDonne);

    }

    public void ajouterChamp(String libelle, String donnee){
        libelleDonne.put(libelle, donnee);
    }


    public String toString() {
        StringBuilder chaine = new StringBuilder("num : " + get_numC() + " ; nom : " + nom + " ; prenom : " + prenom + " ; tel : " + tel + " ; adresse : " + adresse + " ; cp : " + cp + " ; email : " + email + " ; metier : " + metier + " ; situation : " + situation + " ; miniature : " + miniature + libelleDonne);
        chaine.append("\n");
        return chaine.toString();
    }

}