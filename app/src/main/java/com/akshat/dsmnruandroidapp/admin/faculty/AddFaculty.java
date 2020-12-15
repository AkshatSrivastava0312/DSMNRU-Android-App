package com.akshat.dsmnruandroidapp.admin.faculty;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddFaculty extends AppCompatActivity {

    private ImageView addFacultyImage;
    private EditText addFacultyName;
    private EditText addFacultyEmail;
    private EditText addFacultyPost;
    private Spinner addFacultyCategory;
    private Button addFacultyBtn;
    private final int REQ = 1;
    private Bitmap bitmap = null;
    private String category;
    private String name, email, post, download_url = "";
    private ProgressDialog pd;
    private StorageReference storageReference;
    private DatabaseReference reference, dbRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_faculty);


        addFacultyImage = findViewById(R.id.addFacultyImage);
        addFacultyName = findViewById(R.id.addFacultyName);
        addFacultyEmail = findViewById(R.id.addFacultyEmail);
        addFacultyPost = findViewById(R.id.addFacultyPost);
        addFacultyCategory = findViewById(R.id.add_faculty_category);
        addFacultyBtn = findViewById(R.id.addFacultyBtn);

        pd = new ProgressDialog(this);


        reference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();


        String[] items = new String[]{"Select Category", "Computer Science & Engineering", "Electronics & Communication Engineering", "Civil Engineering", "Mechanical Engineering", "Electrical Engineering"};
        addFacultyCategory.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,items));
        addFacultyCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = addFacultyCategory.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        addFacultyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallary();
            }
        });


        addFacultyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });
    }

    private void checkValidation() {
        name = addFacultyName.getText().toString();
        email = addFacultyEmail.getText().toString();
        post = addFacultyPost.getText().toString();

        if(name.isEmpty()){
            addFacultyName.setError("Empty");
            addFacultyName.requestFocus();
        }else if(email.isEmpty()){
            addFacultyEmail.setError("Empty");
            addFacultyEmail.requestFocus();
        }else if(post.isEmpty()){
            addFacultyPost.setError("Empty");
            addFacultyPost.requestFocus();
        }else if(category.equals("Select Category")){
            Toast.makeText(this, "Please Select a Category", Toast.LENGTH_SHORT).show();
        }else if(bitmap == null){
            pd.setMessage("Uploading");
            pd.show();
            uploadData();
        }else{
            pd.setMessage("Uploading");
            pd.show();
            uploadImage();
        }
    }

    private void uploadData() {
        if(bitmap == null)
        {
            download_url = "https://firebasestorage.googleapis.com/v0/b/dsmnru-android-app-2ab44.appspot.com/o/Faculty%2Ffaculty.png?alt=media&token=fbe730e4-e937-4e1f-9e34-68d9995a57f0";
        }

        dbRef = reference.child("Faculty");
        final String uniqueKey = reference.push().getKey();
        
        final String password = generatePassword();


        FacultyData facultyData = new FacultyData(name,email,post,category,download_url,password,uniqueKey);
        dbRef.child(uniqueKey).setValue(facultyData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                pd.dismiss();
                Toast.makeText(AddFaculty.this, "Faculty Added", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(AddFaculty.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private String generatePassword() {
        String password = name.replaceAll(" ", "");
        password = password.toLowerCase();
        return password.concat("123");
    }

    private void uploadImage() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] finalImage = baos.toByteArray();
        final StorageReference filePath;
        filePath = storageReference.child("Faculty").child(finalImage+"jpg");
        final UploadTask uploadTask = filePath.putBytes(finalImage);
        uploadTask.addOnCompleteListener(AddFaculty.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                                    uploadData();
                                }
                            });
                        }
                    });
                }else {
                    pd.dismiss();
                    Toast.makeText(AddFaculty.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
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
            addFacultyImage.setImageBitmap(bitmap);
        }
    }
}