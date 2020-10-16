package com.example.libraryutt.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.libraryutt.Details.AccountStudentsDetails;
import com.example.libraryutt.Details.BorrowDetail;
import com.example.libraryutt.Login;
import com.example.libraryutt.Profile;
import com.example.libraryutt.PromissoryNote;
import com.example.libraryutt.R;
import com.example.libraryutt.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Account extends Fragment {
    View view;
    String email, currentPassword, newPassword, confirmPassword;
    FirebaseAuth auth ;
    AccountStudentsDetails accountStudentsDetails;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    FirebaseUser user;
    ImageView imageProfile;
    TextView idLogOut;
    TextView scoreBorrowed, scoreExpired, scoreFinish;
    ProgressDialog progressDialog;
    Dialog dialogPass;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account, container, false);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_account, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuQrCode:
                createMyQrCode();
                break;
            case R.id.menuChangePassword:
                changePassword();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private  void changePassword(){
        dialogPass = new Dialog(getContext());
        dialogPass.setContentView(R.layout.dialog_change_password);
        final EditText edtCurrentPassword = dialogPass.findViewById(R.id.edtCurrentPassword);
        final EditText edtNewPassword = dialogPass.findViewById(R.id.edtNewPassword);
        final EditText edtConfirmPassword = dialogPass.findViewById(R.id.edtConfirmPassword);
        Button btnChangePassword = dialogPass.findViewById(R.id.btnChangePassword);
        final Button btnCancel = dialogPass.findViewById(R.id.btnCancel);

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPassword = edtCurrentPassword.getText().toString().trim();
                newPassword = edtNewPassword.getText().toString().trim();
                confirmPassword = edtConfirmPassword.getText().toString().trim();
                if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()){
                    Toast.makeText(getContext(), getString(R.string.enter_full_information), Toast.LENGTH_LONG).show();

                }
                else {
                    if (!newPassword.equals(confirmPassword)){
                        Toast.makeText(getContext(), getString(R.string.password_is_not_correct), Toast.LENGTH_LONG).show();
                    }
                    else {
                        requestChangePassword(currentPassword, confirmPassword);
                    }
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPass.dismiss();
            }
        });
        dialogPass.show();
    }
    private  void requestChangePassword(String oldPass, final String newPass){
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.sending_request));
        progressDialog.show();
         user= auth.getCurrentUser();
        AuthCredential credential = EmailAuthProvider
                .getCredential(email, oldPass);
        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getContext(), "Change password success...", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                                dialogPass.dismiss();

                            }
                            else {
                                Toast.makeText(getContext(), "Change password fail...", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                                dialogPass.dismiss();
                            }
                        }
                    });
                }else {
                    Toast.makeText(getContext(), task.getException()+"", Toast.LENGTH_LONG).show();

                }
            }
        });
    }
    private  void createMyQrCode(){
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(accountStudentsDetails.getStudentCode(), BarcodeFormat.QR_CODE, 200, 200);
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
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.my_qr_code);
        ImageView myQrCode = dialog.findViewById(R.id.myQrCode);
        myQrCode.setImageBitmap(bitmap);
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();

        firebaseDatabase = FirebaseDatabase.getInstance("https://library-80e61.firebaseio.com/");
        reference = firebaseDatabase.getReference();
        imageProfile  = view.findViewById(R.id.imageProfile);
        idLogOut = view.findViewById(R.id.idLogOut);
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser()==null){
            listenerEvent();
        }
        else {
            email = auth.getCurrentUser().getEmail();
            Log.e("TAG", email);
        }
//       setupViews();
        getDatUserStudents();
        listenerEvent();

    }

    private  void  setupViews(final AccountStudentsDetails accountStudentsDetails){

         TextView txtNameUser = view.findViewById(R.id.txtNameUser);
        TextView txtEmailUser = view.findViewById(R.id.txtEmailUser);
        TextView txtProfile = view.findViewById(R.id.txtProfile);
        txtNameUser.setText(accountStudentsDetails.getName());
        txtEmailUser.setText(accountStudentsDetails.getEmail());

        Picasso.get().load(accountStudentsDetails.getImage()).into(imageProfile);
        TextView txtPromissoryNote = view.findViewById(R.id.txtPromissoryNote);
        new getDataBorrow().execute(accountStudentsDetails.getStudentCode());

        txtPromissoryNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PromissoryNote.class);
                intent.putExtra("userCode", accountStudentsDetails.getStudentCode());
                startActivity(intent);
            }
        });
        txtProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Profile.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("DATA", accountStudentsDetails);
                intent.putExtra("BUNDLE", bundle);
                startActivity(intent);
            }
        });


    }
    private void  listenerEvent(){

        imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUser();
            }
        });
        idLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(getContext(), Login.class));

            }
        });
    }
    private  void  checkUser(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser()==null){
            startActivity(new Intent(getContext(), Login.class));
        }
        else {
            email = auth.getCurrentUser().getEmail();
            Log.e("TAG", email);
        }
    }
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

                        Log.e("TAG", accountStudentsDetails.getImage()+" ");
                    }
                    setupViews(accountStudentsDetails);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private  class getDataBorrow extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }
        @Override
        protected Void doInBackground(String... strings) {
            Query query = reference.child("BorrowBooks").orderByChild("userCode").equalTo(strings[0]);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        int borrowed = 0, expired =0, finish=0 ;
                        BorrowDetail borrowDetail;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            borrowDetail = dataSnapshot.getValue(BorrowDetail.class);
                            if (borrowDetail.getStatus().equals(com.example.libraryutt.Status.Borrowed))
                            {
                                borrowed +=1;
                            }
                            else if (borrowDetail.getStatus().equals(com.example.libraryutt.Status.Expired)){
                                expired +=1;
                            }
                            else if (borrowDetail.getStatus().equals(com.example.libraryutt.Status.Finish)){
                                finish+=1;
                            }
                            Log.e("DATA", borrowed +" a "+ expired+" a "+ finish);
                            setScore(borrowed, expired, finish);

                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            return null;
        }



    }
    private  void  setScore(int a, int b, int c){
        scoreFinish = view.findViewById(R.id.scoreFinish);
        scoreBorrowed = view.findViewById(R.id.scoreBorrowed);
        scoreExpired = view.findViewById(R.id.scoreExpired);
        scoreExpired.setText(b+"");
        scoreBorrowed.setText(a+"");
        scoreFinish.setText(c+"");
    }

}
