package com.udacity.thefedex87.takemyorder.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.application.TakeMyOrderApplication;
import com.udacity.thefedex87.takemyorder.dagger.ApplicationModule;
import com.udacity.thefedex87.takemyorder.dagger.DaggerNetworkComponent;
import com.udacity.thefedex87.takemyorder.dagger.NetworkComponent;
import com.udacity.thefedex87.takemyorder.mock.PostMockData;
import com.udacity.thefedex87.takemyorder.models.Customer;
import com.udacity.thefedex87.takemyorder.models.Restaurant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static com.google.android.gms.vision.barcode.Barcode.QR_CODE;

public class LoginMapsActivity extends AppCompatActivity {
    //Keys
    public final static String RESTAURANTS_INFO_KEY = "RESTAURANTS_INFO";
    public final static String USER_INFO_KEY = "USER_INFO_KEY";
    public final static String USER_RESTAURANT_KEY = "USER_RESTAURANT_KEY";
    public final static String USER_RESTAURANT_TABLE_KEY = "USER_RESTAURANT_TABLE_KEY";

    //Activity results
    public static final int RC_SIGN_IN = 1;
    public static final int RC_PHOTO_PICKER = 2;
    public static final int RC_PHOTO_SHOT = 3;

    @BindView(R.id.map)
    ImageView map;

    @BindView(R.id.login)
    ImageView login;

    @Inject
    Context context;

    private ApplicationModule applicationModule;
    private NetworkComponent networkComponent;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference restaurantsReference;
    private DatabaseReference usersReference;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private Customer customer;

    private boolean logindRequest = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_maps);

        Timber.plant(new Timber.DebugTree());

        TakeMyOrderApplication.appComponent().inject(this);

        applicationModule = new ApplicationModule(context);

        networkComponent = DaggerNetworkComponent.builder().applicationModule(applicationModule).build();

        initUi();

        firebaseDatabase = FirebaseDatabase.getInstance();
        restaurantsReference = firebaseDatabase.getReference("restaurants");

        //PostMockData.postMockData();

        //Intent intent = new Intent(this, RestaurantsMapActivity.class);
        //startActivity(intent);

        //PostMockData.postMockData();

