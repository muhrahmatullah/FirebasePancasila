package com.rahmat.app.firebasepancasila.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rahmat.app.firebasepancasila.R;
import com.rahmat.app.firebasepancasila.model.Student;

import java.util.List;


public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder>{

    Context context;
    List<Student> student;


    public StudentAdapter(Context context, List<Student> student) {
        this.context = context;
        this.student = student;
    }

    @Override
    public StudentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView;

        rootView = LayoutInflater.from(context).inflate(R.layout.item_student, parent, false);

        return new StudentViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(StudentViewHolder holder, int position) {

        Student s = student.get(position);
        holder.tvname.setText(s.getName());
        holder.tvmajor.setText(s.getMajor());
        holder.tvuniv.setText(s.getUniversity());

    }

    @Override
    public int getItemCount() {
        return student.size();
    }

    class StudentViewHolder extends RecyclerView.ViewHolder{
        TextView tvname, tvmajor, tvuniv;


        public StudentViewHolder(View itemView) {
            super(itemView);

            tvname = itemView.findViewById(R.id.txt_name);
            tvmajor = itemView.findViewById(R.id.txt_major);
            tvuniv = itemView.findViewById(R.id.txt_univ);

        }
    }



}
