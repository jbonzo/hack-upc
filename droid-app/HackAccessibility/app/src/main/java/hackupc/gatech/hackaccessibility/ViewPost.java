package hackupc.gatech.hackaccessibility;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import hackupc.gatech.hackaccessibility.model.Post;
import hackupc.gatech.hackaccessibility.model.User;
import hackupc.gatech.hackaccessibility.net.DataTroveInstance;

public class ViewPost extends AppCompatActivity implements OnMapReadyCallback {

    public static final int VIEW_POST_REQUEST = 101;
    public static final int RESULT_REMOVE = 112;
    public static final int RESULT_DO_NOTHING = 1265;

    private static final int THUMB_NEUTRAL_COLOR = 0x00000000;
    private static final int THUMB_POSITIVE_COlOR = 0xDD45F731;
    private static final int THUMB_NEGATIVE_COLOR = 0xDDFA690C;

    private MapView mapView;

    private Post mPost;

    private TextView tvVotes;
    private LatLng location;

    private ImageButton imgThumbsDown, imgThumbsUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/


        Intent intent = getIntent();

        double latitude = intent.getDoubleExtra(CreatePostActivity.LATITUDE_EXTRA, 0);
        double longitude = intent.getDoubleExtra(CreatePostActivity.LONGITUDE_EXTRA, 0);
        location = new LatLng(latitude, longitude);

        String title = intent.getStringExtra(CreatePostActivity.TITLE_EXTRA);

        mPost = new Post("author", title, "desc", latitude, longitude, "category", "imgurl", 0);
        mPost = DataTroveInstance.GetInstance().getPost(mPost.hashCode());

        imgThumbsUp = (ImageButton) findViewById(R.id.imgThumbsUp);
        imgThumbsDown = (ImageButton) findViewById(R.id.imgThumbsDown);

        initFields(mPost);

        mapView = (MapView) findViewById(R.id.mapSnapshot);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    private void initFields(Post post) {
        Log.d("initField", "User: " + User.GetName() + " | author: " + post.getAuthor());
        ((TextView) findViewById(R.id.tvViewTitle)).setText(post.getTitle());
        ((TextView) findViewById(R.id.tvViewCategory)).setText(post.getCategory());
        ((TextView) findViewById(R.id.tvViewDescription)).setText(post.getDescription());


        updateThumbColors();

        if (post.getAuthor().equals(User.GetName())) {
            tvVotes = (TextView) findViewById(R.id.tvBottomLeftText);
            tvVotes.setText(post.getVotes() + " votes!");

            findViewById(R.id.layoutVotes).setVisibility(View.INVISIBLE);
            findViewById(R.id.btnRemove).setVisibility(View.VISIBLE);
        }
        else {
            tvVotes = (TextView) findViewById(R.id.tvVotes);
            tvVotes.setText(String.valueOf(post.getVotes()));

            ((TextView) findViewById(R.id.tvBottomLeftText)).setText("By: " + post.getAuthor());

            findViewById(R.id.layoutVotes).setVisibility(View.VISIBLE);
            findViewById(R.id.btnRemove).setVisibility(View.INVISIBLE);
        }
    }

    private void updateThumbColors() {
        //if (true) {return;}

        imgThumbsDown.setBackgroundColor(THUMB_NEUTRAL_COLOR);
        imgThumbsUp.setBackgroundColor(THUMB_NEUTRAL_COLOR);
        switch (DataTroveInstance.GetInstance().getVote(mPost)) {
            case UP_VOTE: imgThumbsUp.setBackgroundResource(R.drawable.positive_thumb_bg); break;
            case DOWN_VOTE: imgThumbsDown.setBackgroundResource(R.drawable.negative_thumb_bg); break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        googleMap.addMarker(new MarkerOptions().position(location));
    }



    public void btnThumbsUp_onClick(View view) {
        Log.d("onClick", "Thumbs up!");
        int newVotes = DataTroveInstance.GetInstance().upvotePost(mPost);
        updateThumbColors();
        tvVotes.setText(String.valueOf(newVotes));
    }

    public void btnThumbsDown_onClick(View view) {
        Log.d("onClick", "Thumbs down!");
        int newVotes = DataTroveInstance.GetInstance().downvotePost(mPost);
        updateThumbColors();
        tvVotes.setText(String.valueOf(newVotes));
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
    public void onDestroy() {
        super.onDestroy();
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

    public void btnRemove_onClick(View view) {

        //this is gross but it just asks to confirm before deleting whatevs
        new AlertDialog.Builder(this).setTitle("Are you sure?")
                .setMessage("Are you sure you want to do this? Life is final and there is no going back honky")
                .setNegativeButton("Nah brah", null)
                .setPositiveButton("I accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DataTroveInstance.GetInstance().deletePost(mPost);

                        setResult(RESULT_REMOVE);
                        finish();
                    }
                }).create().show();

    }
}
