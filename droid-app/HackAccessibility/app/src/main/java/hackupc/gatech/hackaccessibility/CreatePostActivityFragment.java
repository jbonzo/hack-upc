package hackupc.gatech.hackaccessibility;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import hackupc.gatech.hackaccessibility.model.Post;
import hackupc.gatech.hackaccessibility.net.DataTroveInstance;

/**
 * A placeholder fragment containing a simple view.
 */
public class CreatePostActivityFragment extends Fragment implements OnMapReadyCallback {

    private MapView mapView = null;

    private LatLng mLocation = null;

    public CreatePostActivityFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent recievingIntent = getActivity().getIntent();
        mLocation = new LatLng(recievingIntent.getDoubleExtra(CreatePostActivity.LATITUDE_EXTRA, 0),
                recievingIntent.getDoubleExtra(CreatePostActivity.LONGITUDE_EXTRA, 0));


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_post, container, false);

//        SupportMapFragment mapFragment = (SupportMapFragment) this.getFragmentManager()
//                .findFragmentById(R.id.mapSnapshot);
//        mapFragment.getMapAsync(this);

        mapView = (MapView) view.findViewById(R.id.mapSnapshot);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        return view;

    }



    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("onMapReady", "Map Ready!!!#@$@#$");

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(mLocation));
        googleMap.addMarker(new MarkerOptions().position(mLocation));
    }
}
