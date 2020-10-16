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

import com.example.libraryutt.Adapter.BorrowAdapter;
import com.example.libraryutt.Details.AccountStudentsDetails;
import com.example.libraryutt.Details.BorrowDetail;
import com.example.libraryutt.PromissoryNote;
import com.example.libraryutt.R;
import com.example.libraryutt.ShowBorrowDetails;
import com.example.libraryutt.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FragmentBorrowed extends Fragment implements BorrowAdapter.Callback {
    ArrayList<BorrowDetail> borrowDetailArrayList;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    ImageView imageNoData;
    RecyclerView recyclerViewPromissory;
    AccountStudentsDetails accountStudentsDetails;
    BorrowAdapter adapter;
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_borrowed, container, false);
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        firebaseDatabase = FirebaseDatabase.getInstance("https://library-80e61.firebaseio.com/");
        reference = firebaseDatabase.getReference();
                imageNoData = view.findViewById(R.id.imageNoData);
        recyclerViewPromissory = view.findViewById(R.id.recyclerBorrowed);
        new readData().execute(PromissoryNote.userCode);
//        checkUser();
    }
//    private  void  checkUser(){
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        if (auth.getCurrentUser() == null){
//            recyclerViewPromissory.setVisibility(View.GONE);
//            imageNoData.setVisibility(View.VISIBLE);
//        }
//        else {
//            String email = auth.getCurrentUser().getEmail();
//            getDatUserStudents(email);
//        }
//
//    }
//    private  void getDatUserStudents(final String email){
//        final FirebaseDatabase database = FirebaseDatabase.getInstance("https://library-80e61.firebaseio.com/");
//        DatabaseReference reference = database.getReference("Accounts");
//        final Query query = reference.child("Students").orderByChild("email").equalTo(email);
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()){
//
//                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
//                        accountStudentsDetails = dataSnapshot.getValue(AccountStudentsDetails.class);
//                    }
//                    new readData().execute(accountStudentsDetails.getStudentCode());
//
//                }
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    @Override
    public void onClickItem(BorrowDetail borrowDetail) {
        Intent intent = new Intent(getContext(), ShowBorrowDetails.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("DATA", borrowDetail);
        intent.putExtra("BUNDLE", bundle);
        startActivity(intent);
    }

    private  class readData extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            borrowDetailArrayList = new ArrayList<>();
        }
        @Override
        protected Void doInBackground(String... strings) {
            Query query = reference.child("BorrowBooks").orderByChild("userCode").equalTo(strings[0]);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        BorrowDetail borrowDetail;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            borrowDetail = dataSnapshot.getValue(BorrowDetail.class);
                            if (borrowDetail.getStatus().equals(com.example.libraryutt.Status.Borrowed))
                            {
                                borrowDetailArrayList.add(borrowDetail);
                            }
                        }
                        setupViews(borrowDetailArrayList);
                        imageNoData.setVisibility(View.GONE);
                    }
                    else {
                        imageNoData.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            return null;
        }



    }
    private void setupViews(ArrayList<BorrowDetail> borrowDetails) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerViewPromissory.setLayoutManager(linearLayoutManager);
        adapter = new BorrowAdapter(borrowDetails, getContext(), this );
        recyclerViewPromissory.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
