package com.kalmac.filibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginAndRegisterActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore firebaseDatabase;


    private TextInputLayout email, password, username, confirmPassword;
    private TextView goToLogin;
    private Button loginAndRegisterButton;

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
        setContentView(R.layout.activity_login_register);

        initComponents();
        registerEventHandlers();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        firebaseDatabase = FirebaseFirestore.getInstance();

    }

    private void swtichToLogin(){
        Intent switchToLogin = new Intent(this, LoginActivity.class);
        startActivity(switchToLogin);
    }
    private void switchToMain(){
        Intent loginToMain = new Intent(this, MainActivity.class);
        startActivity(loginToMain);
    }
    private void createUserDocument(){
        firebaseDatabase = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        CollectionReference users = firebaseDatabase.collection("users");

        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("username", username.getEditText().getText().toString());
        userDetails.put("liked_film_ids", Arrays.asList());
        users.document(currentUser.getUid()).set(userDetails);


    }
    private void initComponents(){
        email = findViewById(R.id.emailLayoutInput);
        password = findViewById(R.id.passwordLayoutInput);
        username = findViewById(R.id.usernameLayoutInput);
        loginAndRegisterButton = findViewById(R.id.login_Register);
        goToLogin = findViewById(R.id.buttonGoToLogin);
        confirmPassword = findViewById(R.id.confirmPasswordLayoutInput);
    }
    private void registerEventHandlers(){
        loginAndRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isEmailValid = validateEmail(email);
                boolean isUsernameValid = validateUsername(username);
                boolean isPasswordsValid = validatePassword(password, confirmPassword);


                setValidatePassword(password, confirmPassword);

                if (!isEmailValid)
                    email.setError("Email is incorrect");
                else
                    email.setError(null);

                if (!isUsernameValid)
                    username.setError("Username is incorrect");
                else
                    username.setError(null);


                if (isEmailValid && isUsernameValid && isPasswordsValid){
                    mAuth.createUserWithEmailAndPassword(email.getEditText().getText().toString(), password.getEditText().getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("fire", "user created succes");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        UserProfileChangeRequest profileNameUpdate = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(username.getEditText().getText().toString())
                                                .build();
                                        user.updateProfile(profileNameUpdate)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Log.d("fire", "profile updated");
                                                        }
                                                    }
                                                });
                                        createUserDocument();
                                        switchToMain();
                                    } else if (task.getException() instanceof FirebaseAuthUserCollisionException)
                                    {
                                        Toast.makeText(LoginAndRegisterActivity.this, "Email is aldready registered", Toast.LENGTH_SHORT).show();

                                    }else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                        Toast.makeText(LoginAndRegisterActivity.this, "Email format is incorrect", Toast.LENGTH_SHORT).show();

                                    }else if (task.getException() instanceof FirebaseAuthWeakPasswordException) {
                                        Toast.makeText(LoginAndRegisterActivity.this, "Password is weak", Toast.LENGTH_SHORT).show();
                                    }else
                                    {
                                        Toast.makeText(LoginAndRegisterActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }


            }
        });

        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swtichToLogin();
            }
        });
    }

    private boolean validateEmail(TextInputLayout wrapper) {
        String input = wrapper.getEditText().getText().toString();
        return android.util.Patterns.EMAIL_ADDRESS.matcher(input).matches();
    }
    private boolean validateUsername(TextInputLayout wrapper){
        String input = wrapper.getEditText().getText().toString();
        if (input.equals("") || input == null){
            return false;
        }

        if (input.length() > 10 || input.length() <= 0) {
            return false;
        }
        return true;
    }
    private void setValidatePassword(TextInputLayout password, TextInputLayout confirmPassword){
        String passwordText = password.getEditText().getText().toString();
        String confirmPasswordText = confirmPassword.getEditText().getText().toString();

        if (passwordText == null || passwordText.equals("")){
            password.setError("Please enter a password");
        }
        else if (passwordText.length() < 8){
            password.setError("Password must have at least 8 characters");
        }else {
            password.setError(null);
        }
        if (!confirmPasswordText.equals(passwordText)){
            confirmPassword.setError("Passwords do not match");
        }
        else {
            confirmPassword.setError(null);
        }
    }
    private boolean validatePassword(TextInputLayout password, TextInputLayout confirmPassword) {
        String passwordText = password.getEditText().getText().toString();
        String confirmPasswordText = confirmPassword.getEditText().getText().toString();
        if (passwordText != null && !passwordText.equals("") &&
                passwordText.length() >= 8 && confirmPasswordText.equals(passwordText)){
            return true;
        }
        return false;
    }
}