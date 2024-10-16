package com.chabrier.tablayoutviewpager;

public class Contact {

    // attributs d'instance
    private int numC ;
    private String nom ;
    private String prenom ;

    // accès attributs d'instance
    public int get_numC() { return this.numC ; }
    public void set_numC(int i) { this.numC = i ; }
    public String get_nom()	{ return this.nom ; }
    public void set_nom(String s) { this.nom = s ; }
    public String get_prenom() { return this.prenom ; }
    public void set_prenom(String s) { this.prenom = s ; }



    // constructeurs
    Contact(){}
    Contact(String nom, String prenom){
        set_nom(nom);
        set_prenom(prenom);

    }

    // methodes
    public String toString(){
        String chaine ;
        chaine = nom + " ; " + prenom +"\n" ;
        return chaine ;
    }
}