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
        Log.d(TAG, "onCreate: Initialising fragments.");
        setContentView(R.layout.activity_scrollable_tabs);

        viewPager = findViewById(R.id.viewpager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        initialiseFragments();
    }

    public void initialiseFragments() {
        Log.d(TAG, "initialiseFragments: Loading fragments.");
        fragments.clear();
        a1 = new Annuaire();
        a1.lectureContacts(this, "fichier1.txt");

        if (a1.get_num() > 0) {
            Log.d(TAG, "initialiseFragments: Number of contacts found = " + a1.get_num());
            for (Contact contact : a1.get_liste()) {
                fragments.add(new FragmentContact(contact));
                Log.d(TAG, "initialiseFragments: Added fragment for contact: " + contact.get_nom());
            }
        } else {
            fragments.add(new FragmentContactNouveau());
            Log.d(TAG, "initialiseFragments: No contacts found. Added FragmentContactNouveau.");
        }
        updateViewPager();
    }

    private void updateViewPager() {
        Log.d(TAG, "updateViewPager: Updating ViewPager with " + fragments.size() + " fragments.");
        adapter.refreshFragments(fragments);
        viewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void setupViewPager(ViewPager viewPager) {
        Log.d(TAG, "setupViewPager: Setting up ViewPager with " + fragments.size() + " fragments.");
        resetViewPager();

        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        for(int i = 0 ; i < a1.get_num() ; i++){
            adapter.addFrag(get_fragments().get(i), "");
            Log.d(TAG, "setupViewPager: Added fragment at position " + i);
        }

        adapter.refreshFragments(fragments);
        viewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    public void resetViewPager() {
        Log.d(TAG, "resetViewPager: Resetting ViewPager and reloading contacts.");
        fragments.clear();
        a1.lectureContacts(this, "fichier1.txt");

        if (a1.get_num() > 0) {
            for (int i = 0; i < a1.get_num(); i++) {
                Contact contact = a1.get_liste().get(i);
                fragments.add(new FragmentContact(contact));
                Log.d(TAG, "resetViewPager: Added fragment for contact: " + contact.get_nom());
            }
        } else {
            fragments.add(new FragmentContactNouveau());
            Log.d(TAG, "resetViewPager: No contacts found. Added FragmentContactNouveau.");
        }
        adapter.refreshFragments(fragments);
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
        Log.d(TAG, "obtenirFragmentContact: Attempting to display fragment at index " + index);
        if (index >= 0 && index < fragments.size()) {
            Log.d(TAG, "obtenirFragmentContact: Displayed fragment at index " + index);
            viewPager.setCurrentItem(index, true);
        } else {
            Log.d(TAG, "obtenirFragmentContact: Index " + index + " out of bounds.");
        }
    }

    public int obtenirPositionActuelle() {
        int position = viewPager.getCurrentItem();
        Log.d(TAG, "obtenirPositionActuelle: Current position = " + position);
        return position;
    }

    public void supprimerContact(int index) {
        Log.d(TAG, "supprimerContact: Removing contact at index " + index);
        a1.supprimer(index, this, "fichier1.txt");
        updateFragments();
    }

    public void ajouterContact(FragmentContactNouveau contact) {
        Log.d(TAG, "ajouterContact: Adding new contact");
        fragments.add(contact);
        updateFragments();
    }

    private void updateFragments() {
        Log.d(TAG, "updateFragments: Updating fragment list based on contacts in Annuaire.");
        ArrayList<Fragment> newFragments = new ArrayList<>();
        for (int i = 0; i < a1.get_num(); i++) {
            Contact contact = a1.get_liste().get(i);
            newFragments.add(new FragmentContact(contact));
            Log.d(TAG, "updateFragments: Added fragment for contact: " + contact.get_nom());
        }
        if (newFragments.isEmpty()) {
            Log.d(TAG, "updateFragments: No contacts found. Adding FragmentContactNouveau.");
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
            Log.d(TAG, "getItem: Retrieving fragment at position " + position);
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            //Log.d(TAG, "getCount: Fragment count = " + mFragmentList.size());
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
            Log.d(TAG, "addFrag: Added fragment with title " + title);
        }

        public void clearFragments() {
            Log.d(TAG, "clearFragments: Clearing all fragments.");
            mFragmentList.clear();
            mFragmentTitleList.clear();
            notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        public void refreshFragments(List<Fragment> newFragmentList) {
            Log.d(TAG, "refreshFragments: Refreshing fragments list.");
            mFragmentList.clear();
            mFragmentTitleList.clear();
            mFragmentList.addAll(newFragmentList);
            notifyDataSetChanged();
        }
    }
}
