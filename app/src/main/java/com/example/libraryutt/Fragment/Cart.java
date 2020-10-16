package com.example.libraryutt.Fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.libraryutt.Adapter.BorrowAdapter;
import com.example.libraryutt.Adapter.CartAdapter;
import com.example.libraryutt.Adapter.PromissoryNoteAdapter;
import com.example.libraryutt.Details.AccountStudentsDetails;
import com.example.libraryutt.Details.BorrowDetail;
import com.example.libraryutt.R;
import com.example.libraryutt.ShowBorrowDetails;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Cart extends Fragment {

    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cart_main, container, false);
        setupViews();
        return view;
    }
    private void setupViews() {
        ViewPager viewPager = view.findViewById(R.id.viewPagerCart);
        TabLayout tabLayout = view.findViewById(R.id.tabCart);
        viewPager.setAdapter(new CartAdapter(getActivity().getSupportFragmentManager(), getContext()));
        tabLayout.setupWithViewPager(viewPager);
    }


}
