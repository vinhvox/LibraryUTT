package com.example.libraryutt.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryutt.Details.BookDetail;
import com.example.libraryutt.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookHomeAdapter extends RecyclerView.Adapter<BookHomeAdapter.ViewHolder> {
    ArrayList<BookDetail> bookDetails;
    Context context;
    Callback callback;

    public BookHomeAdapter(ArrayList<BookDetail> bookDetails, Context context, Callback callback) {
        this.bookDetails = bookDetails;
        this.context = context;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = R.layout.item_book_show;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(layoutId, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        View view = holder.getView();
        inflaterToViews(view, position);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClickItem(bookDetails.get(position));
            }
        });

    }
    private void inflaterToViews(View view, int position){
        ImageView imageViewBookShow = view.findViewById(R.id.imageViewBookShow);
        TextView textViewBookShow = view.findViewById(R.id.textViewBookShow);
        BookDetail bookDetail = bookDetails.get(position);
        Picasso.get().load(bookDetail.getCoverImage()).into(imageViewBookShow);
        textViewBookShow.setText(bookDetail.getBookName());
    }

    @Override
    public int getItemCount() {
        return bookDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
        }

        public View getView() {
            return view;
        }
    }
    public  interface Callback{
        void  onClickItem(BookDetail bookDetail);
    }
}
