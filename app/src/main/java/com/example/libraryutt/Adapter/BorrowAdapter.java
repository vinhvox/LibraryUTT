package com.example.libraryutt.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryutt.Details.BorrowDetail;
import com.example.libraryutt.R;

import java.util.List;

public class BorrowAdapter extends RecyclerView.Adapter<BorrowAdapter.ViewHolder> {
    List<BorrowDetail> borrowDetailList;
    Context context;
    Callback callback;

    public BorrowAdapter(List<BorrowDetail> borrowDetailList, Context context, Callback callback) {
        this.borrowDetailList = borrowDetailList;
        this.context = context;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = R.layout.row_promissory_note;
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
                callback.onClickItem(borrowDetailList.get(position));
            }
        });

    }
    private  void  inflaterToViews(View view, int position){
        TextView txtFormCode = view.findViewById(R.id.txtFormCode);
        TextView txtBookName = view.findViewById(R.id.txt);
        TextView txtDataCreate = view.findViewById(R.id.txtDataCreate);
        TextView txtStatus = view.findViewById(R.id.txtStatus);

        BorrowDetail borrowDetail = borrowDetailList.get(position);
        txtFormCode.setText(borrowDetail.getPromissoryNoteCode());
        txtBookName.setText(borrowDetail.getBookName());
        txtDataCreate.setText(borrowDetail.getDayCreate());
        txtStatus.setText(borrowDetail.getStatus()+"");


    }


    @Override
    public int getItemCount() {
        return borrowDetailList.size();
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
    public  interface  Callback{
        void  onClickItem(BorrowDetail borrowDetail);
    }
}
