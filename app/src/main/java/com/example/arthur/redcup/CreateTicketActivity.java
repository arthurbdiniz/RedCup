package com.example.arthur.redcup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreateTicketActivity extends AppCompatActivity {

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private String ticketId;

    private TextView txtDetails;
    private EditText nameTicket;
    private EditText description;
    private EditText price;
    private EditText CPF;
    private Button buttonSend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ticket);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_create_ticket);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Inserir Ticket");


        buttonSend = (Button)findViewById(R.id.button_enviar);

        nameTicket = (EditText) findViewById(R.id.edit_text_title);
        description = (EditText)findViewById(R.id.edit_text_description);
        price = (EditText) findViewById(R.id. editTextPrice);
        CPF = (EditText) findViewById(R.id.editTextCEP);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nameStr = nameTicket.getText().toString();
                String descriptionStr = description.getText().toString();
                String priceStr = price.getText().toString();
                String CEP_Str = CPF.getText().toString();


                // Check for already existed userId
                //if (TextUtils.isEmpty(userId)) {
                    if(createUser(nameStr, descriptionStr, priceStr, CEP_Str)){
                        onBackPressed();
                    }
               // } else {
                  //  updateUser(nameStr, descriptionStr, priceStr, CEP_Str);
                //}
            }
        });

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("tickets");
        mFirebaseInstance.getReference("app_title").setValue("RedCup");

        // app_title change listener
        mFirebaseInstance.getReference("app_title").addValueEventListener(new ValueEventListener() {
            //public static final String TAG = "asd";

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.e(TAG, "App title updated");

                //String appTitle = dataSnapshot.getValue(String.class);

                // update toolbar title
                //getSupportActionBar().setTitle(appTitle);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
              //  Log.e(TAG, "Failed to read app title value.", error.toException());
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private boolean createUser(String title, String description, String price, String CEP) {
        // TODO
        // In real apps this userId should be fetched
        // by implementing firebase auth
        if (TextUtils.isEmpty(ticketId)) {
            ticketId = mFirebaseDatabase.push().getKey();
        }

        Ticket user = new Ticket(title, description, price, CEP);

        mFirebaseDatabase.child(ticketId).setValue(user);

        addUserChangeListener();
        return  true;
    }


    private void addUserChangeListener() {
        // Ticket data change listener
        mFirebaseDatabase.child(ticketId).addValueEventListener(new ValueEventListener() {
            public static final String TAG = "sadasd";

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Ticket user = dataSnapshot.getValue(Ticket.class);

                // Check for null
                if (user == null) {
                    Log.e(TAG, "Ticket data is null!");
                    return;
                }

                //Log.e(TAG, "Ticket data is changed!" + user.name + ", " + user.email);

                // Display newly updated name and email
                //txtDetails.setText(user.name + ", " + user.email);

                // clear edit text
                //inputEmail.setText("");
                //inputName.setText("");

                //toggleButton();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read user", error.toException());
            }
        });
    }


    private void updateUser(String title, String description, String price, String CEP) {
        // updating the user via child nodes
        if (!TextUtils.isEmpty(title))
            mFirebaseDatabase.child(ticketId).child("title").setValue(title);

        if (!TextUtils.isEmpty(price))
            mFirebaseDatabase.child(ticketId).child("price").setValue(price);

        if (!TextUtils.isEmpty(description))
            mFirebaseDatabase.child(ticketId).child("description").setValue(description);

        if (!TextUtils.isEmpty(CEP))
            mFirebaseDatabase.child(ticketId).child("CEP").setValue(CEP);

    }


}
