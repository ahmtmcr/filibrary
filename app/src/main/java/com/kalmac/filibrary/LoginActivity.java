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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    EditText email;
    EditText password;
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
                if (email.getText() != null && !email.getText().toString().equals("") && password.getText() != null && !password.getText().toString().equals("")){
                    mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        Log.d("fire", "signed in suecces");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        switchToMain();
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
}