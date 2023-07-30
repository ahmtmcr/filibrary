package com.kalmac.filibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    TextInputLayout email, password;
    Button signIn;
    TextView goToRegister;

    @Override
    public void onStart(){
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
           switchToMain();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initComponents();
        registerEventHandlers();

        mAuth = FirebaseAuth.getInstance();
    }
    private void switchToMain(){
        Intent loginToMain = new Intent(this, MainActivity.class);
        startActivity(loginToMain);
    }
    private void initComponents(){
        email = findViewById(R.id.mailLogin);
        password = findViewById(R.id.passwordLogin);
        signIn = findViewById(R.id.signInB);
        goToRegister = findViewById(R.id.buttonGoToRegister);
    }
    private void registerEventHandlers(){
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isEmailValid = validateEmail(email);
                boolean isPasswordValid = validatePassword(password);

                if (isEmailValid && isPasswordValid){
                    mAuth.signInWithEmailAndPassword(email.getEditText().getText().toString(), password.getEditText().getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        Log.d("fire", "signed in suecces");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        switchToMain();
                                    } else {
                                        String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                                        switch (errorCode) {

                                            case "ERROR_INVALID_EMAIL":
                                                Toast.makeText(getApplicationContext(), "The email address is badly formatted.", Toast.LENGTH_LONG).show();
                                                email.setError("The email address is badly formatted.");
                                                email.requestFocus();
                                                break;

                                            case "ERROR_WRONG_PASSWORD":
                                                Toast.makeText(getApplicationContext(), "The password is invalid or the user does not have a password.", Toast.LENGTH_LONG).show();
                                                password.setError("Password is incorrect ");
                                                password.requestFocus();
                                                password.getEditText().setText("");
                                                break;

                                            case "ERROR_USER_NOT_FOUND":
                                                Toast.makeText(getApplicationContext(), "User not found.", Toast.LENGTH_LONG).show();
                                                break;
                                        }
                                    }

                                }
                            });
                }
            }
        });
        goToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToRegister();
            }
        });
    }
    private void switchToRegister(){
        Intent loginToMain = new Intent(this, LoginAndRegisterActivity.class);
        startActivity(loginToMain);
    }

    private boolean validateEmail(TextInputLayout wrapper) {
        String input = wrapper.getEditText().getText().toString();
        return android.util.Patterns.EMAIL_ADDRESS.matcher(input).matches();
    }

    private boolean validatePassword(TextInputLayout wrapper) {
         String input = wrapper.getEditText().getText().toString();
         if (input.equals("") || input == null){
             return false;
         }
         return true;
    }
}