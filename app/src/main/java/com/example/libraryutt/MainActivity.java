package com.example.libraryutt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.libraryutt.Details.AccountEmployeeDetail;
import com.example.libraryutt.Details.AccountStudentsDetails;
import com.example.libraryutt.Fragment.Account;
import com.example.libraryutt.Fragment.Cart;
import com.example.libraryutt.Fragment.Home;
import com.example.libraryutt.Fragment.Search;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity {
    public  static String userCode;
    AccountStudentsDetails accountStudentsDetails;
    AccountEmployeeDetail accountEmployeeDetail;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    String email;
  ActionBar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseDatabase = FirebaseDatabase.getInstance("https://library-80e61.firebaseio.com/");
        reference = firebaseDatabase.getReference();
//        getDatUserStudents();
        setupView();

    }
    private  void setupView(){
        toolbar = getSupportActionBar();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadFragment(new Home());
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser()==null){
            return;
        }
        else {
            email = auth.getCurrentUser().getEmail();
        }

    }
    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigationHome:
                    fragment = new Home();
                    loadFragment(fragment);
                    return true;
                case R.id.navigationSearch:
                    fragment = new Search();
                    loadFragment(fragment);
                    return true;
                case R.id.navigationCart:
                    fragment = new Cart();
                    loadFragment(fragment);
                    return true;
                case R.id.navigationProfile:
                    fragment = new Account();
                    loadFragment(fragment);
                    return true;
            }
            return true;
        }
    };
    private  void getDatUserStudents(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance("https://library-80e61.firebaseio.com/");
        DatabaseReference reference = database.getReference("Accounts");
        final Query query = reference.child("Students").orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        accountStudentsDetails = dataSnapshot.getValue(AccountStudentsDetails.class);
                    }
                    Log.e("VINH", accountStudentsDetails.getStudentCode()+" st");
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("DATA_USER", (Serializable) accountStudentsDetails);
                    Home home = new Home();
                    home.setArguments(bundle);
                }
                getDataStaff();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private  void getDataStaff(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance("https://library-80e61.firebaseio.com/");
        DatabaseReference reference = database.getReference("Accounts");
        final Query query = reference.child("Staff").orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        accountEmployeeDetail = dataSnapshot.getValue(AccountEmployeeDetail.class);
                    }
                    Log.e("VINH", accountEmployeeDetail.getEmployeeCode()+" at");
                    Bundle bundle = new Bundle();
                    bundle.putString("DATA_USER",accountEmployeeDetail.getEmployeeCode());
                    Home home = new Home();
                    home.setArguments(bundle);
                }
                Log.e("VINH", "noDATA {{");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}