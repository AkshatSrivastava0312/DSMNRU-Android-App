package com.akshat.dsmnruandroidapp.faculty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.akshat.dsmnruandroidapp.LoginHome;
import com.akshat.dsmnruandroidapp.R;
import com.akshat.dsmnruandroidapp.admin.faculty.FacultyAdapter;
import com.akshat.dsmnruandroidapp.admin.faculty.FacultyData;
import com.akshat.dsmnruandroidapp.admin.faculty.UpdateFaculty;
import com.akshat.dsmnruandroidapp.admin.faculty.UpdateSelectedFacultyActivity;
import com.akshat.dsmnruandroidapp.admin.notice.DeleteNotice;
import com.akshat.dsmnruandroidapp.admin.notice.NoticeAdapter;
import com.akshat.dsmnruandroidapp.admin.notice.NoticeData;
import com.akshat.dsmnruandroidapp.faculty.FacultyHome;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class UpdateFacultyAccountInfo extends AppCompatActivity {
    private EditText updateFacultyEmail, updateFacultyPassword;
    private ProgressDialog pd;
    private Button updateFacultyBtn;
    private String email, password, key,img;
    private DatabaseReference ref;
    private StorageReference storageReference;
    private ImageView facultyImageUpdate;
    private Bitmap bitmap = null;
    private final int REQ = 1;
    private String download_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_faculty_account_info);


        Intent i = getIntent();

        email = i.getExtras().getString("email");
        password = i.getExtras().getString("password");
        key = i.getExtras().getString("key");
        img = i.getExtras().getString("image");


        updateFacultyEmail = findViewById(R.id.updateFacultyEmail);
        updateFacultyPassword = findViewById(R.id.updateFacultyPassword);
        facultyImageUpdate = findViewById(R.id.facultyImageUpdate);
        updateFacultyBtn = findViewById(R.id.updateFacultyBtn);
        pd = new ProgressDialog(this);



        ref = FirebaseDatabase.getInstance().getReference().child("Faculty");
        storageReference = FirebaseStorage.getInstance().getReference();


        updateFacultyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });

        facultyImageUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallary();
            }
        });

        updateFacultyEmail.setText(email);
        updateFacultyPassword.setText(password);

        try {
            Picasso.get().load(img).into(facultyImageUpdate);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    private void validate() {
        String e, p;
        e = updateFacultyEmail.getText().toString();
        p = updateFacultyPassword.getText().toString();

        if(e.isEmpty()){
            updateFacultyEmail.setError("Empty");
            updateFacultyEmail.requestFocus();
        }else if(p.isEmpty()) {
            updateFacultyPassword.setError("Empty");
            updateFacultyPassword.requestFocus();
        }else if(bitmap == null){
            pd.setMessage("Please Wait");
            pd.show();
            updateData(img);
        }else{
            pd.setMessage("Please Wait");
            pd.show();
            updateImage();
        }
    }

    private void updateImage() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] finalImage = baos.toByteArray();
        final StorageReference filePath;
        filePath = storageReference.child("Faculty").child(finalImage+"jpg");
        final UploadTask uploadTask = filePath.putBytes(finalImage);
        uploadTask.addOnCompleteListener(this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    download_url = String.valueOf(uri);
                                    updateData(download_url);
                                }
                            });
                        }
                    });
                }else {
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openGallary() {
        Intent pickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImage,REQ);
    }

    private void updateData(String download_url) {

        HashMap hp = new HashMap();
        hp.put("email",updateFacultyEmail.getText().toString());
        hp.put("password",updateFacultyPassword.getText().toString());
        hp.put("image",download_url);

        ref.child(key).updateChildren(hp).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                pd.dismiss();
                Intent intent = new Intent(getApplicationContext(), LoginHome.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Please login with new credentials", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });


    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ && resultCode == RESULT_OK){
            Uri uri = data.getData();
            try{
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            }catch (IOException e){
                e.printStackTrace();
            }
            facultyImageUpdate.setImageBitmap(bitmap);
        }
    }


}