package com.rahmat.app.firebasepancasila;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;

    private EditText edUserPassword, edUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //init view
        edUserEmail = findViewById(R.id.eduseremail);
        edUserPassword = findViewById(R.id.eduserpassword);

        findViewById(R.id.btnLogin).setOnClickListener(this);
        findViewById(R.id.toregister).setOnClickListener(this);

        //init fAuth
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void login(String email, String password){
        if (!validateForm()) {
            return;
        }
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Login", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            navigateToHome();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Login", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            edUserPassword.setText("");
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            //mStatusTextView.setText(R.string.auth_failed);
                        }
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

    @Override
    public void onClick(View v) {
         int i = v.getId();

         switch(i){
             case R.id.btnLogin:
                    login(edUserEmail.getText().toString(), edUserPassword.getText().toString());
                 break;
             case R.id.toregister:
                    Intent in = new Intent(this, RegisterActivity.class);
                    startActivity(in);
                 break;
             default:

                 break;
         }

    }

    private void navigateToHome(){

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = edUserEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            edUserEmail.setError("Required.");
            valid = false;
        } else {
            edUserEmail.setError(null);
        }

        String password = edUserPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            edUserPassword.setError("Required.");
            valid = false;
        } else {
            edUserPassword.setError(null);
        }

        return valid;
    }
}
