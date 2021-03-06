package com.example.wawinternet.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wawinternet.Modeles.ModelUser;
import com.example.wawinternet.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Abonner extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private Button locationButton;
    private TextView locationText;
    private EditText Rnom;
    private EditText Rprenom;
    private EditText Remail;
    private EditText Rtelepone;
    private RadioGroup RGdebit;
    private RadioButton RBdebit;
    private ModelUser modelUser;
    private double latitude01;
    private double longitude01;

    //Initializing the GoogleApiClient object
    private GoogleApiClient googleApiClient;
    public static final int LOCATION_REQUEST = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abonner);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Abonnement");
        myToolbar.setNavigationIcon(R.drawable.back2);

        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

        modelUser=new ModelUser();
        Rnom=(EditText) findViewById(R.id.Unom);
        Rprenom=(EditText) findViewById(R.id.Uprenom);
        Remail=(EditText) findViewById(R.id.Uemail);
        Rtelepone=(EditText) findViewById(R.id.Utel);
        RGdebit=(RadioGroup) findViewById(R.id.Uradiogroup);

        locationButton = (Button) findViewById(R.id.valider);

        locationText = (TextView) findViewById(R.id.location_text);
        //Building a instance of Google Api Client
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .build();
        /*locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                afficher();
            }
        });*/


    }
    public void onStart() {
        super.onStart();
        // Initiating the GoogleApiClient Connection when the activity is visible
        googleApiClient.connect();
    }
    public void onStop() {
        super.onStop();
        //Disconnecting the GoogleApiClient when the activity goes invisible
        googleApiClient.disconnect();
    }
    /*
    This callback is invoked when the GoogleApiClient is successfully connected
    */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //We set a listener to our button only when the ApiClient is connected successfully
        locationButton.setOnClickListener(this);
    }
    //This callback is invoked when the user grants or rejects the location permission
    //Ce rappel est appelé lorsque l'utilisateur accorde ou refuse l'emplacement. PermissionBACK
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation();
                } else
                    Toast.makeText(this, "Accàs votre posiytion refusé à WawInternet", Toast.LENGTH_SHORT);
                break;
        }
    }
    private void getCurrentLocation() {
        //Checking if the location permission is granted
        //Vérifier si l'autorisation d'emplacement est accordée
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            }, LOCATION_REQUEST);
            return;
        }
        //Fetching location using FusedLOcationProviderAPI
        FusedLocationProviderApi fusedLocationApi = LocationServices.FusedLocationApi;
        Location location = fusedLocationApi.getLastLocation(googleApiClient);
        //In some rare cases Location obtained can be null
        if (location == null)
            locationText.setText("Not able to fetch location");
        else{
           // locationText.setText("Location co-ord are " + location.getLatitude() + "," + location.getLongitude());
            latitude01= location.getLatitude();
            longitude01=location.getLongitude();

            }
    }
    //Callback invoked if the GoogleApiClient connection is suspended
    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Connection was suspended", Toast.LENGTH_SHORT);
    }
    //Callback invoked if the GoogleApiClient connection fails
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection failed", Toast.LENGTH_SHORT);
    }
   @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.valider:
                getCurrentLocation();
                afficher();
                break;

        }
    }

    public void afficher() {
       // modelUser.setNom(Rnom.getText().toString());
        //Toast.makeText(Abonner.this,modelUser.getNom(),Toast.LENGTH_LONG).show();

        int selectedRdebit=RGdebit.getCheckedRadioButtonId();
        RBdebit=(RadioButton) findViewById(selectedRdebit);


                modelUser.setNom(Rnom.getText().toString());
                modelUser.setPrenom(Rprenom.getText().toString());
                modelUser.setEmail(Remail.getText().toString());
                modelUser.setTel(Rtelepone.getText().toString());
                modelUser.setDebitAbonner(RBdebit.getText().toString());
                modelUser.setLatitude(latitude01);
                modelUser.setLongitude(longitude01);
                Toast.makeText(Abonner.this,modelUser.getNom(),Toast.LENGTH_LONG).show();
                Toast.makeText(Abonner.this,modelUser.getPrenom(),Toast.LENGTH_LONG).show();
                Toast.makeText(Abonner.this,modelUser.getEmail(),Toast.LENGTH_LONG).show();
                Toast.makeText(Abonner.this,modelUser.getTel(),Toast.LENGTH_LONG).show();
                Toast.makeText(Abonner.this,modelUser.getDebitAbonner(),Toast.LENGTH_LONG).show();
                Toast.makeText(Abonner.this,modelUser.getLatitude()+"",Toast.LENGTH_LONG).show();
                Toast.makeText(Abonner.this,modelUser.getLongitude()+"",Toast.LENGTH_LONG).show();
                //Toast.makeText(Abonner.this,latitude01+", -"+longitude01,Toast.LENGTH_LONG).show();
    }
}
