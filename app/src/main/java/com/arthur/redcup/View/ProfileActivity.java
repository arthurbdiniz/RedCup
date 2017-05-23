package com.arthur.redcup.View;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arthur.redcup.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnChangeEmail, btnChangePassword, btnSendResetEmail, btnRemoveUser,
            changeEmail, changePassword, sendEmail, remove, signOut;

    private EditText oldEmail, newEmail, password, newPassword;
    private ProgressBar progressBar;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private  FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFirebase();
        initToolbar();
        initView();
        setView();
        setClickListeners();
    }

    /**Init Firebase Instance method**/
    public void initFirebase(){
        //get firebase auth instance
        auth = FirebaseAuth.getInstance();

        //get current user
        user = FirebaseAuth.getInstance().getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };
    }

    /**Init Toolbar method**/
    public void initToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_user_config);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    /**Init view finding all items in XML file by ID**/
    public void initView(){
        btnChangeEmail = (Button) findViewById(R.id.change_email_button);
        btnChangePassword = (Button) findViewById(R.id.change_password_button);
        btnSendResetEmail = (Button) findViewById(R.id.sending_pass_reset_button);
        btnRemoveUser = (Button) findViewById(R.id.remove_user_button);
        changeEmail = (Button) findViewById(R.id.changeEmail);
        changePassword = (Button) findViewById(R.id.changePass);
        sendEmail = (Button) findViewById(R.id.send);
        remove = (Button) findViewById(R.id.remove);
        signOut = (Button) findViewById(R.id.sign_out);
        oldEmail = (EditText) findViewById(R.id.old_email);
        newEmail = (EditText) findViewById(R.id.new_email);
        password = (EditText) findViewById(R.id.password);
        newPassword = (EditText) findViewById(R.id.newPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    /**Set view start mode method**/
    public void setView(){
        oldEmail.setVisibility(View.GONE);
        newEmail.setVisibility(View.GONE);
        password.setVisibility(View.GONE);
        newPassword.setVisibility(View.GONE);
        changeEmail.setVisibility(View.GONE);
        changePassword.setVisibility(View.GONE);
        sendEmail.setVisibility(View.GONE);
        remove.setVisibility(View.GONE);
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }

    }

    /**Set Click listeners to all itens who needs callbacks**/
    public void setClickListeners(){
        btnChangeEmail.setOnClickListener(this);
        changeEmail.setOnClickListener(this);
        btnChangePassword.setOnClickListener(this);
        changePassword.setOnClickListener(this);
        btnSendResetEmail.setOnClickListener(this);
        sendEmail.setOnClickListener(this);
        btnRemoveUser.setOnClickListener(this);
        signOut.setOnClickListener(this);
    }

    /**Sign out method**/
    public void signOut() {
        auth.signOut();
    }

    /**Remove user method**/
    public void removeUser(){
        progressBar.setVisibility(View.VISIBLE);
        if (user != null) {
            user.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ProfileActivity.this, getString(R.string.profile_delect), Toast.LENGTH_SHORT).show();
                                //startActivity(new Intent(ProfileActivity.this, SignupActivity.class));
                                finish();
                                progressBar.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(ProfileActivity.this, getString(R.string.failed_profile_delect), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
        }

    }

    /**send email method**/
    public void sendEmail(){
        progressBar.setVisibility(View.VISIBLE);
        if (!oldEmail.getText().toString().trim().equals("")) {
            auth.sendPasswordResetEmail(oldEmail.getText().toString().trim())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ProfileActivity.this, getString(R.string.reset_passaword), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(ProfileActivity.this, getString(R.string.failed_send), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
        } else {
            oldEmail.setError(getString(R.string.inputEmail));
            progressBar.setVisibility(View.GONE);
        }
    }

    /**send email animation method**/
    public void animationSendResetEmail(){
        oldEmail.setVisibility(View.VISIBLE);
        newEmail.setVisibility(View.GONE);
        password.setVisibility(View.GONE);
        newPassword.setVisibility(View.GONE);
        changeEmail.setVisibility(View.GONE);
        changePassword.setVisibility(View.GONE);
        sendEmail.setVisibility(View.VISIBLE);
        remove.setVisibility(View.GONE);
    }

    /**Change password method**/
    public void changePassword(){
        progressBar.setVisibility(View.VISIBLE);
        if (user != null && !newPassword.getText().toString().trim().equals("")) {
            if (newPassword.getText().toString().trim().length() < 6) {
                newPassword.setError(getString(R.string.minimum_password));
                progressBar.setVisibility(View.GONE);
            } else if (newPassword.getText().toString().trim().length() > 16) {
                newPassword.setError(getString(R.string.maximum_password));
                progressBar.setVisibility(View.GONE);
            } else {
                user.updatePassword(newPassword.getText().toString().trim())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ProfileActivity.this, getString(R.string.update_password), Toast.LENGTH_SHORT).show();
                                    signOut();
                                    progressBar.setVisibility(View.GONE);
                                } else {
                                    Toast.makeText(ProfileActivity.this, getString(R.string.failed_password), Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
            }
        } else if (newPassword.getText().toString().trim().equals("")) {
            newPassword.setError(getString(R.string.hint_new_password));
            progressBar.setVisibility(View.GONE);
        }
    }

    /**Change password animation method**/
    public void animationChangePassword(){
        oldEmail.setVisibility(View.GONE);
        newEmail.setVisibility(View.GONE);
        password.setVisibility(View.GONE);
        newPassword.setVisibility(View.VISIBLE);
        changeEmail.setVisibility(View.GONE);
        changePassword.setVisibility(View.VISIBLE);
        sendEmail.setVisibility(View.GONE);
        remove.setVisibility(View.GONE);
    }

    /**Change emaiil method**/
    public void changeEmail(){
        progressBar.setVisibility(View.VISIBLE);
        if (user != null && !newEmail.getText().toString().trim().equals("")) {
            user.updateEmail(newEmail.getText().toString().trim())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ProfileActivity.this, getString(R.string.update_email), Toast.LENGTH_LONG).show();
                                signOut();
                                progressBar.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(ProfileActivity.this, getString(R.string.failed_email), Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
        } else if (newEmail.getText().toString().trim().equals("")) {
            newEmail.setError(getString(R.string.enter_email));
            progressBar.setVisibility(View.GONE);
        }
    }

    /**Change email animation method**/
    public void animationChangeEmail(){
        oldEmail.setVisibility(View.GONE);
        newEmail.setVisibility(View.VISIBLE);
        password.setVisibility(View.GONE);
        newPassword.setVisibility(View.GONE);
        changeEmail.setVisibility(View.VISIBLE);
        changePassword.setVisibility(View.GONE);
        sendEmail.setVisibility(View.GONE);
        remove.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.change_email_button:
                animationChangeEmail();
                break;

            case R.id.changeEmail:
                changeEmail();
                break;

            case R.id.change_password_button:
                animationChangePassword();
                break;

            case R.id.changePass:
                changePassword();
                break;

            case R.id.sending_pass_reset_button:
                animationSendResetEmail();
                break;

            case R.id.send:
                sendEmail();
                break;

            case R.id.remove_user_button:
                removeUser();
                break;

            case R.id.sign_out:
                signOut();
                break;
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }
}
