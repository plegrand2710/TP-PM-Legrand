package com.example.tp2;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

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

        viewPager = findViewById(R.id.viewpager);
        a1 = new Annuaire(this);
        bd = new DBAdapter(this);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        loadContacts();
    }

    private void loadContacts() {
        fragments.clear();

        ArrayList<Contact> contacts = a1.get_liste();
        for (Contact contact : contacts) {
            fragments.add(new FragmentContact(contact));
        }

        if (fragments.isEmpty()) {
            fragments.add(new FragmentContactNouveau());
        }
        refreshViewPager();
    }


    public void ajouterContact(Contact contact) {
        bd.open();
        a1.ajout(contact);
        FragmentContact fc = new FragmentContact(contact);
        fragments.add(fc);
        loadContacts();
    }

    public void supprimerContact(int id) {
        a1.supprimer(id);
        refreshViewPager();
    }

    private void refreshViewPager() {
        adapter.refreshFragments(fragments);
    }

    public void initialiseFragments() {
        setContentView(R.layout.activity_scrollable_tabs);
        fragments.clear();
        adapter.clearFragments();
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        fragments = new ArrayList<>();
        a1 = new Annuaire(this);
        if (a1.get_num() > 0) {
            for (int i = 0; i < a1.get_num(); i++) {
                Contact contact = a1.get_liste().get(i);
                FragmentContact fragmentContact = new FragmentContact(contact);
                fragments.add(fragmentContact);
            }
        }
        else {
            FragmentContact fr = new FragmentContactNouveau();
            fragments.add(fr);
        }

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
    }

    public void setupViewPager(ViewPager viewPager) {
        adapter.clearFragments();
        for (Fragment fragment : fragments) {
            adapter.addFrag(fragment, "");
        }
        adapter.notifyDataSetChanged();

        adapter.refreshFragments(fragments);
    }

    public void reinitialiseFragments(){
        fragments.clear();
        Log.d(TAG, "reinitialiseFragments: reinitialisation de la liste de fragments");
        adapter.clearFragments();
        Log.d(TAG, "reinitialiseFragments: reinitialisation du viewpageradapter");
        adapter.notifyDataSetChanged();
        Log.d(TAG, "reinitialiseFragments: notification de changement");
        if (a1.get_num() > 0) {
            for (int i = 0; i < a1.get_num(); i++) {
                Contact contact = a1.get_liste().get(i);
                Log.d(TAG, "reinitialiseFragments: creation d'un contact");
                FragmentContact fragmentContact = new FragmentContact(contact);
                fragments.add(fragmentContact);
                Log.d(TAG, "reinitialiseFragments: ajout au tableau de fragment");
            }
        }
        Log.d(TAG, "reinitialiseFragments: fin de la lecture de l'annuaire");
        
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);


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


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
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
        }

    }
}
