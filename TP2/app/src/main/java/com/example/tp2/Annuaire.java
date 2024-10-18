package com.example.tp2;

import android.content.Context;
import android.widget.Toast;

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
    private int nbChampBase = 10;

    private ArrayList<Contact> liste ;

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

    public void ajout(Contact c){
        get_liste().add(c);
        inc_num();
    }

    public void supprimer(int i, Context c, String s){
        get_liste().remove(i);
        dec_num();
        this.sauvegarder(c, s);
    }

    // permet d'ajouter 1 contact
    public void ecritureContact(Context c, String s, Contact ctt) {
        try {
            FileOutputStream fichier = c.openFileOutput(s, Context.MODE_APPEND);
            fichier.write(ctt.toString().getBytes());
            fichier.close();
            Toast.makeText(c.getApplicationContext(),"contact ajouté",Toast.LENGTH_LONG).show();

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

    public void lectureContacts(Context c, String s) {
        try {
            FileInputStream fichier= c.openFileInput(s);
            InputStreamReader inputStreamReader = new InputStreamReader(fichier);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String ligne;
            get_liste().clear();
            Annuaire recreer = this;

            while((ligne = bufferedReader.readLine()) != null){
                String[] infos = ligne.split(" ; ");
                if(infos.length >= nbChampBase){
                    String num = infos[0].trim();
                    String nom = infos[1].trim();
                    String prenom = infos[2].trim();
                    String tel = infos[3].trim();
                    String adresse = infos[4].trim();
                    String cp = infos[5].trim();
                    String email = infos[6].trim();
                    String metier = infos[7].trim();
                    String situation = infos[8].trim();
                    int miniature = Integer.parseInt(infos[9]);
                    Contact temp = new Contact(nom, prenom, tel, adresse, cp, email, situation, metier, miniature);


                    if(infos.length > nbChampBase){
                        for (int i = 9; i < infos.length; i++) {
                            String[] autreLib = infos[i].split(" : ");
                            if (autreLib.length == 2) {
                                String libelle = autreLib[0].trim();
                                String donnee = autreLib[1].trim();
                                temp.ajouterChamp(libelle, donnee);
                            }
                        }

                    }
                    recreer.ajout(temp);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}