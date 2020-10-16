package com.example.libraryutt.Fragment;

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
import com.example.libraryutt.Status;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FragmentFinish extends Fragment implements BorrowAdapter.Callback {
    ArrayList<BorrowDetail> borrowDetailArrayList;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    ImageView imageNoData;
    RecyclerView recyclerFinish;
    BorrowAdapter adapter;
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_finish, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        firebaseDatabase = FirebaseDatabase.getInstance("https://library-80e61.firebaseio.com/");
        reference = firebaseDatabase.getReference();
        imageNoData = view.findViewById(R.id.imageNoData);
        recyclerFinish = view.findViewById(R.id.recyclerFinish);
        new readData().execute(PromissoryNote.userCode);
    }

    @Override
    public void onClickItem(BorrowDetail borrowDetail) {

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
                            if (borrowDetail.getStatus().equals(com.example.libraryutt.Status.Finish))
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
        recyclerFinish.setLayoutManager(linearLayoutManager);
        adapter = new BorrowAdapter(borrowDetails, getContext(), this );
        recyclerFinish.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
