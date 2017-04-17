package com.example.arthur.redcup.View;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.arthur.redcup.Model.Ticket;
import com.example.arthur.redcup.Model.User;
import com.example.arthur.redcup.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class TicketActivity extends AppCompatActivity {

    private Ticket userTicket;
    private TextView textViewTitle, textViewDescription, textViewPrice, textViewUserEmail, textViewTelephone;
    private Button deleteTicketButton;
    private FirebaseAuth.AuthStateListener authListener;
    private User userLog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(TicketActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        if (user != null) {
            // Name, email address, and profile photo Url
            //String name = user.getDisplayName();
            String email = user.getEmail();
            String uid = user.getUid();
            //Uri photoUrl = user.getPhotoUrl();

            userLog = new User(uid, email);

        }

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_ticket);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Ticket");

        //Recover Information of ticket from List View
        Intent intent = getIntent();
        userTicket = (Ticket) intent.getSerializableExtra("Ticket");
//        if(userTicket != null){
//            setTitle(userTicket.getTitle());
//        }

        textViewTitle = (TextView) findViewById(R.id.text_view_ticket_title);
        textViewDescription = (TextView) findViewById(R.id.text_view_ticket_description);
        textViewPrice = (TextView) findViewById(R.id.text_view_ticket_price);
        textViewUserEmail = (TextView) findViewById(R.id.text_view_ticket_user_email);
        textViewTelephone = (TextView) findViewById(R.id.text_view_ticket_user_email);
        deleteTicketButton = (Button) findViewById(R.id.button_delete_ticket);

        textViewTitle.setText(userTicket.title);
        textViewDescription.setText(userTicket.description);
        textViewPrice.setText(userTicket.price);
        textViewUserEmail.setText(userTicket.userEmail);
        //textViewTelephone.setText(userTicket.);


        if(userTicket.userId.equals(userLog.getId())){
            deleteTicketButton.setVisibility(View.VISIBLE);
        }else{
            deleteTicketButton.setVisibility(View.GONE);
        }

        deleteTicketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTicket();
                //Snackbar.make(v, "Your ticket was successfully deleted!", Snackbar.LENGTH_LONG).setAction("No action", null).show();
                finish();
            }
        });

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void deleteTicket(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("tickets");
        ref.child(userTicket.ticketId).removeValue();
    }
}
