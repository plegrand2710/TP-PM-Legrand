package com.example.tp2;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class Fragment1 extends Fragment {
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
    int ncontact = 0 ;
    View view ;
    ArrayList<String> libelleChamp = null;
    ArrayList<String> donneeChamp = null;
    ArrayList<Integer> idTextView = null;
    ArrayList<Integer> idEditText = null;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_main1, container, false);
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
        a1 = new Annuaire();
        a1.lectureContacts(view.getContext(), "fichier1.txt");

        idEditText = new ArrayList<>();
        idTextView = new ArrayList<>();

        setUpButtonListeners();
        return view;

    }

    private void setUpButtonListeners() {
        // Bouton "Moins"
        view.findViewById(R.id.bouttonAvant).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bMoins(v);
            }
        });

        // Bouton "Plus"
        view.findViewById(R.id.bouttonApres).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bPlus(v);
            }
        });

        // Bouton "Début"
        view.findViewById(R.id.bouttonDebut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cDebut(v);
            }
        });

        // Bouton "Suivant"
        view.findViewById(R.id.bouttonSuivant).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cSuivant(v);
            }
        });

        // Bouton "Précédent"
        view.findViewById(R.id.bouttonPrecedent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cPrecedent(v);
            }
        });

        // Bouton "Fin"
        view.findViewById(R.id.bouttonFin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cFin(v);
            }
        });

        // Bouton "Milieu"
        view.findViewById(R.id.bouttonMilieu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cMilieu(v);
            }
        });

        // Bouton "Numéro"
        view.findViewById(R.id.bouttonNumero).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cNumero(v);
            }
        });

        // Bouton "Supprimer"
        view.findViewById(R.id.bouttonSupprimer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cSupprimer(v);
            }
        });

        // Bouton "Ajouter"
        view.findViewById(R.id.bouttonAjouter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ajouter(v);
            }
        });

        // Bouton "Sauvegarder"
        view.findViewById(R.id.bouttonSauvegarder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sauvegarder(v);
            }
        });

        // Bouton "ajouter Champ"
        view.findViewById(R.id.bajouterChamp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cAjouterChamp(v);
            }
        });
    }

    public void cAjouterChamp(View v) {
        libelleChamp = new ArrayList<>();
        donneeChamp = new ArrayList<>();
        idEditText = new ArrayList<>();
        idTextView = new ArrayList<>();
        AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
        dialog.setTitle(R.string.ajouterChamp);

        EditText et = new EditText(view.getContext());
        et.setInputType(InputType.TYPE_CLASS_TEXT);
        dialog.setView(et);

        dialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int nul) {
                String libelle = et.getText().toString();
                if (!libelle.isEmpty()) {
                    TableLayout tl = view.findViewById(R.id.tableauChamp);

                    TableRow tr = new TableRow(view.getContext());

                    TableRow existingRow = (TableRow) tl.getChildAt(0);
                    TableRow.LayoutParams existingParams = (TableRow.LayoutParams) existingRow.getChildAt(0).getLayoutParams();

                    TextView tv = new TextView(view.getContext());
                    int textViewid = View.generateViewId();
                    tv.setId(textViewid);
                    tv.setText(libelle);
                    tv.setLayoutParams(existingParams);
                    tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    tv.setTextColor(getResources().getColor(R.color.textview));

                    EditText et1 = new EditText(view.getContext());
                    int editTextId = View.generateViewId();
                    et1.setId(editTextId);
                    et1.setInputType(InputType.TYPE_CLASS_TEXT);
                    et1.setLayoutParams(existingParams);
                    et1.setHint("saisir");

                    idTextView.add(tv.getId());
                    idEditText.add(et1.getId());

                    tr.addView(tv);
                    tr.addView(et1);

                    tl.addView(tr);


                }
            }
        });

        dialog.setNegativeButton(R.string.annuler, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int nul) {
                dialog.cancel();
            }
        });
        dialog.show();
    }


    public void bMoins(View v){
        if(nImage>=2){
            nImage--;
            image(nImage);
        }
    }

    public void bPlus(View v){
        if(nImage<=6){
            nImage++;
            image(nImage);
        }
    }

    public void cDebut(View v) {
        if (a1.get_num() > 0) {
            ncontact = 0;
            chargement();
        }
    }


    public void cSuivant(View v) {
        if (ncontact < a1.get_num() - 1) {
            ncontact++;
            chargement();
        } else {
            Toast.makeText(view.getContext(), "Dernier contact atteint", Toast.LENGTH_SHORT).show();
        }
    }

    public void cPrecedent(View v) {
        if (ncontact > 0) {
            ncontact--;
            chargement();
        } else {
            Toast.makeText(view.getContext(), "Premier contact atteint", Toast.LENGTH_SHORT).show();
        }
    }

    public void cFin(View v) {
        if (a1.get_num() > 0) {
            ncontact = a1.get_num() - 1;
            chargement();
        }
    }

    public void cMilieu(View v){
        if (a1.get_num() > 0) {
            ncontact = (a1.get_num() - 1) / 2;
            chargement();
        }
    }

    public void cNumero(View v) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
        dialog.setTitle(R.string.dialogue);

        EditText et = new EditText(view.getContext());
        et.setInputType(InputType.TYPE_CLASS_NUMBER);
        dialog.setView(et);

        dialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int nul) {
                String numero = et.getText().toString();
                if (!numero.isEmpty()) {
                    int num = Integer.parseInt(numero);
                    if (num > 0 && num <= a1.get_num()) {
                        ncontact = num - 1;
                        chargement();
                    } else {
                        Toast.makeText(view.getContext().getApplicationContext(), R.string.invalide, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        dialog.setNegativeButton(R.string.annuler, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int nul) {
                dialog.cancel();
            }
        });
        dialog.show();
    }


    public void cSupprimer(View v){
        int supp ;
        String num = tNum.getText().toString() ;
        supp = Integer.parseInt(num) ;
        a1.supprimer(--supp, view.getContext(), "fichier1.txt");
    }

    public void ajouter(View v) {
        tNum.setText("" + (a1.get_num()+1));

        clearAffichage();

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

        fminiature.setImageResource(R.drawable.client1);
        nImage = 1;
    }

    public void clearAffichage(){
        TableLayout tl = view.findViewById(R.id.tableauChamp);
        int count = tl.getChildCount();

        for (int i = count - 1; i > 8; i--) {
            View child = tl.getChildAt(i);
            if (child instanceof TableRow) {
                tl.removeView(child);
            }
        }

        if (libelleChamp != null) {
            libelleChamp.clear();
        }
        if (donneeChamp != null) {
            donneeChamp.clear();
        }
        if (idTextView != null) {
            idTextView.clear();
        }
        if (idEditText != null) {
            idEditText.clear();
        }
    }

    public void sauvegarder(View v){
        a1.sauvegarder(view.getContext(), "fichier1.txt");
        String nom = eNom.getText().toString();
        String prenom = ePrenom.getText().toString();
        String tel = eTel.getText().toString();
        String adresse = eAdresse.getText().toString();
        String cp = eCp.getText().toString();
        String email = eEmail.getText().toString();
        String metier = eMetier.getText().toString();
        String situation = eSituation.getText().toString();
        for(int i = 0 ; i < idTextView.size() && i < idEditText.size() ; i++){
            TextView tempT = (TextView) view.findViewById(idTextView.get(i));
            EditText tempE = (EditText) view.findViewById(idEditText.get(i));
            libelleChamp.add(tempT.getText().toString());
            donneeChamp.add(tempE.getText().toString());
        }
        int miniature = nImage;
        Contact creer = new Contact(nom, prenom, tel, adresse, cp, email, metier, situation, miniature, libelleChamp, donneeChamp);
        creer.set_numC(a1.get_num());
        a1.ajout(creer);
        a1.ecritureContact(view.getContext(), "fichier1.txt", creer);

        Toast.makeText(view.getContext().getApplicationContext(),"contact créé",Toast.LENGTH_LONG).show();
    }


    public void image(int i){
        switch (i) {
            case 1:
                fminiature.setImageResource(R.drawable.client1);
                break;
            case 2:
                fminiature.setImageResource(R.drawable.client2);
                break;
            case 3:
                fminiature.setImageResource(R.drawable.client3);
                break;
            case 4:
                fminiature.setImageResource(R.drawable.client4);
                break;
            case 5:
                fminiature.setImageResource(R.drawable.client5);
                break;
            case 6:
                fminiature.setImageResource(R.drawable.client6);
                break;
            case 7:
                fminiature.setImageResource(R.drawable.client7);
                break;
            default:
                fminiature.setImageResource(R.drawable.ic_launcher_background);
                break;
        }
    }

    public void chargement(){
        clearAffichage();
        a1.lectureContacts(view.getContext(), "fichier1.txt");
        Contact afficher = a1.get_liste().get(ncontact);
        tNum.setText("" + (afficher.get_numC()+1));
        eNom.setText(afficher.get_nom().isEmpty() ? "non renseigné" : afficher.get_nom());
        ePrenom.setText(afficher.get_prenom().isEmpty() ? "non renseigné" : afficher.get_prenom());
        eTel.setText(afficher.get_tel().isEmpty() ? "non renseigné" : afficher.get_tel());
        eAdresse.setText(afficher.get_adresse().isEmpty() ? "non renseigné" : afficher.get_adresse());
        eCp.setText(afficher.get_cp().isEmpty() ? "non renseigné" : afficher.get_cp());
        eEmail.setText(afficher.get_email().isEmpty() ? "non renseigné" : afficher.get_email());
        eMetier.setText(afficher.get_metier().isEmpty() ? "non renseigné" : afficher.get_metier());
        eSituation.setText(afficher.get_situation().isEmpty() ? "non renseigné" : afficher.get_situation());
        image(afficher.get_miniature());

        if(!afficher.get_libelleC().isEmpty() && !afficher.get_donneeC().isEmpty()){
            for (int i = 0; i < afficher.get_libelleC().size(); i++) {
                String libelle = afficher.get_libelleC().get(i);
                String donnee = afficher.get_donneeC().get(i);

                if (!libelle.isEmpty() && !donnee.isEmpty()) {
                    TableLayout tl = view.findViewById(R.id.tableauChamp);

                    TableRow tr = new TableRow(view.getContext());

                    TableRow existingRow = (TableRow) tl.getChildAt(0);
                    TableRow.LayoutParams existingParams = (TableRow.LayoutParams) existingRow.getChildAt(0).getLayoutParams();

                    TextView tv = new TextView(view.getContext());
                    int textViewid = View.generateViewId();
                    tv.setId(textViewid);
                    tv.setText(libelle);
                    tv.setLayoutParams(existingParams);
                    tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    tv.setTextColor(getResources().getColor(R.color.textview));

                    EditText et1 = new EditText(view.getContext());
                    int editTextId = View.generateViewId();
                    et1.setId(editTextId);
                    et1.setInputType(InputType.TYPE_CLASS_TEXT);
                    et1.setLayoutParams(existingParams);
                    et1.setText(donnee);

                    idTextView.add(tv.getId());
                    idEditText.add(et1.getId());

                    tr.addView(tv);
                    tr.addView(et1);

                    tl.addView(tr);
                }
            }
        }
    }
}
