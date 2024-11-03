package com.example.tp2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Enumeration;


public class FragmentContact extends Fragment {

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
    int nImage = 1 ;
    View view ;
    ArrayList<String> libelleChamp = null;
    ArrayList<String> donneeChamp = null;
    ArrayList<Integer> idTextView = null;
    ArrayList<Integer> idEditText = null;
    private Contact c;
    ScrollableTabsActivity activity ;
    DBAdapter bd;

    String TAG = "annuaire";
    public FragmentContact(Contact c1) {
        c = c1;
    }

    public FragmentContact(){
        c = new Contact();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        bd = new DBAdapter(getContext());
        bd.open();
        bd.loadBD();
        bd.getChampsAddContact(1);
        bd.close();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_main2, container, false);
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

        activity = (ScrollableTabsActivity) getActivity();
        idEditText = new ArrayList<>();
        idTextView = new ArrayList<>();

        tNum.setText("" + (c.get_numC()+1));
        eNom.setText(c.get_nom().isEmpty() ? "non renseigné" : c.get_nom());
        ePrenom.setText(c.get_prenom().isEmpty() ? "non renseigné" : c.get_prenom());
        eTel.setText(c.get_tel().isEmpty() ? "non renseigné" : c.get_tel());
        eAdresse.setText(c.get_adresse().isEmpty() ? "non renseigné" : c.get_adresse());
        eCp.setText(c.get_cp().isEmpty() ? "non renseigné" : c.get_cp());
        eEmail.setText(c.get_email().isEmpty() ? "non renseigné" : c.get_email());
        eMetier.setText(c.get_metier().isEmpty() ? "non renseigné" : c.get_metier());
        eSituation.setText(c.get_situation().isEmpty() ? "non renseigné" : c.get_situation());
        image(c.get_miniature());

        if (c.get_libelleDonnee() != null && !c.get_libelleDonnee().isEmpty()) {
            for (Enumeration<String> keys = c.get_libelleDonnee().keys(); keys.hasMoreElements();) {
                String libelle = keys.nextElement();
                String donnee = c.get_libelleDonnee().get(libelle).toString();

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

        if (activity.get_annuaire().get_num() > 0) {
            activity.obtenirFragmentContact(0);
        }
    }


    public void cSuivant(View v) {
        if (activity.obtenirPositionActuelle() < activity.get_annuaire().get_num() - 1) {
            activity.obtenirFragmentContact((activity.obtenirPositionActuelle())+1);
        } else {
            Toast.makeText(view.getContext(), "Dernier contact atteint", Toast.LENGTH_SHORT).show();
        }
    }

    public void cPrecedent(View v) {
        if (activity.obtenirPositionActuelle() > 0) {
            activity.obtenirFragmentContact((activity.obtenirPositionActuelle())-1);
        } else {
            Toast.makeText(view.getContext(), "Premier contact atteint", Toast.LENGTH_SHORT).show();
        }
    }

    public void cFin(View v) {
        if (activity.get_annuaire().get_num() > 0) {
            activity.obtenirFragmentContact(activity.get_fragments().size()-1);
        }
    }

    public void cMilieu(View v){
        if (activity.get_annuaire().get_num() > 0) {
            activity.obtenirFragmentContact((activity.get_annuaire().get_num() - 1) / 2);
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
                    if (num > 0 && num <= activity.get_annuaire().get_num()) {
                        activity.obtenirFragmentContact(num-1);
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
        activity.get_annuaire().supprimer(activity.obtenirPositionActuelle());
        activity.initialiseFragments();
    }


    public void ajouter(View v) {
        FragmentContactNouveau fragmentNouveau = new FragmentContactNouveau();
        activity.get_fragments().add(fragmentNouveau);
        activity.get_viewPagerAdapter().notifyDataSetChanged();
        activity.setupViewPager(activity.get_viewPager());
        activity.obtenirFragmentContact(activity.get_fragments().size()-1);
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
}
