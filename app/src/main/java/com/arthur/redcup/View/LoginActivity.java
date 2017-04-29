package com.arthur.redcup.View;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arthur.redcup.Model.User;
import com.arthur.redcup.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btnSignup, btnLogin, btnReset;
    private User userLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, NavigationActivity.class));
            finish();
        }

        // set the view now
        setContentView(R.layout.activity_login);


        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnReset = (Button) findViewById(R.id.btn_reset_password);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });



        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {

                    //Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();

                    inputEmail.setError(getString(R.string.inputEmail));

                    return;
                }

                if (TextUtils.isEmpty(password)) {

                    //Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();


                    inputPassword.setError(getString(R.string.inputPassword));

                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds

                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    // There was an error
                                    if (password.length() < 6) {
                                        inputPassword.setError(getString(R.string.minimum_password));

                                    } else if (password.length() > 16){
                                        inputPassword.setError(getString(R.string.maximum_password));
                                    } else {

                                        Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();

                                    }
                                } else {
                                    //User user = new User(auth.getCurrentUser().toString(),"", email, password);
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    if (user != null) {
                                        // Name, email address, and profile photo Url
                                        //String name = user.getDisplayName();
                                        String email = user.getEmail();
                                        String uid = user.getUid();
                                        //Uri photoUrl = user.getPhotoUrl();

                                        // Check if user's email is verified
                                        //boolean emailVerified = user.isEmailVerified();

                                       userLog = new User(uid, email);

                                    }

                                    Intent goNavigation = new  Intent(getApplicationContext(), NavigationActivity.class);
                                    goNavigation.putExtra("User", userLog);
                                    startActivity(goNavigation);

                                    finish();
                                }
                            }
                        });
            }
        });
    }




}
