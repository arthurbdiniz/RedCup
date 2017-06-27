package com.arthur.redcup.View;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arthur.redcup.Model.Location;
import com.arthur.redcup.R;

import java.util.ArrayList;
import java.util.List;

public class LocationActivity extends AppCompatActivity implements View.OnClickListener{

    private ArrayList<Location> locationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_location );

        locationList = new ArrayList<Location>();

        locationList.add(new Location("AC", "Acre"));
        locationList.add(new Location("AL", "Alagoas"));
        locationList.add(new Location("AP", "Amapá"));
        locationList.add(new Location("AM", "Amazonas"));
        locationList.add(new Location("BA", "Bahia"));
        locationList.add(new Location("CE", "Ceará"));
        locationList.add(new Location("DF", "Distrito Federal"));
        locationList.add(new Location("ES", "Espírito Santo"));
        locationList.add(new Location("GO", "Goiás"));
        locationList.add(new Location("MA", "MArranhão"));
        locationList.add(new Location("MT", "Mato Grosso"));
        locationList.add(new Location("MS", "Mato Grosso do Sul"));
        locationList.add(new Location("MG", "Minas Gerais"));
        locationList.add(new Location("PA", "Pará"));
        locationList.add(new Location("PB", "Paraíba"));
        locationList.add(new Location("PR", "Paraná"));
        locationList.add(new Location("PE", "Pernambuco"));
        locationList.add(new Location("PI", "Piauí"));
        locationList.add(new Location("RJ", "Rio de Janeiro"));
        locationList.add(new Location("RN", "Rio Grande do Norte"));
        locationList.add(new Location("RS", "Rio Grande do Sul"));
        locationList.add(new Location("RO", "Rondônia"));
        locationList.add(new Location("RR", "Roraima"));
        locationList.add(new Location("SC", "Santa Catarina"));
        locationList.add(new Location("SP", "São Paulo"));
        locationList.add(new Location("SE", "Sergipe"));
        locationList.add(new Location("TO", "Tocantins"));

        CardView cardView = (CardView) findViewById(R.id.cardView_location);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_location);
        recyclerView.setAdapter(new Adapter(locationList, getApplicationContext(), recyclerView));
        RecyclerView.LayoutManager layout = new LinearLayoutManager(LocationActivity.this);
        recyclerView.setLayoutManager(layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_location);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Localização");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {}

    private class Adapter extends RecyclerView.Adapter implements View.OnClickListener {

        private final List<Location> location;
        private Context context;
        private RecyclerView recyclerView;

        public Adapter(List<Location> location, Context context, RecyclerView recyclerView) {
            this.location = location;
            this.context = context;
            this.recyclerView = recyclerView;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(context).inflate(R.layout.location_item, parent, false);
            ViewHolder holder = new ViewHolder(view);
            view.setOnClickListener(this);

            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

            LocationActivity.ViewHolder holder = (LocationActivity.ViewHolder) viewHolder;


            Location location  = locationList.get(position) ;
            holder.uf.setText(location.getUf());
            holder.state.setText(location.getState());
        }

        @Override
        public int getItemCount() {
            return location.size();
        }

        @Override
        public void onClick(View v) {

            int itemPosition = recyclerView.getChildLayoutPosition(v);
            Location location = locationList.get(itemPosition);

            Intent intent = new Intent();
            intent.putExtra("Location", location);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final TextView uf;
        final TextView state;

        public ViewHolder(View view) {
            super(view);
            uf = (TextView) view.findViewById(R.id.uf);
            state = (TextView) view.findViewById(R.id.state);
        }

    }
}
