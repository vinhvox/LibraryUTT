package com.example.libraryutt.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.libraryutt.Fragment.FragmentBorrowed;
import com.example.libraryutt.Fragment.FragmentExpired;
import com.example.libraryutt.Fragment.FragmentFinish;
import com.example.libraryutt.R;

import java.util.ArrayList;


public class PromissoryNoteAdapter extends FragmentStatePagerAdapter {
    FragmentBorrowed fragmentBorrowed;
    FragmentExpired fragmentExpired;
    FragmentFinish fragmentFinish;
    ArrayList<String> titles;
    public PromissoryNoteAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm);
        titles = new ArrayList<>();
        titles.add(context.getString(R.string.borrowed));
        titles.add(context.getString(R.string.expired));
        titles.add(context.getString(R.string.finish));
        fragmentBorrowed = new FragmentBorrowed();
        fragmentExpired = new FragmentExpired();
        fragmentFinish = new FragmentFinish();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return  fragmentBorrowed;
            case 1:
                return  fragmentExpired;
            case 2:
                return fragmentFinish;
        }
        return null;
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
