package com.akshat.dsmnruandroidapp.admin.students;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

import com.akshat.dsmnruandroidapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class UpdateSelectedStudentActivity extends AppCompatActivity {

    private ImageView updateCurrentStudentImage;
    private EditText updateCurrentStudentName,updateCurrentStudentEmail,updateCurrentStudentRollNo, updateCurrentStudentYear;
    private Button updateCurrentStudentBtn,deleteCurrentStudentBtn;
    private final int REQ = 1;
    private Bitmap bitmap = null;

    private StorageReference storageReference;
    private DatabaseReference reference;

    private String download_url;

    private String name,email,image,rollno,year,key,category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_selected_student);

        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        image = getIntent().getStringExtra("image");
        rollno = getIntent().getStringExtra("rollno");
        year = getIntent().getStringExtra("year");
        key = getIntent().getStringExtra("key");
        category = getIntent().getStringExtra("category");
        key = getIntent().getStringExtra("key");

        reference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();


        updateCurrentStudentName = findViewById(R.id.updateCurrentStudentName);
        updateCurrentStudentImage = findViewById(R.id.updateCurrentStudentImage);
        updateCurrentStudentEmail = findViewById(R.id.updateCurrentStudentEmail);
        updateCurrentStudentRollNo = findViewById(R.id.updateCurrentStudentRollNo);
        updateCurrentStudentYear = findViewById(R.id.updateCurrentStudentYear);
        updateCurrentStudentBtn = findViewById(R.id.updateCurrentStudentBtn);
        deleteCurrentStudentBtn = findViewById(R.id.deleteCurrentStudentBtn);


        try {
            Picasso.get().load(image).into(updateCurrentStudentImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        updateCurrentStudentEmail.setText(email);
        updateCurrentStudentName.setText(name);
        updateCurrentStudentRollNo.setText(rollno);
        updateCurrentStudentYear.setText(year);

        updateCurrentStudentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallary();
            }
        });

        updateCurrentStudentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = updateCurrentStudentName.getText().toString();
                email = updateCurrentStudentEmail.getText().toString();
                rollno = updateCurrentStudentRollNo.getText().toString();
                year = updateCurrentStudentYear.getText().toString();
                checkValidation();
            }
        });


        deleteCurrentStudentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData();
            }
        });
    }

    private void deleteData() {
        reference.child("Students").child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(UpdateSelectedStudentActivity.this, "Student Deleted Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UpdateSelectedStudentActivity.this,UpdateStudents.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateSelectedStudentActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void checkValidation() {
        if(name.isEmpty()){
            updateCurrentStudentName.setError("Empty");
            updateCurrentStudentName.requestFocus();
        }else if(email.isEmpty()){
            updateCurrentStudentEmail.setError("Empty");
            updateCurrentStudentEmail.requestFocus();
        }else if(rollno.isEmpty()) {
            updateCurrentStudentRollNo.setError("Empty");
            updateCurrentStudentRollNo.requestFocus();
        }else if(year.isEmpty()){
            updateCurrentStudentYear.setError("Empty");
            updateCurrentStudentYear.requestFocus();
        }else if(bitmap == null) {
            updateData(image);
        }else {
            uploadImage();
        }

    }


    private void uploadImage() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] finalImage = baos.toByteArray();
        final StorageReference filePath;
        filePath = storageReference.child("Students").child(finalImage+"jpg");
        final UploadTask uploadTask = filePath.putBytes(finalImage);
        uploadTask.addOnCompleteListener(UpdateSelectedStudentActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
//                    pd.dismiss();
                    Toast.makeText(UpdateSelectedStudentActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void updateData(String s) {

        HashMap hp = new HashMap();
        hp.put("name",name);
        hp.put("email",email);
        hp.put("rollno",rollno);
        hp.put("year",year);
        hp.put("image",s);



        reference.child("Students").child(key).updateChildren(hp).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(UpdateSelectedStudentActivity.this, "Student Updated Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UpdateSelectedStudentActivity.this,UpdateStudents.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateSelectedStudentActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void openGallary() {
        Intent pickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImage,REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ && resultCode == RESULT_OK){
            Uri uri = data.getData();
            try{
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            }catch (IOException e){
                e.printStackTrace();
            }
            updateCurrentStudentImage.setImageBitmap(bitmap);
        }
    }



}