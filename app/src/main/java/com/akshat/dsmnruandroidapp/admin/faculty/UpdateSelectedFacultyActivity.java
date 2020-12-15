package com.akshat.dsmnruandroidapp.admin.faculty;

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

public class UpdateSelectedFacultyActivity extends AppCompatActivity {

    private ImageView updateCurrentFacultyImage;
    private EditText updateCurrentFacultyName,updateCurrentFacultyEmail,updateCurrentFacultyPost;
    private Button updateCurrentFacultyBtn,deleteCurrentFacultyBtn;
    private final int REQ = 1;
    private Bitmap bitmap = null;

    private StorageReference storageReference;
    private DatabaseReference reference;

    private String download_url;

    private String name,email,image,post,key,category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_selected_faculty);

        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        image = getIntent().getStringExtra("image");
        post = getIntent().getStringExtra("post");
        key = getIntent().getStringExtra("key");
        category = getIntent().getStringExtra("category");
        key = getIntent().getStringExtra("key");

        reference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();


        updateCurrentFacultyName = findViewById(R.id.updateCurrentFacultyName);
        updateCurrentFacultyImage = findViewById(R.id.updateCurrentFacultyImage);
        updateCurrentFacultyEmail = findViewById(R.id.updateCurrentFacultyEmail);
        updateCurrentFacultyPost = findViewById(R.id.updateCurrentFacultyPost);
        updateCurrentFacultyBtn = findViewById(R.id.updateCurrentFacultyBtn);
        deleteCurrentFacultyBtn = findViewById(R.id.deleteCurrentFacultyBtn);


        try {
            Picasso.get().load(image).into(updateCurrentFacultyImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        updateCurrentFacultyEmail.setText(email);
        updateCurrentFacultyName.setText(name);
        updateCurrentFacultyPost.setText(post);

        updateCurrentFacultyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallary();
            }
        });

        updateCurrentFacultyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = updateCurrentFacultyName.getText().toString();
                email = updateCurrentFacultyEmail.getText().toString();
                post = updateCurrentFacultyPost.getText().toString();
                checkValidation();
            }
        });


        deleteCurrentFacultyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData(); 
            }
        });
    }

    private void deleteData() {
        reference.child("Faculty").child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(UpdateSelectedFacultyActivity.this, "Faculty Deleted Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UpdateSelectedFacultyActivity.this,UpdateFaculty.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateSelectedFacultyActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void checkValidation() {
        if(name.isEmpty()){
            updateCurrentFacultyName.setError("Empty");
            updateCurrentFacultyName.requestFocus();
        }else if(email.isEmpty()){
            updateCurrentFacultyEmail.setError("Empty");
            updateCurrentFacultyEmail.requestFocus();
        }else if(post.isEmpty()){
            updateCurrentFacultyPost.setError("Empty");
            updateCurrentFacultyPost.requestFocus();
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
        filePath = storageReference.child("Faculty").child(finalImage+"jpg");
        final UploadTask uploadTask = filePath.putBytes(finalImage);
        uploadTask.addOnCompleteListener(UpdateSelectedFacultyActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                    Toast.makeText(UpdateSelectedFacultyActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void updateData(String s) {

        HashMap hp = new HashMap();
        hp.put("name",name);
        hp.put("email",email);
        hp.put("post",post);
        hp.put("image",s);



        reference.child("Faculty").child(key).updateChildren(hp).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(UpdateSelectedFacultyActivity.this, "Faculty Updated Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UpdateSelectedFacultyActivity.this,UpdateFaculty.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateSelectedFacultyActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
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
            updateCurrentFacultyImage.setImageBitmap(bitmap);
        }
    }



}