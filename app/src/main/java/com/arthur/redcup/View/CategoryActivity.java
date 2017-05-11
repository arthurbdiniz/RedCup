package com.arthur.redcup.View;

import android.content.Context;
import android.icu.util.ULocale;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arthur.redcup.Model.Category;
import com.arthur.redcup.R;

import java.util.ArrayList;
import java.util.List;



public class CategoryActivity extends AppCompatActivity {


    private ArrayList<Category> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);


        categoryList = new ArrayList<Category>();
        categoryList.add(new Category("Cinema"));
        categoryList.add(new Category("Show"));



        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setAdapter(new Adapter(categoryList, getApplicationContext()));
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

    private class Adapter extends RecyclerView.Adapter {

        private final List<Category> category;
        private Context context;


        public Adapter(List<Category> category, Context context) {
            this.category = category;
            this.context = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(context).inflate(R.layout.category_item, parent, false);
            ViewHolder holder = new ViewHolder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

            ViewHolder holder = (ViewHolder) viewHolder;


            Category category  = categoryList.get(position) ;
            holder.nome.setText(category.getNome());

        }

        @Override
        public int getItemCount() {

            return category.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final TextView nome;



        public ViewHolder(View view) {
            super(view);
            nome = (TextView) view.findViewById(R.id.nome);

        }

    }
}


