package com.arthur.redcup.View;


import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arthur.redcup.R;

public class TicketViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener    {

    final TextView nome;
    final TextView preco;
    final TextView location;
    final ImageView photo;

    public TicketViewHolder(View view) {
        super(view);
        view.setOnClickListener(this);
        nome = (TextView) view.findViewById(R.id.ticketTitle);
        preco = (TextView) view.findViewById(R.id.price_number);
        location = (TextView) view.findViewById(R.id.ticketLocation);
        photo = (ImageView) view.findViewById(R.id.ticketPhoto);
    }
    @Override
    public void onClick(View view) {

    }


}
