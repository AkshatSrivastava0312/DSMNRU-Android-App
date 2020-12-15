package com.akshat.dsmnruandroidapp.admin.students;

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

public class AddStudents extends AppCompatActivity {

    private ImageView addStudentImage;
    private EditText addStudentName;
    private EditText addStudentEmail;
    private EditText addStudentRollNo;
    private EditText addStudentYear;
    private Spinner addStudentCategory;
    private Button addStudentBtn;
    private final int REQ = 1;
    private Bitmap bitmap = null;
    private String category;
    private String name, email, rollno, year, download_url = "";
    private ProgressDialog pd;
    private StorageReference storageReference;
    private DatabaseReference reference, dbRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_students);


        addStudentImage = findViewById(R.id.addStudentImage);
        addStudentName = findViewById(R.id.addStudentName);
        addStudentEmail = findViewById(R.id.addStudentEmail);
        addStudentRollNo = findViewById(R.id.addStudentRollNo);
        addStudentYear = findViewById(R.id.addStudentYear);
        addStudentCategory = findViewById(R.id.add_student_category);
        addStudentBtn = findViewById(R.id.addStudentBtn);

        pd = new ProgressDialog(this);


        reference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();


        String[] items = new String[]{"Select Category", "Computer Science & Engineering", "Electronics & Communication Engineering", "Civil Engineering", "Mechanical Engineering", "Electrical Engineering"};
        addStudentCategory.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,items));
        addStudentCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = addStudentCategory.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        addStudentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallary();
            }
        });


        addStudentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });
    }

    private void checkValidation() {
        name = addStudentName.getText().toString();
        email = addStudentEmail.getText().toString();
        rollno = addStudentRollNo.getText().toString();
        year = addStudentYear.getText().toString();

        if(name.isEmpty()){
            addStudentName.setError("Empty");
            addStudentName.requestFocus();
        }else if(email.isEmpty()){
            addStudentEmail.setError("Empty");
            addStudentEmail.requestFocus();
        }else if(rollno.isEmpty()) {
            addStudentRollNo.setError("Empty");
            addStudentRollNo.requestFocus();
        }else if(year.isEmpty()){
                addStudentYear.setError("Empty");
                addStudentYear.requestFocus();
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

        dbRef = reference.child("Students");
        final String uniqueKey = reference.push().getKey();

        final String password = generatePassword();


        StudentsData studentsData = new StudentsData(name,email,rollno, year,category,download_url,password,uniqueKey);
        dbRef.child(uniqueKey).setValue(studentsData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                pd.dismiss();
                Toast.makeText(AddStudents.this, "Student Added", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(AddStudents.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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
        filePath = storageReference.child("Students").child(finalImage+"jpg");
        final UploadTask uploadTask = filePath.putBytes(finalImage);
        uploadTask.addOnCompleteListener(AddStudents.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                    Toast.makeText(AddStudents.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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
            addStudentImage.setImageBitmap(bitmap);
        }
    }
}