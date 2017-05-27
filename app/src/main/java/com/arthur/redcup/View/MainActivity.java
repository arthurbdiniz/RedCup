package com.arthur.redcup.View;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.SearchView;

import com.arthur.redcup.Model.User;
import com.arthur.redcup.R;
import com.arthur.redcup.Model.Ticket;
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

import static com.arthur.redcup.R.id.fab;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private DatabaseReference mDatabase;
    private User userLog;
    private FirebaseAuth.AuthStateListener authListener;
    private ProgressBar progressBar;
    private TextView navUserTextView;
    private TicketAdapter adapter;
    private ViewGroup viewGroup;
    private FloatingActionButton createTicketFloatingButton;
    private RecyclerView recyclerView;
    private AppBarLayout appBarLayout;
    final   ArrayList<Ticket> tickets = new ArrayList<Ticket>();


    private  Toolbar toolbar;


    public List<Ticket> listTickets;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        viewGroup = (ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);

        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
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

        initToolbar();
        initAppBarLayout();
        initFloatingButton();
        initDrawerLayout();
        initRecyclerView();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("tickets");
//      DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("tickets");

        progressBar.setVisibility(View.VISIBLE);
        mDatabase.addValueEventListener(new ValueEventListener() {


                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    progressBar.setVisibility(View.GONE);
                    tickets.clear();
                    for (DataSnapshot player : dataSnapshot.getChildren()) {
                        //player.child("title").getValue();
                        //Log.i("player", player.getKey());
                        //friends.add(mDatabase.getKey());
                        Ticket ticket = new Ticket(player.child("title").getValue().toString(),
                                                    player.child("description").getValue().toString(),
                                                    player.child("price").getValue().toString(),
                                                    player.child("CEP").getValue().toString(),
                                                    player.child("userId").getValue().toString(),
                                                    player.child("userEmail").getValue().toString(),
                                                    player.child("userTelephone").getValue().toString(),
                                                    player.child("dateCreation").getValue().toString(),
                                                    player.child("dateExpiration").getValue().toString(),
                                                    player.child("uf").getValue().toString(),
                                                    player.child("location").getValue().toString(),
                                                    player.child("neighborhood").getValue().toString(),
                                                    player.child("category").getValue().toString(),
                                                    player.child("pathImage").getValue().toString());


                        ticket.setTicketId(player.getKey());
                        tickets.add(ticket);

                    }
                    adapter = new TicketAdapter(tickets ,getApplicationContext(), recyclerView);
                    recyclerView.setAdapter(adapter);
                    RecyclerView.LayoutManager layout = new LinearLayoutManager(getApplicationContext(),
                            LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(layout);
                }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


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
        getMenuInflater().inflate(R.menu.navigation, menu);

        MenuItem searchViewItem = menu.findItem(R.id.action_search);
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) searchViewItem.getActionView();
        searchView.setQueryHint("Search");
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);// Do not iconify the widget; expand it by defaul

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                // This is your adapter that will be filtered
                adapter.getFilter().filter(newText);
                return true;
            }

            public boolean onQueryTextSubmit(String query) {

                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long

        switch (item.getItemId()) {
            case R.id.action_search: // should I need to add intent ?
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_create_ticket) {
            startActivity(new Intent(MainActivity.this, CreateTicketActivity.class));
            // Handle the camera action
        }else if (id == R.id.nav_tickets) {
            //Back to main tickets

        }else if (id == R.id.nav_manage) {
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));

        }else if (id == R.id.nav_talk) {
            Snackbar.make(viewGroup , "Em breve voce poderá estar entrando em contato conosco!"  , Snackbar.LENGTH_LONG).setAction("Action", null).show();

        }else if(id == R.id.nav_frequently_questions){
            Snackbar.make(viewGroup , "Estamos trabalhando nessa funcionalidade..."  , Snackbar.LENGTH_LONG).setAction("Action", null).show();

        }else if (id == R.id.nav_share) {
            actionShare();

        }else if(id == R.id.nav_open_google_play){
            actionOpenGooglePLay();

        }else if (id == R.id.nav_use_terms) {

            Snackbar.make(viewGroup , "Estamos trabalhando em nossos Termos de Uso!"  , Snackbar.LENGTH_LONG).setAction("Action", null).show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onClickWhatsApp(View view) {

        PackageManager pm=getPackageManager();
        try {

            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");
            String text = "YOUR TEXT HERE";

            PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            //Check if package exists or not. If not then code
            //in catch block will be called
            waIntent.setPackage("com.whatsapp");

            waIntent.putExtra(Intent.EXTRA_TEXT, text);
            startActivity(Intent.createChooser(waIntent, "Share with"));

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                    .show();
        }

    }


    private void collectPhoneNumbers(Map<String,Object> users) {
        ArrayList<Ticket> tickets = new ArrayList  <Ticket>();
        //ArrayList<Ticket> tickets = new ArrayList<Ticket>();

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()){

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list

            tickets.add(new Ticket( singleUser.get("title").toString(),
                                    singleUser.get("price").toString(),
                                    singleUser.get("description").toString(),
                                    singleUser.get("CEP").toString(),
                                    singleUser.get("userId").toString(),
                                    singleUser.get("userEmail").toString(),
                                    singleUser.get("userTelephone").toString(),
                                    singleUser.get("dateCreation").toString(),
                                    singleUser.get("dateExpiration").toString(),
                                    singleUser.get("uf").toString(),
                                    singleUser.get("location").toString(),
                                    singleUser.get("neighborhood").toString(),
                                    singleUser.get("category").toString(),
                                    singleUser.get("image").toString()));
        }


    }

    private void initToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    private void initFloatingButton(){
        createTicketFloatingButton = (FloatingActionButton) findViewById(fab);

        createTicketFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent goNavigation = new  Intent(getApplicationContext(), CreateTicketActivity.class);
                startActivity(goNavigation);

            }
        });
    }

    private void initDrawerLayout(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View header=navigationView.getHeaderView(0);

        navUserTextView = (TextView) header.findViewById(R.id.text_view_user_nav_name);
        navUserTextView.setText(userLog.email);

        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initAppBarLayout(){
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
    }

    private void initRecyclerView(){
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                hideViews();
            }
            @Override
            public void onShow() {
                showViews();
            }
        });
    }

    public void actionOpenGooglePLay(){

        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }

    }

    public void actionShare(){
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "RedCup");
            String sAux = "\nDeixa eu te recomendar este aplicatico\n\n";
            sAux = sAux + "https://play.google.com/store/apps/details?id=com.arthur.redcup \n\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "choose one"));
        } catch(Exception e) {

        }
    }

    private void hideViews() {
        appBarLayout.animate().translationY(-appBarLayout.getHeight()).setInterpolator(new AccelerateInterpolator(2));
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) createTicketFloatingButton.getLayoutParams();
        int fabBottomMargin = lp.bottomMargin;
        createTicketFloatingButton.animate().translationY(createTicketFloatingButton.getHeight()+fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
    }

    private void showViews() {
        appBarLayout.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        createTicketFloatingButton.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
    }



}
