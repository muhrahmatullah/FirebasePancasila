package com.rahmat.app.firebasepancasila;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;

    private EditText edUserPasswordReg, edUserEmailReg, eddUserPasswordConfirmReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edUserEmailReg = findViewById(R.id.eduseremailreg);
        edUserPasswordReg = findViewById(R.id.eduserpasswordreg);
        eddUserPasswordConfirmReg = findViewById(R.id.edconfirmpasswordreg);

        findViewById(R.id.btnRegister).setOnClickListener(this);
        findViewById(R.id.tologin).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void register(String email, String password){
        if (!validateForm()) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Register", "createUserWithEmail:success");
                    Toast.makeText(getApplicationContext(), "Register Success",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    // If sign in fails, display a message to the user.
                    Log.w("Register", "createUserWithEmail:failure", task.getException());
                    Toast.makeText(getApplicationContext(), "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = edUserEmailReg.getText().toString();
        if (TextUtils.isEmpty(email)) {
            edUserEmailReg.setError("Required.");
            valid = false;
        } else {
            edUserEmailReg.setError(null);
        }

        String password = edUserPasswordReg.getText().toString();
        if (TextUtils.isEmpty(password)) {
            edUserPasswordReg.setError("Required.");
            valid = false;
        } else {
            edUserPasswordReg.setError(null);
        }
        String passwordConfirm = eddUserPasswordConfirmReg.getText().toString();
        if (TextUtils.isEmpty(password) && !passwordConfirm.equals(password)) {
            edUserPasswordReg.setError("Required.");
            valid = false;
        } else {
            edUserPasswordReg.setError(null);
        }

        return valid;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.btnRegister){
            register(edUserEmailReg.getText().toString(), edUserPasswordReg.getText().toString());
        }else{
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
