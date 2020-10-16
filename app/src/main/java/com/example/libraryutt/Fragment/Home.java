package com.example.libraryutt.Fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryutt.Adapter.BookHomeAdapter;
import com.example.libraryutt.Details.AccountEmployeeDetail;
import com.example.libraryutt.Details.BookDetail;
import com.example.libraryutt.R;
import com.example.libraryutt.ShowBookDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Home extends Fragment  implements BookHomeAdapter.Callback {
    View view ;
    public  static  AccountEmployeeDetail accountEmployeeDetail;
    ArrayList<BookDetail> bookDetailsLike, bookDetailsNew;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://library-80e61.firebaseio.com/");
    DatabaseReference reference;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       view = inflater.inflate(R.layout.fragment_home, container, false);
         reference = database.getReference("Books");

         new readDataLike().execute();
         new readDataNew().execute();
        return view;
    }


    private  void  setupViewsLike(ArrayList<BookDetail> bookDetails){
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewLike);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        BookHomeAdapter adapter = new BookHomeAdapter(bookDetails, getContext(), this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    private  class readDataLike extends AsyncTask<Void, Void,ArrayList<BookDetail> >{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            bookDetailsLike  = new ArrayList<>();
        }

        @Override
        protected ArrayList<BookDetail> doInBackground(Void... voids) {
            Query  query = reference.orderByChild("currentViews").limitToLast(20);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        BookDetail bookDetail ;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            bookDetail = dataSnapshot.getValue(BookDetail.class);
                            bookDetailsLike.add(bookDetail);

                        }
                        setupViewsLike(bookDetailsLike);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<BookDetail> bookDetails) {
            super.onPostExecute(bookDetails);
        }
    }
    private  class readDataNew extends AsyncTask<Void, Void,ArrayList<BookDetail>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            bookDetailsNew = new ArrayList<>();
        }

        @Override
        protected ArrayList<BookDetail> doInBackground(Void... voids) {
            Query  query = reference.orderByChild("dateCreate").limitToLast(20);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        BookDetail bookDetail ;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            bookDetail = dataSnapshot.getValue(BookDetail.class);
                            bookDetailsNew.add(bookDetail);

                        }
                        setupRecyclerNew(bookDetailsNew);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            return null;

        }
    }
    private  void setupRecyclerNew(ArrayList<BookDetail> bookDetails){
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewNew);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        BookHomeAdapter adapter = new BookHomeAdapter(bookDetails, getContext(), this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onClickItem(BookDetail bookDetail) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("DATA",bookDetail);
        Intent intent = new Intent(getContext(), ShowBookDetails.class);
        intent.putExtra("BUNDLE", bundle);
        startActivity(intent);

    }
}
