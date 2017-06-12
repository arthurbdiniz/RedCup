package com.arthur.redcup.View;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.arthur.redcup.Model.Ticket;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import com.arthur.redcup.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class TicketAdapter extends RecyclerView.Adapter implements View.OnClickListener ,Filterable {

    private FirebaseStorage storage = FirebaseStorage.getInstance();

    private ArrayList<Ticket> tickets;
    private ArrayList<Ticket> filteredTickets;
    private ArrayList<Ticket> tempList;
    private FriendFilter friendFilter;
    private RecyclerView recyclerView;
    private Context context;
    private  TicketViewHolder ticketViewHolder;
    private boolean imageTask = false;

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
        ticketViewHolder = (TicketViewHolder) holder;

        final Ticket ticket  = filteredTickets.get(position) ;

        ticketViewHolder.nome.setText(ticket.getTitle());
        ticketViewHolder.preco.setText(ticket.getPrice());
        ticketViewHolder.location.setText(ticket.getLocation() + " - " + ticket.getUf() + " - " + ticket.getNeighborhood());

        byte[] imageAsBytes = Base64.decode(ticket.getPathImage() .getBytes(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        ticketViewHolder.photo.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 330, 330, false));
        //ticketViewHolder.photo.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length)

        //ticketViewHolder.photo.setImageBitmap();
        //previewStoredFirebaseImage(ticket.getPathImage());

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
        byte[] imageAsBytes = Base64.decode(ticket.getPathImage() .getBytes(), Base64.DEFAULT);
        //Bitmap bitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        Intent goTicket = new  Intent(context, TicketActivity.class);
        goTicket.putExtra("picture", imageAsBytes);
        goTicket.putExtra("Ticket", ticket);
        goTicket.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(goTicket);




//        Bundle extras = getIntent().getExtras();
//        Bitmap bmp = (Bitmap) extras.getParcelable("imagebitmap");


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
                    if (ticket.getCategory().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        tempList.add(ticket);
                    }
                    if (ticket.getUf().toLowerCase().contains(constraint.toString().toLowerCase())) {
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

    public void previewStoredFirebaseImage(String imagePath) {
        StorageReference storageRef = storage.getReferenceFromUrl("gs://red-cup.appspot.com").child(imagePath);

        try {
            final File localFile = File.createTempFile("images", "jpg");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    ticketViewHolder.photo.setImageBitmap(bitmap);
                    //mImageView.setImageBitmap(bitmap);
                    imageTask = true;

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    imageTask = true;
                }
            });
        } catch (IOException e ) {}
    }


}

