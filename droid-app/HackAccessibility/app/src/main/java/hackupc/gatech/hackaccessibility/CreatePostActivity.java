package hackupc.gatech.hackaccessibility;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import hackupc.gatech.hackaccessibility.model.Post;
import hackupc.gatech.hackaccessibility.net.DataTroveInstance;

public class CreatePostActivity extends AppCompatActivity {

    public static final String LONGITUDE_EXTRA = "Longitude Extra";
    public static final String LATITUDE_EXTRA = "Latitude Extra";
    public static final int CREATE_POST_REQUEST = 100;
    public static final String TITLE_EXTRA = "Title Extra";
    public static final int RESULT_SUCCESS = 1;
    public static final int RESULT_FAILURE = -1;

    private LatLng mLocation = null;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Create New Post");

        Intent recievingIntent = getIntent();
        mLocation = new LatLng(recievingIntent.getDoubleExtra(CreatePostActivity.LATITUDE_EXTRA, 0),
                recievingIntent.getDoubleExtra(CreatePostActivity.LONGITUDE_EXTRA, 0));


        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        })*/;
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void btnConfirm_onClick(View view) {
        Post post = getPostFromInput();

        DataTroveInstance.GetInstance().savePost(post);

        Intent returnIntent = new Intent();
        returnIntent.putExtra(LATITUDE_EXTRA, mLocation.latitude);
        returnIntent.putExtra(LONGITUDE_EXTRA, mLocation.longitude);
        returnIntent.putExtra(TITLE_EXTRA, post.getTitle());

        setResult(RESULT_SUCCESS, returnIntent);
        finish();
    }

    public void btnCancel_onClick(View view) {
        setResult(RESULT_FAILURE);
        finish();
    }

    private Post getPostFromInput() {
        String author = "Terry Tester";
        String title = ((EditText) findViewById(R.id.txtTitle)).getText().toString();
        String description = ((EditText) findViewById(R.id.txtDescription)).getText().toString();

        return new Post(author, title, description,
                mLocation.latitude, mLocation.longitude);
    }

}
