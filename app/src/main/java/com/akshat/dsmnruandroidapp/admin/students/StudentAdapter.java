package com.akshat.dsmnruandroidapp.admin.students;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akshat.dsmnruandroidapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewAdapter>{

    private List<StudentsData> list;
    private Context context;
    private String category;
    private String key;

    public StudentAdapter(List<StudentsData> list, Context context, String category, String key) {
        this.list = list;
        this.context = context;
        this.category = category;
        this.key = key;
    }

    @NonNull
    @Override
    public StudentViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.students_item_layout,parent,false);

        return new StudentViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewAdapter holder, int position) {
        final StudentsData item = list.get(position);
        holder.name.setText(item.getName());
        holder.email.setText(item.getEmail());
        holder.rollno.setText("Roll No. "+ item.getRollno());
        holder.year.setText(item.getYear()+" Year");
        try {
            Picasso.get().load(item.getImage()).into(holder.image);
        }catch (Exception e){
            Toast.makeText(context, "An Unexpected Error Occured ", Toast.LENGTH_SHORT).show();
        }

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,UpdateSelectedStudentActivity.class);
                intent.putExtra("name",item.getName());
                intent.putExtra("email",item.getEmail());
                intent.putExtra("rollno",item.getRollno());
                intent.putExtra("year",item.getYear());
                intent.putExtra("image",item.getImage());
                intent.putExtra("category",item.getCategory());
                intent.putExtra("key",item.getKey());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class StudentViewAdapter extends RecyclerView.ViewHolder {

        private TextView name, email, rollno, year;
        private Button update;
        private ImageView image;


        public StudentViewAdapter(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.studentName);
            email = itemView.findViewById(R.id.studentEmail);
            rollno = itemView.findViewById(R.id.studentRollNo);
            year = itemView.findViewById(R.id.studentYear);
            image = itemView.findViewById(R.id.studentImage);
            update = itemView.findViewById(R.id.studentUpdate);
        }
    }
}
