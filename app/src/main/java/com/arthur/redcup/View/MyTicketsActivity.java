package com.arthur.redcup.View;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.arthur.redcup.Model.Category;
import com.arthur.redcup.Model.Location;
import com.arthur.redcup.Model.Ticket;
import com.arthur.redcup.R;

import java.util.ArrayList;

public class MyTicketsActivity extends AppCompatActivity {

    private ArrayList<Ticket> userTickets =  null;
    private RecyclerView recyclerView;
    private TicketAdapter adapter;
    private AppBarLayout appBarLayout;
    private LinearLayout noTicketLayout;
    private Button createTicket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tickets);
        initToolbar();
        initAppBarLayout();
        initRecyclerView();

        noTicketLayout = (LinearLayout) findViewById(R.id.no_ticket_layout);
        createTicket = (Button) findViewById(R.id.create_ticket_button);


        if(userTickets != null){
            userTickets.clear();
        }

        userTickets = (ArrayList<Ticket>) getIntent().getExtras().getSerializable("userTickets");
        if(userTickets.size() == 0){
            recyclerView.setVisibility(View.GONE);
            noTicketLayout.setVisibility(View.VISIBLE);
        }


        createTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(MyTicketsActivity.this, CreateTicketActivity.class));
            }
        });

        adapter = new TicketAdapter(userTickets ,getApplicationContext(), recyclerView);

        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layout);


    }

    public void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.title_my_tickets));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            userTickets = (ArrayList<Ticket>) data.getSerializableExtra("Category");
        }
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
    private void initAppBarLayout(){
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
    }

    private void hideViews() {
        appBarLayout.animate().translationY(-appBarLayout.getHeight()).setInterpolator(new AccelerateInterpolator(2));
    }

    private void showViews() {
        appBarLayout.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
    }


}

