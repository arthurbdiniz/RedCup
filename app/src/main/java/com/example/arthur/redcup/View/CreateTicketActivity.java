package com.example.arthur.redcup.View;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arthur.redcup.Model.EventData;
import com.example.arthur.redcup.Model.User;
import com.example.arthur.redcup.R;
import com.example.arthur.redcup.Model.Ticket;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



import static android.R.attr.data;

public class CreateTicketActivity extends AppCompatActivity  {

    private static final int SELECT_FILE = 1;
    private static final int REQUEST_CAMERA = 0;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private String ticketId;

    private EditText nameTicket;
    private EditText description;
    private EditText price;
    private EditText telephone;
    private EditText CPF;

    private TextView expirationDateView;
    private TextView expirationTimeView;



    private Button buttonSend;
    private Button buttonCategory;
    private Button buttonDate;

    private ImageButton buttonCamera;

    private Intent intentRecover;
    private FirebaseAuth.AuthStateListener authListener;

    private User userLog;



    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;
    public String dateExpiration;




    private String userChoosenTask;

    private static final int TAKE_PHOTO_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ticket);

        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(CreateTicketActivity.this, LoginActivity.class));
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


        //DATE
        //dateView = (TextView) findViewById(R.id.textView3);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);





        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_create_ticket);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Inserir Ticket");


        buttonSend = (Button)findViewById(R.id.button_enviar);

        nameTicket = (EditText) findViewById(R.id.edit_text_title);
        description = (EditText)findViewById(R.id.edit_text_description);
        price = (EditText) findViewById(R.id. editTextPrice);
        CPF = (EditText) findViewById(R.id.editTextCEP);
        buttonCamera = (ImageButton) findViewById(R.id.camera_btn);
        buttonCategory = (Button) findViewById(R.id.button_category);
        buttonDate = (Button) findViewById(R.id.button_date);

        expirationDateView = (TextView) findViewById(R.id.text_view_event_date);
        expirationTimeView = (TextView) findViewById(R.id.text_view_event_time);

        telephone = (EditText) findViewById(R.id.editTextTelephone);
        telephone.addTextChangedListener(new PhoneNumberFormattingTextWatcher() {
            //we need to know if the user is erasing or inputing some new character
            private boolean backspacingFlag = false;
            //we need to block the :afterTextChanges method to be called again after we just replaced the EditText text
            private boolean editedFlag = false;
            //we need to mark the cursor position and restore it after the edition
            private int cursorComplement;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //we store the cursor local relative to the end of the string in the EditText before the edition
                cursorComplement = s.length()-telephone.getSelectionStart();
                //we check if the user ir inputing or erasing a character
                if (count > after) {
                    backspacingFlag = true;
                } else {
                    backspacingFlag = false;
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // nothing to do here =D
            }

            @Override
            public void afterTextChanged(Editable s) {
                String string = s.toString();
                //what matters are the phone digits beneath the mask, so we always work with a raw string with only digits
                String phone = string.replaceAll("[^\\d]", "");

                //if the text was just edited, :afterTextChanged is called another time... so we need to verify the flag of edition
                //if the flag is false, this is a original user-typed entry. so we go on and do some magic
                if (!editedFlag) {

                    //we start verifying the worst case, many characters mask need to be added
                    //example: 999999999 <- 6+ digits already typed
                    // masked: (999) 999-999
                    if (phone.length() >= 6 && !backspacingFlag) {
                        //we will edit. next call on this textWatcher will be ignored
                        editedFlag = true;
                        //here is the core. we substring the raw digits and add the mask as convenient
                        String ans = "(" + phone.substring(0, 3) + ") " + phone.substring(3,6) + "-" + phone.substring(6);
                        telephone.setText(ans);
                        //we deliver the cursor to its original position relative to the end of the string
                        telephone.setSelection(telephone.getText().length()-cursorComplement);

                        //we end at the most simple case, when just one character mask is needed
                        //example: 99999 <- 3+ digits already typed
                        // masked: (999) 99
                    } else if (phone.length() >= 3 && !backspacingFlag) {
                        editedFlag = true;
                        String ans = "(" +phone.substring(0, 3) + ") " + phone.substring(3);
                        telephone.setText(ans);
                        telephone.setSelection(telephone.getText().length()-cursorComplement);
                    }
                    // We just edited the field, ignoring this cicle of the watcher and getting ready for the next
                } else {
                    editedFlag = false;
                }
            }
        });


        buttonSend.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {

                String nameStr = nameTicket.getText().toString();
                String descriptionStr = description.getText().toString();
                String priceStr = price.getText().toString();
                String CEP_Str = CPF.getText().toString();
                String userId = userLog.getId();
                String userEmail = userLog.getEmail();
                String userTelephone = telephone.getText().toString();

                String yearStr = String.valueOf((year));



                dateExpiration=(expirationDateView.getText().toString() + "  " + expirationTimeView.getText());

//              PhoneNumberUtils.formatNumber(telephone.getText().toString());

                //Date Creates
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy  HH:mm");
                SimpleDateFormat dateFormat_hora = new SimpleDateFormat("HH:mm:ss");
                Date data = new Date();
                Calendar cal = Calendar.getInstance();
                cal.setTime(data);

                Date data_atual = cal.getTime();
                String dateCreation = dateFormat.format(data_atual);
                String hora_atual = dateFormat_hora.format(data_atual);


                //Snackbar.make(v, data_completa  , Snackbar.LENGTH_LONG).setAction("Action", null).show();
                //Snackbar.make(v, dateTime  , Snackbar.LENGTH_LONG).setAction("Action", null).show();
//                Log.i("data_completa", data_completa);
//                Log.i("data_atual", data_atual.toString());
//                Log.i("hora_atual", hora_atual);
                addNotification("Ticket para Expirar", "Seu ticket esta para expirar, reanuncie ou pague para vende mais rapido");

                // Check for already existed userId
                //if (TextUtils.isEmpty(userId)) {
                    if(createTicket(nameStr, descriptionStr, priceStr, CEP_Str, userId, userEmail, userTelephone, dateCreation, dateExpiration)){
                        Snackbar.make(v, "Ticket criado com sucesso", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                        //onBackPressed();
                        finish();
                    }
//               // } else {
                  //  updateUser(nameStr, descriptionStr, priceStr, CEP_Str);
                //}
            }
        });

        buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//              Intent intent = new Intent(Intent.ACTION_PICK,
