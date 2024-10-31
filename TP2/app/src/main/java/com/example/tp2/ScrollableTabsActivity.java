package com.example.tp2;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;

public class ScrollableTabsActivity extends AppCompatActivity {
    private static final String TAG = "ScrollableTabsActivityLog";

    Annuaire a1 ;
    private ViewPager viewPager;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate : Initialisation des fragments.");
        setContentView(R.layout.activity_scrollable_tabs);

        viewPager = findViewById(R.id.viewpager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        initialiseFragments();
    }

    public void initialiseFragments() {
        Log.d(TAG, "initialiseFragments : Chargement des fragments.");
        fragments.clear();
        a1 = new Annuaire();
        a1.lectureContacts(this, "fichier1.txt");

        if (a1.get_num() > 0) {
            Log.d(TAG, "initialiseFragments : Nombre de contacts trouvés = " + a1.get_num());
            for (Contact contact : a1.get_liste()) {
                fragments.add(new FragmentContact(contact));
                Log.d(TAG, "initialiseFragments : Fragment ajouté pour le contact : " + contact.get_nom());
            }
        } else {
            fragments.add(new FragmentContactNouveau());
            Log.d(TAG, "initialiseFragments : Aucun contact trouvé. Ajout de FragmentContactNouveau.");
        }
        updateViewPager();
    }

    private void updateViewPager() {
        Log.d(TAG, "updateViewPager : Mise à jour du ViewPager avec " + fragments.size() + " fragments.");
        adapter.refreshFragments(fragments);
        if (viewPager.getAdapter() == null) {
            viewPager.setAdapter(adapter);
        }
        adapter.notifyDataSetChanged();

        for (int i = 0; i < adapter.mFragmentList.size(); i++) {
            Log.d(TAG, "Fragment dans ViewPagerAdapter à la position " + i + " : " + adapter.mFragmentList.get(i).toString());
        }
    }

    public void setupViewPager(ViewPager viewPager) {
        Log.d(TAG, "setupViewPager : Configuration du ViewPager avec " + fragments.size() + " fragments.");
        resetViewPager();

        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        for (int i = 0 ; i < a1.get_num() ; i++) {
            adapter.addFrag(get_fragments().get(i), "");
            Log.d(TAG, "setupViewPager : Fragment ajouté à la position " + i);
        }

        adapter.refreshFragments(fragments);
        viewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void resetViewPager() {
        Log.d(TAG, "resetViewPager : Réinitialisation du ViewPager et rechargement des contacts.");
        fragments.clear();
        a1.lectureContacts(this, "fichier1.txt");

        if (a1.get_num() > 0) {
            for (int i = 0; i < a1.get_num(); i++) {
                Contact contact = a1.get_liste().get(i);
                fragments.add(new FragmentContact(contact));
                Log.d(TAG, "resetViewPager : Fragment ajouté pour le contact : " + contact.get_nom());
            }
        } else {
            fragments.add(new FragmentContactNouveau());
            Log.d(TAG, "resetViewPager : Aucun contact trouvé. Ajout de FragmentContactNouveau.");
        }

        updateViewPager();
        adapter.refreshFragments(fragments);
    }

    public Annuaire get_annuaire() {
        return a1;
    }

    public void set_annuaire(Annuaire a2) {
        a1 = a2;
    }

    public ArrayList<Fragment> get_fragments() {
        return fragments;
    }

    public void set_fragments(ArrayList<Fragment> f) {
        fragments = f;
    }

    public ViewPager get_viewPager() {
        return viewPager;
    }

    public void set_viewPager(ViewPager vp) {
        viewPager = vp;
    }

    public ViewPagerAdapter get_viewPagerAdapter() {
        return adapter;
    }

    public void set_viewPagerAdapter(ViewPagerAdapter vpa) {
        adapter = vpa;
    }

    public void obtenirFragmentContact(int index) {
        Log.d(TAG, "obtenirFragmentContact : Tentative d'affichage du fragment à l'index " + index);
        if (index >= 0 && index < fragments.size()) {
            Log.d(TAG, "obtenirFragmentContact : Fragment affiché à l'index " + index);
            viewPager.setCurrentItem(index, true);
        } else {
            Log.d(TAG, "obtenirFragmentContact : Index " + index + " hors limites.");
        }
    }

    public int obtenirPositionActuelle() {
        int position = viewPager.getCurrentItem();
        Log.d(TAG, "obtenirPositionActuelle : Position actuelle = " + position);
        return position;
    }

    public void supprimerContact(int index) {
        Log.d(TAG, "supprimerContact : Suppression du contact à l'index " + index);
        a1.supprimer(index, this, "fichier1.txt");
        initialiseFragments();
    }

    public void ajouterContact(FragmentContactNouveau contact) {
        Log.d(TAG, "ajouterContact : Ajout d'un nouveau contact");
        fragments.add(contact);
        updateViewPager();
    }

    private void updateFragments() {
        Log.d(TAG, "updateFragments : Mise à jour de la liste des fragments en fonction des contacts dans l'annuaire.");
        ArrayList<Fragment> newFragments = new ArrayList<>();
        for (int i = 0; i < a1.get_num(); i++) {
            Contact contact = a1.get_liste().get(i);
            newFragments.add(new FragmentContact(contact));
            Log.d(TAG, "updateFragments : Fragment ajouté pour le contact : " + contact.get_nom());
        }
        if (newFragments.isEmpty()) {
            Log.d(TAG, "updateFragments : Aucun contact trouvé. Ajout de FragmentContactNouveau.");
            newFragments.add(new FragmentContactNouveau());
        }
        updateViewPager();
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d(TAG, "getItem : Récupération du fragment à la position " + position);
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
            Log.d(TAG, "addFrag : Fragment ajouté avec le titre " + title);
        }

        public void clearFragments() {
            Log.d(TAG, "clearFragments : Suppression de tous les fragments.");
            mFragmentList.clear();
            mFragmentTitleList.clear();
            notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        public void refreshFragments(List<Fragment> newFragmentList) {
            Log.d(TAG, "refreshFragments : Rafraîchissement de la liste des fragments.");
            mFragmentList.clear();
            mFragmentList.addAll(newFragmentList);
            notifyDataSetChanged();
        }
    }
}
