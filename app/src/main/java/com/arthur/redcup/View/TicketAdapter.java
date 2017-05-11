package com.arthur.redcup.View;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import com.arthur.redcup.Model.Ticket;
import java.util.ArrayList;
import com.arthur.redcup.R;



public class TicketAdapter extends RecyclerView.Adapter implements View.OnClickListener ,Filterable {

    private ArrayList<Ticket> tickets;
    private ArrayList<Ticket> filteredTickets;
    private ArrayList<Ticket> tempList;
    private FriendFilter friendFilter;
    private RecyclerView recyclerView;

    private Context context;


    public TicketAdapter(ArrayList<Ticket> tickets, Context context, RecyclerView recyclerView) {
        this.tickets = tickets;
        this.filteredTickets = tickets;
        this.context = context;
        this.recyclerView = recyclerView;

        getFilter();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.ticket_item, parent, false);


        TicketViewHolder ticketViewHolder = new TicketViewHolder(view);
        view.setOnClickListener(this);


        return ticketViewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        TicketViewHolder ticketViewHolder = (TicketViewHolder) holder;

        final Ticket ticket  = filteredTickets.get(position) ;

        ticketViewHolder.nome.setText(ticket.getTitle());
        ticketViewHolder.preco.setText(ticket.getPrice());
        ticketViewHolder.location.setText(ticket.getLocation() + " - " + ticket.getUf() + " - " + ticket.getNeighborhood());

    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }



    @Override
    public int getItemCount() {
        return filteredTickets.size();
    }


    @Override
    public void onClick(View v) {

        int itemPosition = recyclerView.getChildLayoutPosition(v);
        Ticket ticket = tickets.get(itemPosition);


        Intent goTicket = new  Intent(context, TicketActivity.class);
        goTicket.putExtra("Ticket", ticket);
        context.startActivity(goTicket);

        switch (v.getId()){
            case R.id.ticketPhoto:
                Snackbar.make(v, "Estamos trabalhando nisso, em breve estará disponivel!", Snackbar.LENGTH_LONG)
                       .setAction("No action", null).show();
                break;
        }
    }


    @Override
    public Filter getFilter() {
        if (friendFilter == null) {
            friendFilter = new FriendFilter();
        }

        return friendFilter;
    }

    private class FriendFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            tempList = new ArrayList<Ticket>();
            if (constraint!=null && constraint.length()>0) {
                // search content in friend list
                for (Ticket ticket : tickets) {
                    if (ticket.getTitle().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        tempList.add(ticket);
                    }
                }

                filterResults.count = tempList.size();
                filterResults.values = tempList;
            } else {
                filterResults.count = tickets.size();
                filterResults.values = tickets;
            }

            return filterResults;
        }


        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredTickets = (ArrayList<Ticket>) results.values;
            notifyDataSetChanged();
        }
    }
}