//              android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
//              startActivityForResult(Intent.createChooser(intent, "Selecione uma imagem"), 123);

                //boolean result=Utility.checkPermission(CreateTicketActivity.this);

                selectImage();
//
//
//              Intent intent = new Intent();
//              intent.setType("image/*");
//              intent.setAction(Intent.ACTION_GET_CONTENT);//
//              startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);

            }


//            @Override
//            protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//                if (resultCode == RESULT_OK) {
//                    switch(requestCode){
//                        case TAKE_PHOTO_CODE:
//                            final File file = getTempFile(getApplicationContext());
//                            try {
//                                Bitmap captureBmp = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file) );
//                                // do whatever you want with the bitmap (Resize, Rename, Add To Gallery, etc)
//                            } catch (FileNotFoundException e) {
//                                e.printStackTrace();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                            break;
//                    }
//                }
//            }
        });

        buttonCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateTicketActivity.this, CategoryActivity.class));
            }
        });

        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Date expires
                DialogFragment timeFragment = new TimePickerFragment();
                timeFragment.show(getFragmentManager(),"TimePicker");


                DialogFragment dateFragment = new DatePickerFragment();
                dateFragment.show(getFragmentManager(),"DatePicker");

            }
        });

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("tickets");
        mFirebaseInstance.getReference("app_title").setValue("RedCup");

        // app_title change listener
        mFirebaseInstance.getReference("app_title").addValueEventListener(new ValueEventListener() {
            //public static final String TAG = "asd";

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.e(TAG, "App title updated");

                //String appTitle = dataSnapshot.getValue(String.class);

                // update toolbar title
                //getSupportActionBar().setTitle(appTitle);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
              //  Log.e(TAG, "Failed to read app title value.", error.toException());
            }
        });

    }

    //Notification
    private void addNotification(String title, String text) {
        NotificationCompat.Builder builder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_tickets)
                        .setContentTitle(title)
                        .setContentText(text);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private boolean createTicket(String title, String description, String price, String CEP, String userId, String userEmail, String userTelephone,String dateCreation, String dateExpiration) {
        // TODO
        // In real apps this userId should be fetched
        // by implementing firebase auth
        if (TextUtils.isEmpty(ticketId)) {
            ticketId = mFirebaseDatabase.push().getKey();
        }

        Ticket user = new Ticket(title, description, price, CEP, userId, userEmail, userTelephone, dateCreation, dateExpiration);

        mFirebaseDatabase.child(ticketId).setValue(user);

        addUserChangeListener();
        return  true;
    }

    private void addUserChangeListener() {
        // Ticket data change listener
        mFirebaseDatabase.child(ticketId).addValueEventListener(new ValueEventListener() {
            public static final String TAG = "Create Ticket Activity";

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Ticket user = dataSnapshot.getValue(Ticket.class);

                // Check for null
                if (user == null) {
                    Log.e(TAG, "Ticket data is null!");
                    return;
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read user", error.toException());
            }
        });
    }

    private void updateUser(String title, String description, String price, String CEP, String userId, String userEmail) {
        // updating the user via child nodes
        if (!TextUtils.isEmpty(title))
            mFirebaseDatabase.child(ticketId).child("title").setValue(title);

        if (!TextUtils.isEmpty(price))
            mFirebaseDatabase.child(ticketId).child("price").setValue(price);

        if (!TextUtils.isEmpty(description))
            mFirebaseDatabase.child(ticketId).child("description").setValue(description);

        if (!TextUtils.isEmpty(CEP))
            mFirebaseDatabase.child(ticketId).child("CEP").setValue(CEP);

        if (!TextUtils.isEmpty(userId))
            mFirebaseDatabase.child(ticketId).child("userId").setValue(userId);

        if (!TextUtils.isEmpty(userEmail))
            mFirebaseDatabase.child(ticketId).child("userEmail").setValue(userEmail);

    }

