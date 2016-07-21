package hackupc.gatech.hackaccessibility;

import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMapClickListener {

    private static final int CIRCLE_FILL_COLOR = 0x502493B5;

    private static final double MILES_TO_METERS = 1609.34;
    private static final float INITIAL_ZOOM_LEVEL = 10.33f;

    private GoogleApiClient mApiClient;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (mApiClient == null) {
            mApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .build(); // TODO: add listeners and stuff
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION});
            return;
        }
        Location curLocation = LocationServices.FusedLocationApi.getLastLocation(mApiClient);

        // Add a marker in Sydney and move the camera
        LatLng bcn = new LatLng(41.388976799774106,2.164280153810978);
        mMap.addMarker(new MarkerOptions().position(bcn).title("Marker in BCN"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(bcn));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(INITIAL_ZOOM_LEVEL));

        CircleOptions clickableRegion = new CircleOptions();
        clickableRegion.center(bcn);
        clickableRegion.radius(10 * MILES_TO_METERS);
        clickableRegion.fillColor(CIRCLE_FILL_COLOR);
        clickableRegion.strokeWidth(0);


        mMap.addCircle(clickableRegion);

        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMapClickListener(this);

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Log.d("Info window clicked", "Info Clicked! title: " + marker.getTitle());
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Log.d("Map Clicked", "Map Clicked! loc: " + latLng.toString());
    }
}
