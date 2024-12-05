package com.example.tp2;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

public class ScrollableTabsActivity extends AppCompatActivity {

    private Annuaire a1;
    private ViewPager viewPager;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ViewPagerAdapter adapter;
    private final String TAG = "TP3";
    private DBAdapter bd ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrollable_tabs);
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        if (width > height) {
            setContentView(R.layout.activity_scrollable_tabs1);
        } else {
            setContentView(R.layout.activity_scrollable_tabs);
        }

        Toolbar menubar = findViewById(R.id.toolbar);
        setSupportActionBar(menubar);

        viewPager = findViewById(R.id.viewpager);
        a1 = new Annuaire(this);
        bd = new DBAdapter(this);
        bd.open();
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        fragments = new ArrayList<>();
        FragmentContact fr = new FragmentContact();
        fragments.add(fr);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        loadContacts();
        viewPager.setOffscreenPageLimit(fragments.size());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu); // Charger le menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_preremplir) {
            predefinirContacts();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void predefinirContacts() {
        fragments.clear();
        adapter.clearFragments();
        bd.clearAllTables();
        bd.open();
        bd.loadBD();
        Log.d(TAG, "loadContacts: j'ai rechargé la base");
        ArrayList<Contact> contacts = a1.get_liste();
        Log.d(TAG, "loadContacts: j'ai récupéré la base");
        int numContact = 1;
        for (Contact contact : contacts) {
            contact.set_numAffichage(numContact++);
            fragments.add(new FragmentContact(contact));
        }
        if (fragments.isEmpty()) {
            fragments.add(new FragmentContact());
        }
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        for (Fragment fragment : fragments) {
            adapter.addFrag(fragment, "");
        }
        viewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void loadContacts() {
        fragments.clear();
        adapter.clearFragments();
        bd.open();
        ArrayList<Contact> contacts = a1.get_liste();
        int numContact = 1;
        for (Contact contact : contacts) {
            contact.set_numAffichage(numContact++);
            fragments.add(new FragmentContact(contact));
        }
        if (fragments.isEmpty()) {
            fragments.add(new FragmentContact());
        }
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        for (Fragment fragment : fragments) {
            adapter.addFrag(fragment, "");
        }
        viewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void ajouterContact(Contact contact) {
        bd.open();
        a1.ajout(contact);
        FragmentContact fc = new FragmentContact(contact);
        fragments.add(fc);
        loadContacts();
        bd.close();
    }

    public void supprimerContact(int id) {
        Log.d(TAG, "supprimerContact: je suis dans la méthode avec id = " + id);
        bd.open();
        Log.d(TAG, "supprimerContact: j'ai ouvert la bd");
        a1.supprimer(id);
        Log.d(TAG, "supprimerContact: j'ai supprimer le contact");
        adapter.clearFragments();
        adapter.notifyDataSetChanged();
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.notifyDataSetChanged();
        loadContacts();
        adapter.notifyDataSetChanged();
        viewPager.setAdapter(adapter);
        bd.close();
    }


    public void sauvegarderTousLesContacts() {
        bd.open();
        Log.d(TAG, "sauvegarderTousLesContacts: Début de la sauvegarde");

        for (Fragment fragment : fragments) {
            try{
                if (fragment instanceof FragmentContact) {
                    FragmentContact contactFragment = (FragmentContact) fragment;

                    Contact contact = contactFragment.sauvegarder();
                    if (contact.get_numC() > 0) {
                        int rowsAffected = bd.updateContact(
                                contact.get_numC(),
                                contact.get_numC(),
                                contact.get_nom(),
                                contact.get_prenom(),
                                contact.get_tel(),
                                contact.get_adresse(),
                                contact.get_cp(),
                                contact.get_email(),
                                contact.get_metier(),
                                contact.get_situation(),
                                String.valueOf(contact.get_miniature())
                        );
                        enregistreChampSup(contact);
                        if (rowsAffected > 0) {
                            Log.d(TAG, "sauvegarderTousLesContacts: Contact mis à jour: " + contact);
                        } else {
                            Log.e(TAG, "sauvegarderTousLesContacts: Échec de la mise à jour du contact: " + contact);
                        }
                    } else {
                        long newId = bd.insertContact(contact);
                        if (newId > 0) {
                            contact.set_numC((int) newId);
                            Log.d(TAG, "sauvegarderTousLesContacts: Nouveau contact ajouté: " + contact);
                        } else {
                            Log.e(TAG, "sauvegarderTousLesContacts: Échec de l'ajout du contact: " + contact);
                        }
                    }
                }
            }catch(Exception e){
                Log.d(TAG, "Erreur avec le fragment : " + fragment, e);
            }
        }

        bd.close();
        Toast.makeText(this, "Tous les contacts ont été sauvegardés", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "sauvegarderTousLesContacts: Fin de la sauvegarde");
    }

    public void enregistreChampSup(Contact contact) {
        Dictionary<String, String> libelleDonnee = contact.get_libelleDonnee();
        if (libelleDonnee != null && libelleDonnee.size() > 0) {
            for (Enumeration<String> keys = libelleDonnee.keys(); keys.hasMoreElements();) {
                String libelle = keys.nextElement();
                String donnee = libelleDonnee.get(libelle);

                int champId = bd.getChampId(libelle, donnee);
                if (champId > 0) {
                    Log.d(TAG, "enregistreChampSup: Champ déjà existant: " + libelle + " = " + donnee);
                } else {
                    champId = (int) bd.insertChamp(null, libelle, donnee);
                    if (champId > 0) {
                        Log.d(TAG, "enregistreChampSup: Nouveau champ ajouté: " + libelle + " = " + donnee);
                    } else {
                        Log.e(TAG, "enregistreChampSup: Échec de l'ajout du champ: " + libelle);
                        continue;
                    }
                }
                if (!bd.existeRelationCC(contact.get_numC(), champId)) {
                    long lienId = bd.insertCc(null, contact.get_numC(), champId);
                    if (lienId > 0) {
                        Log.d(TAG, "enregistreChampSup: Lien ajouté entre contact " + contact.get_numC() + " et champ " + champId);
                    } else {
                        Log.e(TAG, "enregistreChampSup: Échec de l'ajout du lien pour le champ " + libelle);
                    }
                }
            }
        }
    }

    public void setupViewPager(ViewPager viewPager) {
        adapter.clearFragments();
        for (Fragment fragment : fragments) {
            adapter.addFrag(fragment, "");
        }
        adapter.notifyDataSetChanged();

        adapter.refreshFragments(fragments);
        viewPager.setAdapter(adapter);
    }

    public Annuaire get_annuaire(){
        return a1;
    }

    public void set_annuaire(Annuaire a2){
        a1 = a2 ;
    }

    public ArrayList<Fragment> get_fragments(){
        return fragments;
    }

    public void set_fragments(ArrayList<Fragment> f){
        fragments = f ;
    }

    public ViewPager get_viewPager(){
        return viewPager;
    }

    public void set_viewPager(ViewPager vp){
        viewPager = vp ;
    }

    public ViewPagerAdapter get_viewPagerAdapter(){
        return adapter;
    }

    public void set_viewPagerAdapter(ViewPagerAdapter vpa){
        adapter = vpa;
    }

    public void obtenirFragmentContact(int index){
        if (index >= 0 && index < fragments.size()) {
            viewPager.setCurrentItem(index, true);
        }
    }

    public int obtenirPositionActuelle() {
        return viewPager.getCurrentItem();
    }


    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
            notifyDataSetChanged();
        }

        public void clearFragments() {
            mFragmentList.clear();
            mFragmentTitleList.clear();
            notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        public void refreshFragments(List<Fragment> newFragmentList) {
            mFragmentList.clear();
            mFragmentList.addAll(newFragmentList);
            notifyDataSetChanged();

            for (int i = 0; i < mFragmentList.size(); i++) {
                Fragment fragment = mFragmentList.get(i);
            }
        }
    }
}
