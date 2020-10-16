package com.example.libraryutt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;

import com.example.libraryutt.Adapter.PromissoryNoteAdapter;
import com.google.android.material.tabs.TabLayout;

public class PromissoryNote extends AppCompatActivity {
public static String  userCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pomissory_note);
        setupViews();
         userCode = getIntent().getStringExtra("userCode");

    }
    private void setupViews() {
        ViewPager viewPager = findViewById(R.id.viewpagerPromissory);
        TabLayout tabLayout = findViewById(R.id.tablayoutPromissory);
        viewPager.setAdapter(new PromissoryNoteAdapter(getSupportFragmentManager(), this));
        tabLayout.setupWithViewPager(viewPager);
    }
}