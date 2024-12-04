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

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        fragments = new ArrayList<>();
        FragmentContact fr = new FragmentContact();
        fragments.add(fr);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        //loadContacts();

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu); // Charger le menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_preremplir) {
            loadContacts();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadContacts() {
        fragments.clear();
        adapter.clearFragments();
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
        //Toast.makeText(this,""+ contact, Toast.LENGTH_SHORT).show();
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


    private void refreshViewPager() {
        Log.d(TAG, "Contenu actuel de l'adaptateur :");
        for (int i = 0; i < adapter.getCount(); i++) {
            Fragment fragment = adapter.getItem(i);
            Log.d(TAG, "Fragment " + i + ": " + fragment.toString());
        }
        adapter.clearFragments();
        Log.d(TAG, "Contenu actuel après sup de l'adaptateur :");
        for (int i = 0; i < adapter.getCount(); i++) {
            Fragment fragment = adapter.getItem(i);
            Log.d(TAG, "Fragment " + i + ": " + fragment.toString());
        }
        for (Fragment fragment : fragments) {
            adapter.addFrag(fragment, ""); // Ajouter les nouveaux fragments
        }
        viewPager.setAdapter(adapter); // Réappliquer l'adaptateur
        adapter.notifyDataSetChanged(); // Notifier des changements
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
