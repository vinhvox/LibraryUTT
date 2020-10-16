package com.example.libraryutt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.libraryutt.Details.AccountEmployeeDetail;
import com.example.libraryutt.Details.AccountStudentsDetails;
import com.example.libraryutt.Details.AuthorDetail;
import com.example.libraryutt.Details.BookDetail;
import com.example.libraryutt.Details.BorrowDetail;
import com.example.libraryutt.Details.PublishDetails;
import com.example.libraryutt.Fragment.Home;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class ShowBookDetails extends AppCompatActivity {
    BookDetail bookDetail;
    AuthorDetail authorDetail;
    PublishDetails publishDetails;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://library-80e61.firebaseio.com/");
    DatabaseReference reference;
    TextView txtAuthorNameShow, txtPublishNameShow;
    AccountEmployeeDetail accountEmployeeDetail;
    AccountStudentsDetails accountStudentsDetails;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_book_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
         bookDetail = (BookDetail) args.getSerializable("DATA");
         setupViews();
    }

    private void setupViews() {
        TextView txtBookNameShow = findViewById(R.id.txtBookNameShow);
         txtAuthorNameShow = findViewById(R.id.txtAuthorNameShow);
         txtPublishNameShow = findViewById(R.id.txtPublishNameShow);
        TextView txtPublishDateShow = findViewById(R.id.txtPublishDateShow);
        TextView txtCategoryShow = findViewById(R.id.txtCategoryShow);
        TextView txtNationShow = findViewById(R.id.txtNationShow);
        TextView txtLanguageShow = findViewById(R.id.txtLanguageShow);
        TextView txtIntroduceShow = findViewById(R.id.txtIntroduceShow);
        ImageView imageViewBookShow = findViewById(R.id.imageViewBookShow);

        Picasso.get().load(bookDetail.getCoverImage()).into(imageViewBookShow);
        txtBookNameShow.setText(bookDetail.getBookName());
        txtPublishDateShow.setText(bookDetail.getPublicationDate());
        txtCategoryShow.setText(bookDetail.getCategory());
        txtNationShow.setText(bookDetail.getNation());
        txtLanguageShow.setText(bookDetail.getLanguage());
        txtIntroduceShow.setText(bookDetail.getIntroduce());
        getAuthorInformation();
        getPublishInformation();

    }
    private  void  getAuthorInformation(){
        reference = database.getReference("Authors");
        Query query = reference.orderByChild("authorCode").equalTo(bookDetail.getAuthorCode());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        authorDetail = dataSnapshot.getValue(AuthorDetail.class);
                    }
                    txtAuthorNameShow.setText(authorDetail.getPseudonym());
                    showAuthor(authorDetail);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private  void  getPublishInformation(){
        reference = database.getReference("Publish");
        Query query = reference.orderByChild("publishCode").equalTo(bookDetail.getPublish());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        publishDetails = dataSnapshot.getValue(PublishDetails.class);
                    }
                    txtPublishNameShow.setText(publishDetails.getName());
                    showPublish(publishDetails);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private  void showPublish(final PublishDetails publishDetails){
        TextView txtPublishInformation = findViewById(R.id.txtPublishInformation);
        txtPublishInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogShowPublish(publishDetails);
            }
        });

    }
    private  void  dialogShowPublish(PublishDetails publishDetails){
        final Dialog dialog = new Dialog(ShowBookDetails.this);
        dialog.setContentView(R.layout.dialog_publish_show);
        ImageView imageViewPublish = dialog.findViewById(R.id.imageViewPublish);
        ImageView imageDismiss = dialog.findViewById(R.id.imageDismiss);
        TextView txtPublishName = dialog.findViewById(R.id.txtPublishName);
        TextView txtPublishCode = dialog.findViewById(R.id.txtPublishCode);
        TextView txtAddressOfPublish = dialog.findViewById(R.id.txtAddressOfPublish);
        TextView txtPhoneOfPublish = dialog.findViewById(R.id.txtPhoneOfPublish);
        TextView txtFaxOfPublish = dialog.findViewById(R.id.txtFaxOfPublish);
        TextView txtEmailOfPublish = dialog.findViewById(R.id.txtEmailOfPublish);
        TextView txtWebsiteOfPublish = dialog.findViewById(R.id.txtWebsiteOfPublish);
        TextView txtPublishIntro = dialog.findViewById(R.id.txtPublishIntro);
        Picasso.get().load(publishDetails.getImageUri()).into(imageViewPublish);
        txtPublishName.setText(publishDetails.getName());
        txtPublishCode.setText(publishDetails.getPublishCode());
        txtAddressOfPublish.setText(publishDetails.getAddress());
        txtPhoneOfPublish.setText(publishDetails.getPhone());
        txtFaxOfPublish.setText(publishDetails.getFax());
        txtEmailOfPublish.setText(publishDetails.getEmail());
        txtWebsiteOfPublish.setText(publishDetails.getWebsite());
        txtPublishIntro.setText(publishDetails.getIntro());
        imageDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private void  showAuthor(final AuthorDetail authorDetail){
        TextView txtAuthorInformation = findViewById(R.id.txtAuthorInformation);
        txtAuthorInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogShowAuthor(authorDetail);
            }
        });
    }
    private void dialogShowAuthor(AuthorDetail authorDetail){
        final Dialog dialog = new Dialog(ShowBookDetails.this);
        dialog.setContentView(R.layout.dialog_show_author);
        ImageView imageViewAuthorInfo = dialog.findViewById(R.id.imageViewAuthorInfo);
        ImageView imageButtonDismiss = dialog.findViewById(R.id.imageButtonDismiss);
        TextView txtPseudonym = dialog.findViewById(R.id.txtPseudonym);
        TextView txtAuthorNameInfo = dialog.findViewById(R.id.txtAuthorNameInfo);
        TextView txtAuthorCode = dialog.findViewById(R.id.txtAuthorCode);
        TextView txtNationOfAuth = dialog.findViewById(R.id.txtNationOfAuth);
        TextView txtBirthDayOfAuth = dialog.findViewById(R.id.txtBirthDayOfAuth);
        TextView txtCategoryOfAuth = dialog.findViewById(R.id.txtCategoryOfAuth);
        TextView txtArtworkOfAuth = dialog.findViewById(R.id.txtArtworkOfAuth);
        Picasso.get().load(authorDetail.getImageURI()).into(imageViewAuthorInfo);
        txtPseudonym.setText(authorDetail.getPseudonym());
        txtAuthorNameInfo.setText(authorDetail.getAuthorName());
        txtAuthorCode.setText(authorDetail.getAuthorCode());
        txtNationOfAuth.setText(authorDetail.getAuthorNation());
        txtBirthDayOfAuth.setText(authorDetail.getAuthorDay());
        txtCategoryOfAuth.setText(authorDetail.getCategory());
        txtArtworkOfAuth.setText(authorDetail.getArtwork());
        imageButtonDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_cart, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menuAddCart:
                checkUser();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private  void  checkUser(){
      FirebaseAuth auth = FirebaseAuth.getInstance();

      if (auth.getCurrentUser() == null){
          startActivity(new Intent(ShowBookDetails.this, Login.class));
      }
      else {
          String email = auth.getCurrentUser().getEmail();
          getDatUserStudents(email);
          getDataStaff(email);

      }


    }
    private  void getDatUserStudents(final String email){
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
                    checkExists(accountStudentsDetails.getStudentCode(), accountStudentsDetails.getName());
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private  void getDataStaff( String email){
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
                  checkExists(accountEmployeeDetail.getEmployeeCode(), accountEmployeeDetail.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private  void  checkExists(final String userCode, final String userName){
        final String key = userCode+ bookDetail.getBookCode();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Check Exists...");
        progressDialog.show();
        reference = database.getReference("BorrowBooks");

        Query query = reference.orderByChild("promissoryNoteCode").equalTo(key);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    progressDialog.dismiss();
                    Toast.makeText(ShowBookDetails.this, "Đã có trong danh sách", Toast.LENGTH_LONG).show();

                }
                else {
                    progressDialog.dismiss();
                   saveBorrowBook(userCode, userName, key);
                    Log.e("TAG", "null kasjk");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private  void  saveBorrowBook(String userCode,String userName,  String promissoryNoteCode){
        BorrowDetail borrowDetail =  new BorrowDetail(promissoryNoteCode, bookDetail.getBookCode(), bookDetail.getBookName(), userCode, userName,Status.Handling, " slal", getDateCreate(), "102");
        reference = database.getReference("BorrowBooks");
        reference.push().setValue(borrowDetail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ShowBookDetails.this, "Borrow book success...", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(ShowBookDetails.this, "Borrow book fail...", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    private  String getDateCreate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat  = new SimpleDateFormat("dd/MM/yyyy");
        return  simpleDateFormat.format(calendar.getTime());
    }
}