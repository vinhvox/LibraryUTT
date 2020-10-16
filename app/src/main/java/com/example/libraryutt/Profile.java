package com.example.libraryutt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsSpinner;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.libraryutt.Details.AccountStudentsDetails;
import com.example.libraryutt.Details.BookDetail;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Profile extends AppCompatActivity {
    private static final int PERMISSION_CODE = 102;
    private static final int REQUEST_CODE_IMAGE_CAPTURE = 103;
    private static final int REQUEST_CODE_PICK_CAPTURE = 104 ;
    AccountStudentsDetails accountStudentsDetails;
    String valueSex, name, birthday, address, cmnd, phone;
    ProgressDialog progressDialog;

    EditText edtNameProfile, edtBirthDayStudentProfile, edtStudentAddressProfile, edtCardCodeStudentProfile, edtPhoneStudentProfile;
ImageView imageProfile;

    private ArrayList<String> arrayListSex;
    private AbsSpinner spinnerSexEdit;
    private Uri imageUri, uriDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        accountStudentsDetails = (AccountStudentsDetails) args.getSerializable("DATA");
        setupViews();

    }
    private  void setupViews(){
        edtNameProfile = findViewById(R.id.edtNameProfile);
        edtBirthDayStudentProfile = findViewById(R.id.edtBirthDayStudentProfile);
        edtStudentAddressProfile = findViewById(R.id.edtStudentAddressProfile);
        edtCardCodeStudentProfile = findViewById(R.id.edtCardCodeStudentProfile);
        edtPhoneStudentProfile = findViewById(R.id.edtPhoneStudentProfile);
        imageProfile = findViewById(R.id.imageProfile);
        TextView txtStudentCodeProfile = findViewById(R.id.txtStudentCodeProfile);
        TextView txtEmailProfile = findViewById(R.id.txtEmailProfile);
        spinnerSexEdit = findViewById(R.id.spinnerStudentSexProfile);

        edtNameProfile.setText(accountStudentsDetails.getName());
        edtBirthDayStudentProfile.setText(accountStudentsDetails.getBirthDay());
        edtCardCodeStudentProfile.setText(accountStudentsDetails.getCmnd());
        edtStudentAddressProfile.setText(accountStudentsDetails.getAddress());
        edtPhoneStudentProfile.setText(accountStudentsDetails.getPhone()+"");
        txtStudentCodeProfile.setText(accountStudentsDetails.getStudentCode());
        txtEmailProfile.setText(accountStudentsDetails.getEmail());
        Picasso.get().load(accountStudentsDetails.getImage()).into(imageProfile);
        setupDataSex();
changeImageProfile();


    }
    private  void  setupDataSex(){
        arrayListSex = new ArrayList<>();
        arrayListSex.add(getString(R.string.male));
        arrayListSex.add(getString(R.string.female));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Profile.this, android.R.layout.simple_list_item_1, arrayListSex);
        spinnerSexEdit.setAdapter(arrayAdapter);
        spinnerSexEdit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                valueSex = arrayListSex.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        int pos = arrayAdapter.getPosition(accountStudentsDetails.getSex());
        spinnerSexEdit.setSelection(pos);
    }
    private  void changeImageProfile(){
        imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRequestPermission();
                showMenuImages(v);
            }
        });
    }
    private  void  showMenuImages(View view){
        PopupMenu popupMenu = new PopupMenu(Profile.this, view);
        popupMenu.inflate(R.menu.menu_image);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menuOpenCamera:
                        openCamera();
                        break;
                    case R.id.menuOpenGallery:
                        openGallery();
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }
    private void  checkRequestPermission(){
        if (Build.VERSION.SDK_INT> Build.VERSION_CODES.LOLLIPOP_MR1){
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                String[] permission ={ Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permission, PERMISSION_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            }
            else {
                Toast.makeText(Profile.this, getString(R.string.denied), Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(Profile.this, getString(R.string.denied), Toast.LENGTH_LONG).show();
        }
    }
    private  void openCamera(){
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_CODE_IMAGE_CAPTURE);
    }
    private void openGallery(){
        Intent intentPickGallery = new Intent(Intent.ACTION_PICK);
        intentPickGallery.setType("image/*");
        startActivityForResult(intentPickGallery, REQUEST_CODE_PICK_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== REQUEST_CODE_IMAGE_CAPTURE&& resultCode== RESULT_OK&& data!= null){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageProfile.setImageBitmap(imageBitmap);

        }
        if (requestCode== REQUEST_CODE_PICK_CAPTURE&& resultCode== RESULT_OK&& data!= null){
            imageUri = data.getData();
            imageProfile.setImageURI(imageUri);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menuSave:
                informationProcessing();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void informationProcessing() {
        name = edtNameProfile.getText().toString().trim();

        birthday = edtBirthDayStudentProfile.getText().toString().trim();
        address = edtStudentAddressProfile.getText().toString().trim();
        cmnd= edtCardCodeStudentProfile.getText().toString().trim();
        phone = edtPhoneStudentProfile.getText().toString().trim();
        if (name.isEmpty() || birthday.isEmpty()|| address.isEmpty() || cmnd.isEmpty() || phone.isEmpty() ){
            Toast.makeText(Profile.this, getString(R.string.enter_full_information), Toast.LENGTH_LONG).show();
        }
        else {

                saveImageToStorageAndDownloadUri();
        }
    }
    private void saveImageToStorageAndDownloadUri(){
        progressDialog = new ProgressDialog(Profile.this);
        progressDialog.show();
//        String path = pathBookImageInStorage+bookCode+".png";
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://library-80e61.appspot.com");
        StorageReference storageRef = storage.getReference();
// Create a reference to "mountains.jpg"
        final StorageReference mountainsRef = storageRef.child("image/"+accountStudentsDetails.getStudentCode()+".jpg");
        imageProfile.setDrawingCacheEnabled(true);
        imageProfile.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageProfile.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        final byte[] data = baos.toByteArray();
        final UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw Objects.requireNonNull(task.getException());
                        }
                        return mountainsRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            uriDownload = task.getResult();
                            addDataToFireBaseDataBase(uriDownload.toString());
                        } else {
                            Toast.makeText(Profile.this, "Download uri fail",Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();

                        }
                    }
                });
            }
        });
    }
    private void  addDataToFireBaseDataBase(String uri){
        Long phoneNumber = Long.parseLong(phone);
        final HashMap<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("birthDay", birthday);
        map.put("address", address);
        map.put("cmnd", cmnd);
        map.put("phone", phoneNumber);
        map.put("sex", valueSex);
        map.put("image", uri);
        final FirebaseDatabase database = FirebaseDatabase.getInstance("https://library-80e61.firebaseio.com/");
        DatabaseReference reference = database.getReference("Accounts");
        Query query = reference.child("Students").orderByChild("studentCode").equalTo(accountStudentsDetails.getStudentCode());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        dataSnapshot.getRef().updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    progressDialog.dismiss();
                                    Toast.makeText(Profile.this, getString(R.string.update_successful), Toast.LENGTH_LONG).show();
                                }
                                else {
                                    progressDialog.dismiss();
                                    Toast.makeText(Profile.this, getString(R.string.the_update_failed), Toast.LENGTH_LONG).show();
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

}