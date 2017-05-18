package com.arthur.redcup.View;

import android.Manifest;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
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
import android.text.Editable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.arthur.redcup.Model.Category;
import com.arthur.redcup.Model.ImageBitmap;
import com.arthur.redcup.Model.SearchCEPTask;
import com.arthur.redcup.Model.User;
import com.arthur.redcup.R;
import com.arthur.redcup.Model.Ticket;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class CreateTicketActivity extends AppCompatActivity {

    private static final int SELECT_FILE = 1;
    private static final int REQUEST_CAMERA = 0;
    private static final int MY_REQUEST_CODE = 2;
    private static final int CAMERA_REQUEST = 1888;
    private static final int CATEGORY_PICKER = 3;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private ProgressBar progressBar;
    private Category category;

    private String ticketId;

    private LinearLayout dateTimeLayout;
    private LinearLayout cepLayout;
    private LinearLayout categoryLayout;

    private EditText nameTicket;
    private EditText description;
    private EditText price;
    private EditText telephone;
    private EditText cepEditText;

    private TextView expirationDateView;
    private TextView expirationTimeView;
    private TextView locationView;
    private TextView categoryView;

    private CircleImageView circleImageView;

    private Bitmap bm;
    private ImageBitmap imageBitmap;


    private Button buttonSend;
    private Button buttonCategory;
    private Button buttonDate;

    private ImageButton buttonCamera;

    private Intent intentRecover;
    private FirebaseAuth.AuthStateListener authListener;

    private User userLog;
    //public CEP cep;



    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;


   // public JSONObject object;
    public SearchCEPTask searchCEPTask;
    public String dateExpiration;
    public String uf = "";
    public String location = "";
    public String neighborhood = "";

    public String codigoEnderecamentoPostal;

    public String categoryName;



    private String userChoosenTask;

    private static final int TAKE_PHOTO_CODE = 1;
    @RequiresApi(api = Build.VERSION_CODES.M)
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


        if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_REQUEST_CODE);
        }





        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_create_ticket);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.title));

        dateTimeLayout = (LinearLayout) findViewById(R.id.date_time_layout);
        cepLayout = (LinearLayout) findViewById(R.id.cep_layout);
        categoryLayout = (LinearLayout) findViewById(R.id.category_layout);

        nameTicket = (EditText) findViewById(R.id.edit_text_title);
        description = (EditText)findViewById(R.id.edit_text_description);
        price = (EditText) findViewById(R.id. editTextPrice);
        cepEditText = (EditText) findViewById(R.id.editTextCep);
        telephone = (EditText) findViewById(R.id.editTextTelephone);

        buttonCamera = (ImageButton) findViewById(R.id.camera_btn);
        buttonSend = (Button)findViewById(R.id.button_enviar);
        buttonCategory = (Button) findViewById(R.id.button_category);
        buttonDate = (Button) findViewById(R.id.button_date);

        progressBar = (ProgressBar) findViewById(R.id.progressBarCreateTicket);

        expirationDateView = (TextView) findViewById(R.id.text_view_event_date);
        expirationTimeView = (TextView) findViewById(R.id.text_view_event_time);
        locationView = (TextView) findViewById(R.id.text_view_location);
        categoryView = (TextView) findViewById(R.id.text_view_category);

        circleImageView = (CircleImageView) findViewById(R.id.profile_image);


        cepEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {


            }
        });


        //Listener of CEP EditText
        cepEditText.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    searchCEPTask = new SearchCEPTask();
                    codigoEnderecamentoPostal = cepEditText.getText().toString().replace( "/" , "");
                    try {
                        String str_result = searchCEPTask.execute(codigoEnderecamentoPostal).get();
                        JSONObject object = new JSONObject(str_result);

                        uf = object.getString(getString(R.string.cep_uf));
                        location = object.getString(getString(R.string.cep_location));
                        neighborhood = object.getString(getString(R.string.cep_neighborhood));

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //Verify if CEP is valid
                    if (uf.isEmpty() || location.isEmpty() || neighborhood.isEmpty()) {

                        cepEditText.requestFocus();
                        cepEditText.setError("CEP nao encontrado, tente novamente!");

                        return false;

                    }else{
                        cepLayout.setVisibility(VISIBLE);
                        cepEditText.setVisibility(GONE);
                        locationView.setText(location + " - " + uf + " - " + neighborhood);

                    }


                    return true;


                }
                return false;
            }

        });


        //************************************//
        //           FORMACTS TELEPHONE       //
        //************************************//

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

                    if (phone.length() >= 7 && !backspacingFlag) {
                        //we will edit. next call on this textWatcher will be ignored
                        editedFlag = true;
                        //here is the core. we substring the raw digits and add the mask as convenient
                        String ans = "(" + phone.substring(0, 2) + ") " + phone.substring(2,7) + "-" + phone.substring(7);
                        telephone.setText(ans);
                        //we deliver the cursor to its original position relative to the end of the string
                        telephone.setSelection(telephone.getText().length()-cursorComplement);

                        //we end at the most simple case, when just one character mask is needed
                        //example: 99999 <- 3+ digits already typed
                        // masked: (999) 99
                    } else if (phone.length() >= 2 && !backspacingFlag) {
                        editedFlag = true;
                        String ans = "(" +phone.substring(0, 2) + ") " + phone.substring(2);
                        telephone.setText(ans);
                        telephone.setSelection(telephone.getText().length()-cursorComplement);
                    }
                    // We just edited the field, ignoring this cicle of the watcher and getting ready for the next
                } else {
                    editedFlag = false;
                }
            }
        });




        //************************************//
        //             FORMACTS CEP           //
        //************************************//
        cepEditText.addTextChangedListener(new PhoneNumberFormattingTextWatcher() {
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
                    if (phone.length() >= 5 && !backspacingFlag) {
                        //we will edit. next call on this textWatcher will be ignored
                        editedFlag = true;
                        //here is the core. we substring the raw digits and add the mask as convenient
                        String ans =  phone.substring(0,5) + "-" + phone.substring(5);
                        cepEditText.setText(ans);
                        //we deliver the cursor to its original position relative to the end of the string
                        cepEditText.setSelection(cepEditText.getText().length());

                        //we end at the most simple case, when just one character mask is needed
                        //example: 99999 <- 3+ digits already typed
                        // masked: (999) 99
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
                codigoEnderecamentoPostal = cepEditText.getText().toString().replace( "/" , "");;
                String userId = userLog.getId();
                String userEmail = userLog.getEmail();
                String userTelephone = telephone.getText().toString();
                dateExpiration=(expirationDateView.getText().toString() + "  " + expirationTimeView.getText());


                //Date Creates
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy  HH:mm");
                SimpleDateFormat dateFormat_hora = new SimpleDateFormat("HH:mm:ss");
                Date data = new Date();
                Calendar cal = Calendar.getInstance();
                cal.setTime(data);

                Date data_atual = cal.getTime();
                String dateCreation = dateFormat.format(data_atual);
                String hora_atual = dateFormat_hora.format(data_atual);

                String yearStr = String.valueOf((year));

                if (TextUtils.isEmpty(nameStr)) {
                    nameTicket.setError(getString(R.string.name_ticket));
                    return;
                }
                if (TextUtils.isEmpty(priceStr)) {
                    price.setError(getString(R.string.price));
                    return;
                }
                if (TextUtils.isEmpty(descriptionStr)) {
                    description.setError(getString(R.string.description));
                    return;
                }
                if (TextUtils.isEmpty(userTelephone)) {
                    telephone.setError(getString(R.string.telephone));
                    return;
                }
                if(TextUtils.isEmpty(categoryView.getText())){
                    buttonCategory.setError("Seu Ticket precisa ter uma categoria!");
                    return;

                }
                if((dateExpiration.length() < 5)){
                    buttonDate.setError("Seu ticket precisa ter uma data de validade");
                    return;
                }
                if (userTelephone.length() < 11) {
                    telephone.setError(getString(R.string.telephone));
                    return;
                }
                if (TextUtils.isEmpty(codigoEnderecamentoPostal)) {
                    cepEditText.setError(getString(R.string.cep_error));
                    return;
                }else if (codigoEnderecamentoPostal.length() < 8) {
                    cepEditText.setError(getString(R.string.cep_numbers));
                    return;
                }

                searchCEPTask = new SearchCEPTask();

                try {
                    String str_result = searchCEPTask.execute(codigoEnderecamentoPostal).get();
                    JSONObject object = new JSONObject(str_result);

                    uf = object.getString(getString(R.string.cep_uf));
                    location = object.getString(getString(R.string.cep_location));
                    neighborhood = object.getString(getString(R.string.cep_neighborhood));

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Verify if CEP is valid
               if (uf.isEmpty() || location.isEmpty() || neighborhood.isEmpty()) {

                    cepEditText.requestFocus();
                    cepEditText.setError("CEP nao encontrado, tente novamente!");

                    return;

                }

                //addNotification("Ticket para Expirar", "Seu ticket esta para expirar, reanuncie ou pague para vende mais rapido");

                // Check for already existed userId
                //if (TextUtils.isEmpty(userId)) {


                try {
                    if(createTicket(nameStr, descriptionStr, priceStr, codigoEnderecamentoPostal, userId,
                                        userEmail, userTelephone, dateCreation, dateExpiration,  uf,
                                            location, neighborhood, category.getNome())){
//                        Snackbar.make(v, "Ticket criado com sucesso", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }



                   // } else {
                  //  updateUser(nameStr, descriptionStr, priceStr, CEP_Str);
                //}
            }
        });

        buttonCamera.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                selectImage();
            }


        });

        buttonCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CategoryActivity.class);
                startActivityForResult(i, CATEGORY_PICKER);
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

                buttonDate.setVisibility(GONE);
                dateTimeLayout.setVisibility(VISIBLE);
            }
        });

        dateTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonDate.callOnClick();

            }
        });

        cepLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cepLayout.setVisibility(GONE);
                cepEditText.setVisibility(VISIBLE);
                cepEditText.setText("");

            }
        });
        categoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonCategory.setVisibility(VISIBLE);
                categoryLayout.setVisibility(GONE);
                Intent i = new Intent(getApplicationContext(), CategoryActivity.class);
                startActivityForResult(i, 1);
            }
        });


        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent goTicketImage = new  Intent(CreateTicketActivity.this, TicketImageActivity.class);
                goTicketImage.putExtra("TicketBitmap", imageBitmap);
                goTicketImage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(goTicketImage);

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

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

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

    private boolean createTicket(String title, String description, String price, String codigoEnderecamentoPostal, String userId, String userEmail,
                                        String userTelephone,String dateCreation, String dateExpiration, String uf, String location, String neighborhood, String category) throws JSONException {
        // TODO
        // In real apps this userId should be fetched
        // by implementing firebase auth
        if (TextUtils.isEmpty(ticketId)) {
            ticketId = mFirebaseDatabase.push().getKey();
        }

        Ticket user = new Ticket(title, description, price, codigoEnderecamentoPostal, userId, userEmail, userTelephone, dateCreation, dateExpiration, uf,  location, neighborhood, category);

        mFirebaseDatabase.child(ticketId).setValue(user);
        //mFirebaseDatabase.child(ticketId).child("address").setValue("teste", "teste2");

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
                    Log.e(TAG, getString(R.string.ticket_null));
                    return;
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, getString(R.string.failed_read), error.toException());
            }
        });
    }

