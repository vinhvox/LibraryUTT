package com.example.libraryutt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.CompoundButtonCompat;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.libraryutt.Details.BorrowDetail;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ShowBorrowDetails extends AppCompatActivity {
BorrowDetail borrowDetail;
EditText edtDateCreate, edtDatePay;
    DatabaseReference reference ;
    FirebaseDatabase database;
String valueBorrowRead ;
ProgressDialog progressDialog;
Query query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_borrow_details);
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        borrowDetail = (BorrowDetail) args.getSerializable("DATA");
        Log.e("DAT", borrowDetail.getPayDay());
        setupViews();
    }
    private  void setupViews(){
        database= FirebaseDatabase.getInstance("https://library-80e61.firebaseio.com/");
        reference = database.getReference();
        query = reference.child("BorrowBooks").orderByChild("promissoryNoteCode").equalTo(borrowDetail.getPromissoryNoteCode());
        progressDialog = new ProgressDialog(ShowBorrowDetails.this);
        valueBorrowRead = getString(R.string.in_place);
        TextView txtFormCodeShow = findViewById(R.id.txtFormCodeShow);
        TextView txtBorrower = findViewById(R.id.txtBorrower);
        TextView txtStudentCode = findViewById(R.id.txtStudentCode);
        TextView txtBookName = findViewById(R.id.txtBookName);
        TextView txtBookCode = findViewById(R.id.txtBookCode);
        edtDateCreate = findViewById(R.id.edtDateCreate);
        edtDatePay = findViewById(R.id.edtDatePay);
        Button btnSubmit = findViewById(R.id.btnSubmit);
        Button btnDelete = findViewById(R.id.btnDelete);

        txtFormCodeShow.setText(borrowDetail.getPromissoryNoteCode());
        txtBorrower.setText(borrowDetail.getBorrower());
        txtStudentCode.setText(borrowDetail.getUserCode());
        txtBookName.setText(borrowDetail.getBookName());
        txtBookCode.setText(borrowDetail.getBookCode());
        edtDateCreate.setText(borrowDetail.getDayCreate());
        edtDatePay.setText(borrowDetail.getPayDay());

        edtDateCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialogDateCreate();
            }
        });
        edtDatePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialogDatePay();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtDatePay.getText().toString().trim().equals("")){
                    Toast.makeText(ShowBorrowDetails.this, getString(R.string.cannot_be_less_than_the_creation_date), Toast.LENGTH_LONG).show();

                }
                else {
                    submitData();
                }
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData();
            }
        });

    }
    private  void datePickerDialogDateCreate(){
        final Calendar calendar  = Calendar.getInstance();
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String date = simpleDateFormat.format(calendar.getTime());
              switch (date.compareTo(borrowDetail.getDayCreate())) {
                  case 0:
                      edtDateCreate.setText(date);
                      break;
                  case 1:
                      edtDateCreate.setText(date);
                      break;
                  case -1:
                      Toast.makeText(ShowBorrowDetails.this, getString(R.string.cannot_be_less_than_the_creation_date), Toast.LENGTH_LONG).show();
                      break;
              }

            }
        }, year, month, day);
        datePickerDialog.show();
    }
    private  void datePickerDialogDatePay(){
        final Calendar calendar  = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String date = simpleDateFormat.format(calendar.getTime());
                String dateCreate = edtDateCreate.getText().toString().trim();
                switch (date.compareTo(dateCreate)) {
                    case 0:
                        edtDatePay.setText(date);
                        break;
                    case 1:
                        edtDatePay.setText(date);
                        break;
                    case -1:
                        Toast.makeText(ShowBorrowDetails.this, getString(R.string.cannot_be_less_than_the_creation_date), Toast.LENGTH_LONG).show();
                        break;
                }

            }
        }, year, month, day);
        datePickerDialog.show();
    }
    private void  getBorrowRead(){
        RadioButton radioInPlace = findViewById(R.id.radioInPlace);
        RadioButton radioCarriedAway = findViewById(R.id.radioCarriedAway);
        radioCarriedAway.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    valueBorrowRead = buttonView.getText().toString();
                }
            }
        });
        radioInPlace.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    valueBorrowRead = buttonView.getText().toString();
                }
            }
        });
    }
    private  void submitData(){
        getBorrowRead();

        progressDialog.setMessage(getString(R.string.sending_request));
        progressDialog.show();
        final HashMap<String, Object> map = new HashMap<>();
        map.put("dayCreate", edtDateCreate.getText().toString().trim());
        map.put("payDay", edtDatePay.getText().toString().trim());
        map.put("borrowRead", valueBorrowRead);
        map.put("status", Status.Borrowing);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        dataSnapshot.getRef().updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    progressDialog.dismiss();
                                    finish();
                                }
                                else {
                                    progressDialog.dismiss();
                                    Toast.makeText(ShowBorrowDetails.this, getString(R.string.sent_request_failed), Toast.LENGTH_LONG).show();

                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private  void  deleteData(){
        progressDialog.setMessage(getString(R.string.submit_a_data_deletion_request));
        progressDialog.show();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        dataSnapshot.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    progressDialog.dismiss();
                                    finish();
                                }
                                else {
                                    Toast.makeText(ShowBorrowDetails.this, getString(R.string.sent_request_failed), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_qr, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuQrCodeBorrow:
                createMyQrCode();
        }
        return super.onOptionsItemSelected(item);
    }
    private  void createMyQrCode(){
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(borrowDetail.getPromissoryNoteCode(), BarcodeFormat.QR_CODE, 200, 200);
            Bitmap bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.RGB_565);
            for (int x = 0; x<200; x++){
                for (int y=0; y<200; y++){
                    bitmap.setPixel(x,y,bitMatrix.get(x,y)? Color.BLACK : Color.WHITE);
                }
            }
            dialogShowMyQrCode(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private  void dialogShowMyQrCode( Bitmap bitmap){
        Dialog dialog = new Dialog(ShowBorrowDetails.this);
        dialog.setContentView(R.layout.my_qr_code);
        ImageView myQrCode = dialog.findViewById(R.id.myQrCode);
        myQrCode.setImageBitmap(bitmap);
        dialog.show();
    }
}