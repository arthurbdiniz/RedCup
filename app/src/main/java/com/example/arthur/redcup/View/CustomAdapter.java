package com.example.arthur.redcup.View;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arthur.redcup.Model.Ticket;

import java.util.ArrayList;
import com.example.arthur.redcup.R;



public class CustomAdapter extends ArrayAdapter<Ticket> implements View.OnClickListener{

    private ArrayList<Ticket> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView ticketTitle;
        TextView ticketLocation;
        TextView ticketPrice;
        ImageView ticketPhoto;
    }

    public CustomAdapter(ArrayList<Ticket> data, Context context) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext=context;
    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        Ticket ticket=(Ticket) object;

        switch (v.getId()){
            case R.id.ticketPhoto:
                Snackbar.make(v, "Estamos trabalhando nisso, em breve estará disponivel!", Snackbar.LENGTH_LONG)
                       .setAction("No action", null).show();
                break;
        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Ticket ticket = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.ticketTitle = (TextView) convertView.findViewById(R.id.ticketTitle);
            viewHolder.ticketPrice = (TextView) convertView.findViewById(R.id.price_number);
            viewHolder.ticketLocation = (TextView) convertView.findViewById(R.id.ticketLocation);

            //viewHolder.cutOff = (double) convertView.findViewById(R.id.version_number);
            // Teacher here
            viewHolder.ticketPhoto = (ImageView) convertView.findViewById(R.id.ticketPhoto);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

//        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition)
//                ? R.anim.down_from_top : R.anim.down_from_top);
//        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.ticketTitle.setText(ticket.getTitle());
        viewHolder.ticketPrice.setText(ticket.getPrice());
        viewHolder.ticketLocation.setText(ticket.getLocation() + " - " + ticket.getUf() + " - " + ticket.getNeighborhood());
        //viewHolder.cutOff.setText(userClass.getCutOff());
        viewHolder.ticketPhoto.setOnClickListener(this);
        viewHolder.ticketPhoto.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }
}