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
                if(infos.length == 9){
                    String nom = infos[0];
                    String prenom = infos[1];
                    String tel = infos[2];
                    String adresse = infos[3];
                    String cp = infos[4];
                    String email = infos[5];
                    String metier = infos[6];
                    String situation = infos[7];
                    int miniature = Integer.parseInt(infos[8]);
                    Contact temp = new Contact(nom, prenom, tel, adresse, cp, email, situation, metier, miniature);
                    recreer.ajout(temp);
                }
            }
            recreer.set_num(recreer.get_liste().size());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}