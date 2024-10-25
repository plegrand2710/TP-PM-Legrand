package com.example.tp2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class ScrollableTabsActivity extends AppCompatActivity {

    Annuaire a1 ;
    private ViewPager viewPager;
    private ArrayList<Fragment> fragments = null;
    ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrollable_tabs);

        initialiseFragments();

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

    public void initialiseFragments() {
        fragments = new ArrayList<>();
        a1 = new Annuaire();
        a1.lectureContacts(this, "fichier1.txt");
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

            for(int i = 0 ; i<fragments.size(); i++){
                adapter.addFrag(fragments.get(i), "");
            }

        viewPager.setAdapter(adapter);
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

    }
}
