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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;




public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener{


    private static final int RC_SIGN_IN = 9001 ;


    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btnSignup, btnLogin, btnReset;
    private User userLog;


    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;

    private com.google.android.gms.common.SignInButton signInButton;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnReset = (Button) findViewById(R.id.btn_reset_password);
        signInButton = (com.google.android.gms.common.SignInButton)findViewById(R.id.sign_in_button);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this , new GoogleApiClient.OnConnectionFailedListener() {
//                    @Override
//                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//                    }
//                } /* OnConnectionFailedListener */)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                //signInButton.setVisibility(View.GONE);
                //signOutButton.setVisibility(View.VISIBLE);
                if (user != null) {
                    // User is signed in
                    //Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    if(user.getDisplayName() != null){
                        //Intent goNavigation = new  Intent(getApplicationContext(), NavigationActivity.class);
                        //goNavigation.putExtra("User", userLog);
                        //startActivity(goNavigation);

                        //finish();



                        //Its right
                    }
                        //nameTextView.setText("HI " + user.getDisplayName().toString());
                    //emailTextView.setText(user.getEmail().toString());
                    Toast.makeText(LoginActivity.this, "Authentication ok.",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "User is Signout",
                            Toast.LENGTH_SHORT).show();
                    // User is signed out
                    //Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });




        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, NavigationActivity.class));
            finish();
        }

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

                    inputEmail.setError(getString(R.string.inputEmail));
                    return;
                }

                if (TextUtils.isEmpty(password)) {

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

    private void signIn() {

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

        mGoogleApiClient.connect();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
                Toast.makeText(LoginActivity.this, "Authentication deu bom.",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(LoginActivity.this, "Authentication deu ruim.",
                        Toast.LENGTH_SHORT).show();
                // Google Sign In failed, update UI appropriately

            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {


        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                            Toast.makeText(LoginActivity.this, "Authentication good.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
    }



    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }








    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }


    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {

                    }
                });
    }

    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {

                    }
                });
    }
}
