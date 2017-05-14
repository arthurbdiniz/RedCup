package com.arthur.redcup.View;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.arthur.redcup.Model.Ticket;
import com.arthur.redcup.Model.User;
import com.arthur.redcup.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TicketActivity extends AppCompatActivity {

    private Ticket userTicket;
    private TextView textViewTitle, textViewDescription, textViewPrice, textViewUserEmail, textViewTelephone,
            textViewTicketId, textViewCategory, textViewDateCreation, textViewDateExpiration, textViewUserId, textViewLocation, textViewReport;

    private Button deleteTicketButton;

    private  Intent shareIntent;


    private FirebaseAuth.AuthStateListener authListener;
    private User userLog;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;


    private ShareActionProvider mShareActionProvider;

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

        textViewTitle = (TextView) findViewById(R.id.text_view_ticket_title);
        textViewDescription = (TextView) findViewById(R.id.text_view_ticket_description);
        textViewPrice = (TextView) findViewById(R.id.text_view_ticket_price);
        textViewUserEmail = (TextView) findViewById(R.id.text_view_ticket_user_email);
        textViewTelephone = (TextView) findViewById(R.id.text_view_ticket_user_telephone);
        textViewCategory = (TextView) findViewById(R.id.ticketCategory);
        textViewTicketId = (TextView) findViewById(R.id.text_view_ticket_id);
        textViewUserId = (TextView) findViewById(R.id.text_view_ticket_user_id);
        textViewDateCreation = (TextView) findViewById(R.id.text_view_ticket_date_created);
        textViewDateExpiration = (TextView) findViewById(R.id.text_view_ticket_date_expiration);
        textViewTelephone = (TextView) findViewById(R.id.text_view_ticket_user_telephone);
        textViewLocation = (TextView) findViewById(R.id.ticketLocation);
        textViewReport = (TextView) findViewById(R.id.text_view_report);

        deleteTicketButton = (Button) findViewById(R.id.button_delete_ticket);

        textViewTitle.setText(userTicket.title);
        textViewDescription.setText(userTicket.description);
        textViewPrice.append(userTicket.price);
        textViewUserEmail.append(" " + userTicket.userEmail);
        textViewTicketId.append(" " + userTicket.ticketId);
        textViewDateCreation.append(" " + userTicket.dateCreation);
        textViewDateExpiration.append(" " + userTicket.dateExpiration);
        textViewUserId.append(" " + userTicket.userId);
        textViewTelephone.append(" " + userTicket.userTelephone);
        textViewLocation.setText(userTicket.getLocation() + " - " + userTicket.getUf() + " - " + userTicket.getNeighborhood());
        textViewCategory.setText(userTicket.getCategory());


        if (userTicket.userId.equals(userLog.getId())) {
            deleteTicketButton.setVisibility(View.VISIBLE);
        } else {
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

        textViewReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent send = new Intent(Intent.ACTION_SENDTO);
                String uriText = "mailto:" + Uri.encode("arthurbdiniz@gmail.com") +
                        "?subject=" + Uri.encode("Denuncia ticket:  " + userTicket.ticketId) +
                        "&body=" + Uri.encode("ID usuario denunciado: " + userTicket.userId + "\nMotivo: ");
                Uri uri = Uri.parse(uriText);

                send.setData(uri);
                startActivity(Intent.createChooser(send, "Send mail..."));

            }
        });








    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu resource file.
        //MenuInflater inflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.share_menu, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        mShareActionProvider.setShareIntent(getShareIntent());
        mShareActionProvider.setOnShareTargetSelectedListener(new ShareActionProvider.OnShareTargetSelectedListener() {
            @Override
            public boolean onShareTargetSelected(ShareActionProvider actionProvider, Intent intent) {
//                try {
//                    startActivity(Intent.createChooser(shareIntent, "Compatilhe"));
//                } catch (android.content.ActivityNotFoundException ex) {
//                    Toast.makeText(TicketActivity.this, ".", Toast.LENGTH_SHORT).show();
//                }
                return false;
            }
        });

        // Return true to display menu
        return true;
    }


    private Intent getShareIntent() {
        shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT   , "Ticket: "           +   userTicket.getTitle()      + "\n" +
                                                   "Categoria: "        +   userTicket.getCategory()   + "\n" +
                                                   "Preco: "            +   userTicket.price           + "\n" +
                                                   "Email contato: "    +   userTicket.userEmail       + "\n" +
                                                   "Telefone contato: " +   userTicket.userTelephone   + "\n" );
        //Rest of Atributes

        return shareIntent;
    }
    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {

        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }



    //Snackbar.make(, "Your ticket was successfully deleted!", Snackbar.LENGTH_LONG).setAction("No action", null).show();

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("message/rfc822");
                shareIntent.putExtra(Intent.EXTRA_EMAIL  , new String[]{"recipient@example.com"});
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
                shareIntent.putExtra(Intent.EXTRA_TEXT   , "body of email");
                try {
                    startActivity(Intent.createChooser(shareIntent, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(TicketActivity.this, ".", Toast.LENGTH_SHORT).show();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }



    }

    public void deleteTicket(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("tickets");
        ref.child(userTicket.ticketId).removeValue();
    }

    public String refactorTelephoneNumber(TextView phoneView){
        String phoneFormact;

        phoneFormact = phoneView.getText().toString().replace("(", "");
        return phoneFormact = phoneFormact.replace(")", "");
    }

    //******************************//
    //             PHONE            //
    //******************************//
    private void callPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + refactorTelephoneNumber(textViewTelephone)));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(intent);
        }
    }

    public void makePhoneCall(View view) {

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
        } else {
            callPhone();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callPhone();
                }
            }
        }
    }

    public void sendSMS(View view){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", refactorTelephoneNumber(textViewTelephone), null)));
    }



}
