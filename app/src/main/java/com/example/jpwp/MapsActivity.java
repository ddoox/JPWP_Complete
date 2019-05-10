package com.example.jpwp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    LocationCallback locationCallback;
    private boolean PierwszeUstaleniePozycji = true;
    private Location AktualnaPozycjaLocation;
    private LatLng AktualnaPozycjaWspolrzedneLatLang;
    private Marker AktualnaPozycjaMarker;


    //Zad 6
    private TextView textview2;
    private TextView textview3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        // Zad 2 - odebranie wiadomosci
        String wiadomosc = getIntent().getStringExtra("id_wiadomosci");
        final TextView textview = findViewById(R.id.textView2);
        textview.setText(wiadomosc);


        //Zad 3 - funkcja finish() zabijająca activity po naciśnięciu przycisku
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        // Zad 6 - przypisanie pól tekstowych
        textview2 = findViewById(R.id.textView2);
        textview3 = findViewById(R.id.textView3);


        // Zad 5 - kod służacy do otrzymywania lokalizacji
        // Tworzenie instancji Fused Location Provider Client, wykorzystywana do lokalizacji
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //w location request ustawia się parametry odświeżania lokalizacji i dokładność
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        //wywoływane przy aktualizacji położenia
        locationCallback = new LocationCallback()
        {
            @Override
            public void onLocationResult(LocationResult locationResult)
            {
                if (locationResult == null)
                {
                    return;
                }
                else
                {
                    for (Location location : locationResult.getLocations())
                    {
                        //przypisanie nowej lokalizaji do naszych zmiennych
                        AktualnaPozycjaLocation = location;
                        AktualnaPozycjaWspolrzedneLatLang = new LatLng(AktualnaPozycjaLocation.getLatitude(), AktualnaPozycjaLocation.getLongitude());

                        // Zad 6 - aktualizacja pól tekstowych
                        textview2.setText(Double.toString(AktualnaPozycjaLocation.getLatitude()));
                        textview3.setText(Double.toString(AktualnaPozycjaLocation.getLongitude()));

                        // Zad 7 - aktualizacja pozycji markera + przesunięcie kamery
                        AktualnaPozycjaMarker.setPosition(AktualnaPozycjaWspolrzedneLatLang);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(AktualnaPozycjaWspolrzedneLatLang,11.0f));


                        //po pierwszym ustaleniu lokalizacji pokazanie markera, najazd kamery
                        if (PierwszeUstaleniePozycji)
                        {
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(AktualnaPozycjaWspolrzedneLatLang, 10.0f));
                            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(AktualnaPozycjaWspolrzedneLatLang, 10.0f));
                            PierwszeUstaleniePozycji = false;
                        }
                    }
                }
            }
        };




    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Zad 4 - utworzenie markera
        LatLng latlng = new LatLng(50, 20);
        AktualnaPozycjaMarker = mMap.addMarker(new MarkerOptions().position(latlng).title("Marker"));

    }


    //Zad 5 - funkcje do otrzymywania lokalizacji

    @Override
    protected void onResume()
    {
        super.onResume();
        startLocationUpdates();
    }


    @Override
    protected void onPause()
    {
        super.onPause();
        stopLocationUpdates();
    }

    private void startLocationUpdates()
    {
        //sprawdzanie potrzebnych uprawnień
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }


    private void stopLocationUpdates()
    {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

}
