package com.arthur.redcup.View;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;

import com.arthur.redcup.Model.ImageBitmap;
import com.arthur.redcup.Model.Ticket;
import com.arthur.redcup.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class TicketImageActivity extends AppCompatActivity {

    private ImageView imageViewTicket;
    ImageBitmap imageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_image);
        imageViewTicket = (ImageView) findViewById(R.id.image);

        Intent intent = getIntent();
        imageBitmap = (ImageBitmap) intent.getSerializableExtra("TicketBitmap");

        loadImageFromStorage(imageBitmap.getPath(1));

    }

    private void loadImageFromStorage(String path) {
        try {
            File f=new File(path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));

            imageViewTicket.setImageBitmap(b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
