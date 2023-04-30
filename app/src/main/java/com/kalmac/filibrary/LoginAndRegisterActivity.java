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
    FirebaseUser currentUser;
    FirebaseFirestore firebaseDatabase;


    EditText email;
    EditText password;
    EditText username;
    EditText confirmPassword;

    TextView goToLogin;
    Button loginAndRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        firebaseDatabase = FirebaseFirestore.getInstance();

        if(currentUser != null){
            setContentView(R.layout.activity_main);
        }
        setContentView(R.layout.activity_login_register);



        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        username = findViewById(R.id.username);
        loginAndRegisterButton = findViewById(R.id.loginAndRegister);
        goToLogin = findViewById(R.id.buttonGoToLogin);
        confirmPassword = findViewById(R.id.confirmPassword);






        loginAndRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (email.getText() != null && password.getText() != null && username.getText() != null && !email.getText().toString().equals("") && !password.getText().toString().equals("") && !username.getText().toString().equals("") && !confirmPassword.getText().toString().equals("") && confirmPassword.getText() != null){

                    if (confirmPassword.getText().toString().equals(password.getText().toString())){
                        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()){
                                            Log.d("fire", "createUserWithEmail:success");
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            UserProfileChangeRequest profileNameUpdate = new UserProfileChangeRequest.Builder()
                                                    .setDisplayName(username.getText().toString())
                                                    .build();
                                            user.updateProfile(profileNameUpdate)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Log.d("fire", "User profile updated.");
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
                    } else {
                        Toast.makeText(LoginAndRegisterActivity.this, "Password are not a match.", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(LoginAndRegisterActivity.this, "Enter required fields.", Toast.LENGTH_SHORT).show();
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
        userDetails.put("username", username.getText().toString());
        userDetails.put("liked_film_ids", Arrays.asList());
        users.document(currentUser.getUid()).set(userDetails);


    }
}