//    private void updateUser(String title, String description, String price, String CEP, String userId, String userEmail) {
//        // updating the user via child nodes
//        if (!TextUtils.isEmpty(title))
//            mFirebaseDatabase.child(ticketId).child("title").setValue(title);
//
//        if (!TextUtils.isEmpty(price))
//            mFirebaseDatabase.child(ticketId).child("price").setValue(price);
//
//        if (!TextUtils.isEmpty(description))
//            mFirebaseDatabase.child(ticketId).child("description").setValue(description);
//
//        if (!TextUtils.isEmpty(CEP))
//            mFirebaseDatabase.child(ticketId).child("CEP").setValue(CEP);
//
//        if (!TextUtils.isEmpty(userId))
//            mFirebaseDatabase.child(ticketId).child("userId").setValue(userId);
//
//        if (!TextUtils.isEmpty(userEmail))
//            mFirebaseDatabase.child(ticketId).child("userEmail").setValue(userEmail);
//
//    }

    
    //********************************//
    //               CAMERA           //
    //********************************//

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {

            case MY_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Now user should be able to use camera
                }
                break;

            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals(getString(R.string.take_photo)))
                        cameraIntent();
                    else if(userChoosenTask.equals(getString(R.string.library)))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void selectImage() {

        final CharSequence[] items = { getString(R.string.take_photo), getString(R.string.library),
                getString(R.string.cancel) };
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateTicketActivity.this);
        builder.setTitle(getString(R.string.add_photo));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(CreateTicketActivity.this);
                String userChoosenTask;
                if (items[item].equals(getString(R.string.take_photo))) {
                    userChoosenTask=getString(R.string.take_photo);
                    if(result)
                        cameraIntent();
                } else if (items[item].equals(getString(R.string.library))) {
                    userChoosenTask=getString(R.string.library);
                    if(result)
                        galleryIntent();

                } else if (items[item].equals(getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case SELECT_FILE:
                if(resultCode == RESULT_OK){
                    onSelectFromGalleryResult(data);
                }

                break;
            case CAMERA_REQUEST:
                if(resultCode == RESULT_OK){
                    onCaptureImageResult(data);
                }
                break;

            case CATEGORY_PICKER:
                if(resultCode == RESULT_OK){
                    category = (Category) data.getSerializableExtra("Category");
                    buttonCategory.setVisibility(GONE);
                    categoryLayout.setVisibility(VISIBLE);
                    categoryView.setText(category.getNome());
                }
        }


    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        buttonCamera.setVisibility(GONE);
        circleImageView.setVisibility(VISIBLE);
        circleImageView.setImageBitmap(bm);
        saveToInternalStorage(bm);
        imageBitmap = new ImageBitmap();
        imageBitmap.addPath(1, saveToInternalStorage(bm));

    }

    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
//        File destination = new File(Environment.getExternalStorageDirectory(),
//                System.currentTimeMillis() + ".jpg");
//        FileOutputStream fo;
//        try {
//            destination.createNewFile();
//            fo = new FileOutputStream(destination);
//            fo.write(bytes.toByteArray());
//            fo.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        //buttonCamera.setImageBitmap(thumbnail);

        //buttonCamera.setImageBitmap(Bitmap.createScaledBitmap(thumbnail, 300, 300, false));
        buttonCamera.setVisibility(GONE);
        circleImageView.setVisibility(VISIBLE);
        circleImageView.setImageBitmap(thumbnail);
        imageBitmap = new ImageBitmap();
        imageBitmap.addPath(1, saveToInternalStorage(thumbnail));

    }

    private void cameraIntent() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

}
