package com.rahmat.app.firebasepancasila;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rahmat.app.firebasepancasila.adapter.StudentAdapter;
import com.rahmat.app.firebasepancasila.model.Student;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "EmailPassword";

    private ProgressDialog pDialog;

    private TextView mStatusTextView;
    private TextView mEmailTextView;

    private RecyclerView mRecyclerView;
    private StudentAdapter adapter;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Views
        mStatusTextView = findViewById(R.id.dispUserStatus);
        mEmailTextView = findViewById(R.id.dispUserEmail);
        mRecyclerView = findViewById(R.id.studentlist);

        // Buttons
        findViewById(R.id.btnVerify).setOnClickListener(this);
        findViewById(R.id.btnLogOut).setOnClickListener(this);
        findViewById(R.id.btnSend).setOnClickListener(this);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("loading..");

        //prepareList();



        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, 1, false));

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Student> studentList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.child("students").getChildren()){
                    Student students = snapshot.getValue(Student.class);
                    studentList.add(students);
                }
                adapter = new StudentAdapter(MainActivity.this, studentList);
                mRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "something went wrong",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showProgressDialog(){
        pDialog.show();
    }

    private void hideProgressDialog(){
        pDialog.hide();
        pDialog.dismiss();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //check if user is singed in (non null)
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    private void signOut() {
        mAuth.signOut();
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    private void sendEmailVerification() {
        // Disable button
        findViewById(R.id.btnVerify).setEnabled(false);

        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        // Re-enable button
                        findViewById(R.id.btnVerify).setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(MainActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END send_email_verification]
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            mEmailTextView.setText(getString(R.string.emailpassword_fmt,
                    user.getEmail()));
            mStatusTextView.setText(getString(R.string.status_fmt, user.isEmailVerified()));

        } else {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
        }
    }

    private void addData(){

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add, null);
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Enter New Student");
        alertDialog.setCancelable(false);

        final EditText edname = view.findViewById(R.id.stu_name);
        final EditText edmajor = view.findViewById(R.id.stu_major);
        final EditText eduniv = view.findViewById(R.id.stu_univ);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = edname.getText().toString();
                String major = edmajor.getText().toString();
                String univ = eduniv.getText().toString();

                if(name.isEmpty()){
                    edname.setError("Must be filled");
                }else if(major.isEmpty()){
                    edmajor.setError("Must be filled");
                }else if(univ.isEmpty()){
                    eduniv.setError("Must be filled");
                }else {
                    databaseReference
                            .child("students")
                            .child("student" + name.charAt(0) + name.charAt(3) + univ.charAt(0) + univ.charAt(1))
                            .child("name")
                            .setValue(name);
                    databaseReference
                            .child("students")
                            .child("student" + name.charAt(0) + name.charAt(3) + univ.charAt(0) + univ.charAt(1))
                            .child("major")
                            .setValue(major);
                    databaseReference
                            .child("students")
                            .child("student" + name.charAt(0) + name.charAt(3) + univ.charAt(0) + univ.charAt(1))
                            .child("university")
                            .setValue(univ);
                }
                Toast.makeText(getApplicationContext(), "successfully saved", Toast.LENGTH_SHORT).show();

            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.setView(view);
        alertDialog.show();


    }

    @Override
    public void onClick(View v) {

        int i = v.getId();
        if (i == R.id.btnLogOut) {
            signOut();
        } else if (i == R.id.btnVerify) {
            sendEmailVerification();
        }else if (i == R.id.btnSend){
            addData();
        }

    }
}
