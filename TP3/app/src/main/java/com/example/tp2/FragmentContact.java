package com.example.tp2;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.util.DisplayMetrics;
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

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.widget.ImageView;

public class FragmentContact extends Fragment {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
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
    Dictionary libelleDonnee;
    ArrayList<Integer> idTextView = null;
    ArrayList<Integer> idEditText = null;
    private Contact c;
    ScrollableTabsActivity activity ;
    DBAdapter bd;
    private String currentPhotoPath;
    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    private String nImagePath; // Chemin de l'image

    String TAG = "TP3";
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
        bd.getChampsAddContact(1);
        bd.close();
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("nom", eNom.getText().toString());
        outState.putString("prenom", ePrenom.getText().toString());
        outState.putString("tel", eTel.getText().toString());
        outState.putString("adresse", eAdresse.getText().toString());
        outState.putString("cp", eCp.getText().toString());
        outState.putString("email", eEmail.getText().toString());
        outState.putString("metier", eMetier.getText().toString());
        outState.putString("situation", eSituation.getText().toString());
        outState.putInt("nImage", nImage);
        outState.putString("imagePath", nImagePath);

        if (libelleDonnee != null) {
            Bundle libelleBundle = new Bundle();
            for (Enumeration<String> keys = libelleDonnee.keys(); keys.hasMoreElements();) {
                String key = keys.nextElement();
                libelleBundle.putString(key, libelleDonnee.get(key).toString());
            }
            outState.putBundle("libelleDonnee", libelleBundle);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        if (width > height) {
            view = inflater.inflate(R.layout.activity_main1, container, false);
        } else {
            view = inflater.inflate(R.layout.activity_main2, container, false);
        }

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

        if (c.getCheminImage() != null) {
            File imgFile = new File(c.getCheminImage());
            if (imgFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                fminiature.setImageBitmap(bitmap);
                nImagePath = c.getCheminImage();
            }
        }
        
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

        if (savedInstanceState != null) {
            eNom.setText(savedInstanceState.getString("nom", ""));
            ePrenom.setText(savedInstanceState.getString("prenom", ""));
            eTel.setText(savedInstanceState.getString("tel", ""));
            eAdresse.setText(savedInstanceState.getString("adresse", ""));
            eCp.setText(savedInstanceState.getString("cp", ""));
            eEmail.setText(savedInstanceState.getString("email", ""));
            eMetier.setText(savedInstanceState.getString("metier", ""));
            eSituation.setText(savedInstanceState.getString("situation", ""));

            nImagePath = savedInstanceState.getString("imagePath");
            if (nImagePath != null) {
                File imgFile = new File(nImagePath);
                if (imgFile.exists()) {
                    Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    fminiature.setImageBitmap(bitmap);
                }
            }
            nImage
                    = savedInstanceState.getInt("nImage", 1);
            if (nImage > 0) {
                image(nImage);
            }

            Bundle libelleBundle = savedInstanceState.getBundle("libelleDonnee");
            if (libelleBundle != null) {
                libelleDonnee = new Hashtable<>();
                for (String key : libelleBundle.keySet()) {
                    libelleDonnee.put(key, libelleBundle.getString(key));
                }
            }
        }

        setUpButtonListeners();

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            fminiature.setImageBitmap(imageBitmap);
            nImage = -1; // Indiquer que l'utilisateur a pris une photo au lieu de sélectionner une image prédéfinie
            Log.d(TAG, "Photo prise avec succès et affichée.");
        } else {
            Log.d(TAG, "Aucune photo prise ou erreur.");
        }
    }

    private String getAlbumName() {
        return getString(R.string.album_name);
    }

    private void demanderPermissionCamera() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_IMAGE_CAPTURE);
        }
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

        view.findViewById(R.id.bPrendrePhoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirCamera();
            }
        });
    }

    public void cAjouterChamp(View v) {
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

    private void ouvrirCamera() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            demanderPermissionCamera();
        } else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } else {
                Toast.makeText(getContext(), "Impossible d'accéder à la caméra", Toast.LENGTH_SHORT).show();
            }
        }
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
        activity.supprimerContact(activity.obtenirPositionActuelle()+1);
    }


    public void ajouter(View v) {
        FragmentContactNouveau fragmentNouveau = new FragmentContactNouveau();
        activity.get_fragments().add(fragmentNouveau);
        activity.get_viewPagerAdapter().notifyDataSetChanged();
        activity.setupViewPager(activity.get_viewPager());
        activity.obtenirFragmentContact(activity.get_fragments().size()-1);
    }

    public void sauvegarder(View v) {
        libelleDonnee = new Hashtable();
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
            libelleDonnee.put(tempT.getText().toString(), tempE.getText().toString());
        }
        int miniature = nImage;
        Contact creer = new Contact(nom, prenom, tel, adresse, cp, email, metier, situation, miniature, libelleDonnee);
        creer.setCheminImage(nImagePath);
        Log.d(TAG, "sauvegarder: j'ai créer le contact" + creer);
        activity.ajouterContact(creer);
        Toast.makeText(view.getContext().getApplicationContext(), "Contact créé", Toast.LENGTH_LONG).show();
    }


    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getContext().sendBroadcast(mediaScanIntent);
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
