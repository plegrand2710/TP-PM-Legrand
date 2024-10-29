package com.example.tp2;

import java.util.ArrayList;

public class Contact {

    // attributs d'instance
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

    // accès attributs d'instance
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
    Contact(String nom, String prenom, String tel, String adresse, String cp, String email, String metier, String situation, int miniature){
        set_nom(nom);
        set_prenom(prenom);
        set_tel(tel);
        set_adresse(adresse);
        set_cp(cp);
        set_email(email);
        set_metier(metier);
        set_situation(situation);
        set_miniature(miniature);
        set_donneeC(new ArrayList<>());
        set_libelleC(new ArrayList<>());

    }
    Contact(String nom, String prenom, String tel, String adresse, String cp, String email, String metier, String situation, int miniature, ArrayList<String> libelle, ArrayList<String> donnee){
        set_nom(nom);
        set_prenom(prenom);
        set_tel(tel);
        set_adresse(adresse);
        set_cp(cp);
        set_email(email);
        set_metier(metier);
        set_situation(situation);
        set_miniature(miniature);
        set_libelleC(libelle);
        set_donneeC(donnee);

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