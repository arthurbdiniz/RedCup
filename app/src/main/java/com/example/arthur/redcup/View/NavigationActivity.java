package com.example.arthur.redcup.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.arthur.redcup.Model.User;
import com.example.arthur.redcup.R;
import com.example.arthur.redcup.Model.Ticket;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private DatabaseReference mDatabase;
    private ListView mListView;
    private User userLog;
    private FirebaseAuth.AuthStateListener authListener;

    public ArrayList<Ticket> listTickets;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(NavigationActivity.this, LoginActivity.class));
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

        setTitle(userLog.getEmail());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton createTicketFloatingButton = (FloatingActionButton) findViewById(R.id.fab);

        createTicketFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent goNavigation = new  Intent(getApplicationContext(), CreateTicketActivity.class);
                startActivity(goNavigation);

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("tickets");

//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("tickets");

        final ArrayList<Ticket> friends = new ArrayList<Ticket>();
        //friends.clear();

        final ListView listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent goTicket = new  Intent(getApplicationContext(), TicketActivity.class);
                Ticket userTicketCalled = (Ticket) listView.getItemAtPosition(position);
                goTicket.putExtra("Ticket", userTicketCalled);

                startActivity(goTicket);

                //UserClass userClasses= userClasses.get(position);

            }
        });

        mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    friends.clear();
                    for (DataSnapshot player : dataSnapshot.getChildren()) {
                        //player.child("title").getValue();
                        //Log.i("player", player.getKey());
                        //friends.add(mDatabase.getKey());
                        Ticket ticket = new Ticket(player.child("title").getValue().toString(),
                                                        player.child("description").getValue().toString(),
                                                            player.child("price").getValue().toString(),
                                                                player.child("CEP").getValue().toString(),
                                                                   player.child("userId").getValue().toString(),
                                                                        player.child("userEmail").getValue().toString());
                        ticket.setTicketId(player.getKey());
                        //friends.add(player.child("title").getValue().toString());
                        friends.add(ticket);

                    }
                    ArrayAdapter arrayAdapter = new ArrayAdapter(NavigationActivity.this, android.R.layout.simple_list_item_1, friends);
                    listView.setAdapter(arrayAdapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
        });
        //adapter.notifyDataSetChanged();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent intent = new Intent(NavigationActivity.this, CreateTicketActivity.class);
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {
            startActivity(new Intent(NavigationActivity.this, MainActivity.class));


        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void collectPhoneNumbers(Map<String,Object> users) {
        List<Ticket> tickets = new ArrayList  <Ticket>();
        //ArrayList<Ticket> tickets = new ArrayList<Ticket>();

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()){

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list

            tickets.add(new Ticket(singleUser.get("title").toString(),
                                        singleUser.get("price").toString(),
                                            singleUser.get("description").toString(),
                                                singleUser.get("CEP").toString(),
                                                    singleUser.get("userId").toString(),
                                                        singleUser.get("userEmail").toString()));
        }


    }



}
