package com.example.libraryutt.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.libraryutt.Fragment.FragmentBorrowing;
import com.example.libraryutt.Fragment.FragmentHanding;
import com.example.libraryutt.R;

import java.util.ArrayList;

public class CartAdapter extends FragmentStatePagerAdapter{
    ArrayList<String> titles;
    FragmentHanding fragmentHanding;
    FragmentBorrowing fragmentBorrowing;
    public CartAdapter(@NonNull FragmentManager fm , Context context) {
        super(fm);
        titles = new ArrayList<>();
        titles.add(context.getString(R.string.handing));
        titles.add(context.getString(R.string.borrowing));
        fragmentHanding = new FragmentHanding();
        fragmentBorrowing = new FragmentBorrowing();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return  fragmentHanding;
            case 1:
                return fragmentBorrowing;
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
