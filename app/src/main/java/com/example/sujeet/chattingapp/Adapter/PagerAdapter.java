package com.example.sujeet.chattingapp.Adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.sujeet.chattingapp.ChatFragment;
import com.example.sujeet.chattingapp.ContactFagment;

/**
 * Created by Sujeet on 12-10-2017.
 */

public class PagerAdapter extends FragmentPagerAdapter {
    String tabtitles[] = new String[]{"Chat","Contacts"};
    public PagerAdapter(FragmentManager fragmentManager){
        super(fragmentManager);
    }
    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        switch (position){
            case 0:
                return new ChatFragment();
            case 1:
                return new ContactFagment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabtitles[position];
    }
}