/*****************************************CAMERA*****************************************************/


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }



    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateTicketActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(CreateTicketActivity.this);
                String userChoosenTask;
                if (items[item].equals("Take Photo")) {
                    userChoosenTask="Take Photo";
                    if(result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask="Choose from Library";
                    if(result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }


//    //Store the Galerry Image
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if(resultCode == Activity.RESULT_OK){
//            if(requestCode == 123){
//                Uri imagemSelecionada = data.getData();
//                buttonCamera.setImageURI(imagemSelecionada);
//
//            }
//        }
//
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == SELECT_FILE)
//                onSelectFromGalleryResult(data);
//            else if (requestCode == REQUEST_CAMERA)
//                onCaptureImageResult(data);
//        }
//
//
//    }


    //
//    private void takePhoto(){
//        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getTempFile(this)) );
//        startActivityForResult(intent, TAKE_PHOTO_CODE);
//    }


//    private File getTempFile(Context context){
//        //it will return /sdcard/image.tmp
//        final File path = new File( Environment.getExternalStorageDirectory(), context.getPackageName() );
//        if(!path.exists()){
//            path.mkdir();
//        }
//        return new File(path, "image.tmp");
//    }


    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        buttonCamera.setImageBitmap(bm);
    }


    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        buttonCamera.setImageBitmap(thumbnail);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

}
