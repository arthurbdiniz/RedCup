package com.arthur.redcup.View;

import android.content.Context;
import android.content.Intent;
import android.icu.util.ULocale;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.arthur.redcup.Model.Category;
import com.arthur.redcup.Model.Ticket;
import com.arthur.redcup.R;

import java.util.ArrayList;
import java.util.List;



public class CategoryActivity extends AppCompatActivity implements View.OnClickListener{


    private ArrayList<Category> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);


        categoryList = new ArrayList<Category>();
        categoryList.add(new Category("Boate/Bar", R.drawable.ic_boate));
        categoryList.add(new Category("Cinema", R.drawable.ic_cinema));
        categoryList.add(new Category("Festa/Show", R.drawable.ic_festa));
        categoryList.add(new Category("Happy Hour", R.drawable.ic_happyhour));
        categoryList.add(new Category("Teatro/Dança", R.drawable.ic_teatro));

        CardView cardView = (CardView) findViewById(R.id.cardView);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setAdapter(new Adapter(categoryList, getApplicationContext(), recyclerView));
        RecyclerView.LayoutManager layout = new GridLayoutManager(CategoryActivity.this, 2);
        recyclerView.setLayoutManager(layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_category);
        setSupportActionBar(toolbar);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Categoria");


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {

    }


    private class Adapter extends RecyclerView.Adapter implements View.OnClickListener {

        private final List<Category> category;
        private Context context;
        private RecyclerView recyclerView;

        public Adapter(List<Category> category, Context context, RecyclerView recyclerView) {
            this.category = category;
            this.context = context;
            this.recyclerView = recyclerView;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(context).inflate(R.layout.category_item, parent, false);
            ViewHolder holder = new ViewHolder(view);
            view.setOnClickListener(this);

            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

            ViewHolder holder = (ViewHolder) viewHolder;


            Category category  = categoryList.get(position) ;
            holder.nome.setText(category.getNome());
            holder.photo.setImageResource(category.getPhoto());
        }

        @Override
        public int getItemCount() {

            return category.size();
        }

        @Override
        public void onClick(View v) {

            int itemPosition = recyclerView.getChildLayoutPosition(v);
            Category category = categoryList.get(itemPosition);

            Intent intent = new Intent();
            intent.putExtra("Category", category);
            setResult(RESULT_OK, intent);
            finish();

//            switch (v.getId()){
//                case R.id.ticketPhoto:
//                    Snackbar.make(v, "Estamos trabalhando nisso, em breve estará disponivel!", Snackbar.LENGTH_LONG)
//                            .setAction("No action", null).show();
//                    break;
//            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final TextView nome;
        final ImageView photo;


        public ViewHolder(View view) {
            super(view);
            nome = (TextView) view.findViewById(R.id.nome);
            photo = (ImageView) view.findViewById(R.id.ticketPhoto);
        }

    }
}