//        FirebaseDatabase db = FirebaseDatabase.getInstance();
//        DatabaseReference ref = db.getReference("restaurants");
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                int f = 5;
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                int t = 6;
//            }
//        });
    }

    private void initUi(){
        ButterKnife.bind(this);

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restaurantsReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Restaurant> restaurants = new ArrayList<>();
                        for(DataSnapshot restaurantSnapshot : dataSnapshot.getChildren())
                        {
                            restaurants.add(restaurantSnapshot.getValue(Restaurant.class));
                        }

                        Intent intent = new Intent(LoginMapsActivity.this, RestaurantsMapActivity.class);
                        intent.putParcelableArrayListExtra(RESTAURANTS_INFO_KEY, (ArrayList<Restaurant>) restaurants);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get the reference to FirebaseAuth
                firebaseAuth = FirebaseAuth.getInstance();

                logindRequest = true;

                authStateListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        final FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (!logindRequest) return;

                        if (user != null){
                            logindRequest = false;

                            //Connect to Firebase, check if user is a Waiter
                            String email = user.getEmail();
                            usersReference = firebaseDatabase.getReference("waiters");
                            usersReference
                                    .orderByChild("email")
                                    .equalTo(email)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            //If dataSnapshot.getValue() is equal to null, a customer is logging into the system
                                            if (dataSnapshot.getValue() == null){
//                                                AlertDialog.Builder builder;
//                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                                    builder = new AlertDialog.Builder(LoginMapsActivity.this, android.R.style.Theme_Material_Dialog_Alert);
//                                                } else {
//                                                    builder = new AlertDialog.Builder(LoginMapsActivity.this);
//                                                }
//                                                builder.setTitle("Delete entry")
//                                                        .setMessage("Are you sure you want to delete this entry?")
//                                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                                                            public void onClick(DialogInterface dialog, int which) {
//                                                                // continue with delete
//                                                            }
//                                                        })
//                                                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                                                            public void onClick(DialogInterface dialog, int which) {
//                                                                // do nothing
//                                                            }
//                                                        })
//                                                        .setIcon(android.R.drawable.ic_dialog_alert)
//                                                        .show();

                                                customer = new Customer();
                                                customer.setEmail(user.getEmail());
                                                customer.setUserName(user.getDisplayName());

                                                //Since this is a user log in, we need to launch camera in order to scan the qrcode at the table.
                                                String[] mimeTypes = {"image/jpeg", "image/png"};
                                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                                intent.setType("image/*").putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                                                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                                                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);

//                                                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                                                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//                                                    startActivityForResult(takePictureIntent, RC_PHOTO_SHOT);
//                                                }

//                                                //Customer login
//                                                Timber.d("Logging in user: " + user.getEmail());
//                                                Intent intent = new Intent(context, CustomerMainActivity.class);
//                                                Customer customer = new Customer();
//                                                customer.setEmail(user.getEmail());
//                                                customer.setUserName(user.getDisplayName());
//                                                intent.putExtra(USER_INFO_KEY, customer);
//                                                startActivity(intent);

                                            } else {
                                                //Waiter login
                                                AuthUI.getInstance().signOut(LoginMapsActivity.this);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                        } else {
                            //TODO: add facebook login
                            startActivityForResult(
                                    AuthUI.getInstance()
                                            .createSignInIntentBuilder()
                                            .setIsSmartLockEnabled(false)
                                            .setAvailableProviders(Arrays.asList(
                                                    new AuthUI.IdpConfig.EmailBuilder().build(),
                                                    new AuthUI.IdpConfig.GoogleBuilder().build()))
                                            .build(),
                                    RC_SIGN_IN);
                        }
                    }
                };

                firebaseAuth.addAuthStateListener(authStateListener);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (requestCode == RC_SIGN_IN && resultCode == RESULT_OK){
//
//        }

        if ((requestCode == RC_PHOTO_PICKER || requestCode == RC_PHOTO_SHOT) && resultCode == RESULT_OK) {
            //Create the barcode reader
            BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(context).setBarcodeFormats(QR_CODE).build();
            Bitmap bitmap = null;


            if (requestCode == RC_PHOTO_PICKER) {
                //Requested selection of the picture from file chooser
                Uri selectedImageUri = data.getData();

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                    Timber.e("Error parsing Bitmap " + e.getMessage());
                    Toast.makeText(this, getString(R.string.error_loading_bitmap), Toast.LENGTH_LONG).show();
                    return;
                }
            } else if(requestCode == RC_PHOTO_SHOT) {
                //Requested selection of the picture from file camera shot
                Bundle extras = data.getExtras();
                bitmap = (Bitmap) extras.get("data");
            }

            //Detect the barcode from Bitmap
            Frame myFrame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<Barcode> barcodes = barcodeDetector.detect(myFrame);
            if (barcodes.size() == 0){
                //Barcode not found on picture
                Timber.w(getString(R.string.error_no_barcode_found));
                Toast.makeText(this, getString(R.string.error_no_barcode_found), Toast.LENGTH_LONG).show();
            } else {
                //Retrieve the barcode content
                String qrString = barcodes.get(barcodes.keyAt(0)).displayValue;

                //Parse the barcode content into a JSONObject
                JSONObject json = null;
                try {
                    json = new JSONObject(qrString);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Timber.e("Errore creating JSON object " + e.getMessage());
                    Toast.makeText(this, getString(R.string.error_loading_bitmap), Toast.LENGTH_LONG).show();
                    return;
                }

                //Extract the information from the JSONObject
                String restaurantId = null;
                String table = null;
                try {
                    restaurantId = json.getString("restaurantId");
                    table = json.getString("table");
                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (restaurantId != null || table != null) {
                    //If information are correctly extracted goto CustomerMainActivity
                    //Customer login
                    Timber.d("Logging in user: " + customer.getEmail());
                    Intent intent = new Intent(context, CustomerMainActivity.class);
                    intent.putExtra(USER_INFO_KEY, customer);
                    intent.putExtra(USER_RESTAURANT_KEY, restaurantId);
                    intent.putExtra(USER_RESTAURANT_TABLE_KEY, table);
                    startActivity(intent);
                } else {
                    Timber.w(getString(R.string.error_no_valid_information_from_barcode));
                    Toast.makeText(this, getString(R.string.error_no_valid_information_from_barcode), Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
