package com.akshat.dsmnruandroidapp.faculty.classroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akshat.dsmnruandroidapp.R;
import com.akshat.dsmnruandroidapp.faculty.FacultyData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddClassroom extends AppCompatActivity {
    private Button shareBtn, createClassroomBtn;
    private TextView displayClassroomKey, classroomName;
    private LinearLayout linShare;
    private DatabaseReference dbref;
    private ProgressDialog pd;

    private String classroomCreationDate, classroomCreationTime, classroomAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_classroom);

        shareBtn = findViewById(R.id.share);
        classroomName = findViewById(R.id.classroomName);
        displayClassroomKey = findViewById(R.id.displayClassroomKey);
        linShare = findViewById(R.id.shareLinear);
        createClassroomBtn = findViewById(R.id.createClassroomBtn);

        dbref = FirebaseDatabase.getInstance().getReference().child("Classrooms");

        pd = new ProgressDialog(this);

        createClassroomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });



        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = displayClassroomKey.getText().toString();

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String subject = "DSMNRU Android App";
                String body = "Hy User! Please use \"" + id + "\" as the classroom id to join my new classroom \"" + classroomName.getText().toString() + "\"";
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, body);
                startActivity(Intent.createChooser(intent, "Share Classroom ID Using:"));
            }
        });
    }

    private void validate() {
        String className = classroomName.getText().toString();
        pd.setMessage("Please Hold On Until We Create Your Classroom");
        pd.show();
        if(className.isEmpty()){
            pd.dismiss();
            classroomName.setError("Empty");
            classroomName.requestFocus();
        }else {
            createClass();
        }
    }

    private void createClass() {
        final String uniqueKey = dbref.push().getKey();
        final String className = classroomName.getText().toString();

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("E dd MMMM yyyy");
        classroomCreationDate = currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        classroomCreationTime = currentTime.format(calForTime.getTime());

        classroomAdmin = FacultyData.getEMAIL();

        ClassroomData classroomData = new ClassroomData(uniqueKey, className,classroomCreationDate, classroomCreationTime, classroomAdmin);

        dbref.child(className).setValue(classroomData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                pd.dismiss();
                Toast.makeText(AddClassroom.this, "Your classroom is created successfully", Toast.LENGTH_SHORT).show();
                createClassroomBtn.setEnabled(false);
                displayClassroomKey.setText(uniqueKey);
                linShare.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
            }
        });



    }
}