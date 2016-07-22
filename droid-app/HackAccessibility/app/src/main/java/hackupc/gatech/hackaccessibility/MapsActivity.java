package hackupc.gatech.hackaccessibility;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashSet;
import java.util.List;

import hackupc.gatech.hackaccessibility.model.Post;
import hackupc.gatech.hackaccessibility.model.User;
import hackupc.gatech.hackaccessibility.net.DataTroveInstance;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMapClickListener {

    private static final int CIRCLE_FILL_COLOR = 0x502493B5;
    private static final int LOCATION_FILL_COLOR = 0xAA0000FF;

    private static final int GET_LOCATION_PERMISSION_CODE = 1000;

    private static final double MILES_TO_METERS = 1609.34;
    private static final double CIRCLE_RADIUS = 10 * MILES_TO_METERS;

    private static final float INITIAL_ZOOM_LEVEL = 16.33f;

    private LatLng mCurLocLatLng = null;
    private LocationManager mLocationManager;
    private GoogleMap mMap;

    private TextView tvCurUser;
    private Button btnChangeUser;

    private Marker mSelectedMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (!DataTroveInstance.IsInitialized()) {
            DataTroveInstance.InitInstance(getApplicationContext());
        }

        btnChangeUser = (Button) findViewById(R.id.btnChangeUser);
        tvCurUser = (TextView) findViewById(R.id.tvCurUser);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                centerMap();
            }
        });

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


    }

    private void addExistingPosts() {
        List<Post> posts = DataTroveInstance.GetInstance().loadPosts();

        for (Post post : posts) {
            LatLng latLng = new LatLng(post.getLatitude(), post.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title(post.getTitle()));
        }
    }

    private void centerMap() {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mCurLocLatLng, INITIAL_ZOOM_LEVEL));
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

            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                    GET_LOCATION_PERMISSION_CODE);
            return;
        }
        Log.d("onMapReady", "Map ready, request location");

        addExistingPosts();

        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMapClickListener(this);

        mLocationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, null);



    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        mSelectedMarker = marker;

        Log.d("Info window clicked", "Info Clicked! title: " + marker.getTitle());
        Intent intent = new Intent(this, ViewPost.class);
        intent.putExtra(CreatePostActivity.LATITUDE_EXTRA, marker.getPosition().latitude);
        intent.putExtra(CreatePostActivity.LONGITUDE_EXTRA, marker.getPosition().longitude);
        intent.putExtra(CreatePostActivity.TITLE_EXTRA, marker.getTitle());

        startActivityForResult(intent, ViewPost.VIEW_POST_REQUEST);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        float[] distance = new float[1];
        Location.distanceBetween(mCurLocLatLng.latitude, mCurLocLatLng.longitude,
                                 latLng.latitude, latLng.longitude, distance);

        if (distance[0] <= CIRCLE_RADIUS) {
            Intent intent = new Intent(this, CreatePostActivity.class);
            intent.putExtra(CreatePostActivity.LATITUDE_EXTRA, latLng.latitude);
            intent.putExtra(CreatePostActivity.LONGITUDE_EXTRA, latLng.longitude);

            this.startActivityForResult(intent, CreatePostActivity.CREATE_POST_REQUEST);
        }
        else {
            Toast.makeText(this, "You can only make new posts within 10 miles of your current location", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        int n = 0;
        for (String permission : permissions) {
            if (permission.equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                if (grantResults[n] == PackageManager.PERMISSION_GRANTED) {
                    if (requestCode == GET_LOCATION_PERMISSION_CODE) {
                        onMapReady(mMap);
                    }
                }
                else {
                    Toast.makeText(this, "You must grant permission for the app to work...", Toast.LENGTH_SHORT).show();
                }
            }

            n++;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("ACtivity Result", "request: " + requestCode + ", result: " + resultCode);

        if (requestCode == CreatePostActivity.CREATE_POST_REQUEST) {
            if (resultCode == CreatePostActivity.RESULT_SUCCESS) {
                double latitude = data.getDoubleExtra(CreatePostActivity.LATITUDE_EXTRA, 0);
                double longitude = data.getDoubleExtra(CreatePostActivity.LONGITUDE_EXTRA, 0);
                String title = data.getStringExtra(CreatePostActivity.TITLE_EXTRA);

                mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(title));
            }
        }
        else if (requestCode == ViewPost.VIEW_POST_REQUEST) {
            if (resultCode == ViewPost.RESULT_REMOVE) {
                mSelectedMarker.remove();
                mSelectedMarker = null;
            }
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("onLocationChanged", "location changed!");

        mCurLocLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        mMap.moveCamera(CameraUpdateFactory.zoomTo(INITIAL_ZOOM_LEVEL));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mCurLocLatLng));

        CircleOptions clickableRegion = new CircleOptions();
        clickableRegion.center(mCurLocLatLng);
        clickableRegion.radius(CIRCLE_RADIUS);
        clickableRegion.fillColor(CIRCLE_FILL_COLOR);
        clickableRegion.strokeWidth(0);

        CircleOptions curLocationCircle = new CircleOptions();
        curLocationCircle.center(mCurLocLatLng);
        curLocationCircle.radius(50);
        curLocationCircle.fillColor(LOCATION_FILL_COLOR);
        curLocationCircle.strokeWidth(0);

        mMap.addCircle(clickableRegion);
        mMap.addCircle(curLocationCircle);

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Log.d("onStatuschanged", "Status Changed!");
    }

    @Override
    public void onProviderEnabled(String s) {
        Log.d("onProviderEnabled", "Provider Enabled!");
    }

    @Override
    public void onProviderDisabled(String s) {
        Log.d("onProviderDosanled", "Provider Disabled");
    }

    public void btnChangeUser_onClick(View view) {
        btnChangeUser.setText("Be " + User.GetName() + "!");

        User.isTerry = !User.isTerry;

        tvCurUser.setText("Logged in as: " + User.GetName());
    }
}
