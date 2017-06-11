package com.wassa.candidate;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.wassa.candidate.model.NetworkService;
import com.wassa.candidate.model.Place;

import java.util.ArrayList;

import io.realm.Realm;

/**
 * @author khadir
 * @since 10/06/2017
 */
public class DetailActivity extends Activity implements View.OnClickListener, OnMapReadyCallback {

    public static final String EXTRA_PARAM_ID = "place_id";
    private ListView mList;
    private ImageView mImageView;
    private TextView mTitle;
    private FloatingActionButton mShowMapButton;
    private FloatingActionButton mToggleFavoriteButton;
    private FrameLayout mRevealMapView;
    private ArrayList<String> mPlaceDetailList = new ArrayList<>();
    int defaultColorForRipple;
    private MapView mMapView;
    private Place mPlace;
    private boolean favorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        String placeId = getIntent().getStringExtra(EXTRA_PARAM_ID);
        mList = (ListView) findViewById(R.id.list);
        mImageView = (ImageView) findViewById(R.id.placeImage);
        mTitle = (TextView) findViewById(R.id.title);
        mShowMapButton = (FloatingActionButton) findViewById(R.id.btn_showMap);
        mToggleFavoriteButton = (FloatingActionButton) findViewById(R.id.btn_toggleFavorite);
        mRevealMapView = (FrameLayout) findViewById(R.id.reveal_mapLayout);
        mMapView = (MapView) findViewById(R.id.map);

        mRevealMapView.setVisibility(View.INVISIBLE);
        mShowMapButton.setOnClickListener(this);
        mToggleFavoriteButton.setOnClickListener(this);

        mShowMapButton.setImageResource(android.R.drawable.ic_dialog_map);
        defaultColorForRipple = getResources().getColor(R.color.colorPrimary);

        fetchPlace(placeId);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);

    }

    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user receives a prompt to install
     * Play services inside the SupportMapFragment. The API invokes this method after the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in the coordinates,
        // and move the map's camera to the same location.
        if (mPlace != null) {
            LatLng latLng = new LatLng(mPlace.getLatitiude(), mPlace.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(latLng)
                    .title(mPlace.getLabel()));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMapView.setVisibility(View.VISIBLE);
            mMapView.onResume();
        }
    }

    /**
     * request place informations from the webservice by the place id
     *
     * @param placeId
     */
    private void fetchPlace(String placeId) {
        Place existingPlace = Realm.getDefaultInstance().where(Place.class).equalTo("id", placeId).findFirst();
        favorite = existingPlace.isFavorite();
        NetworkService.getInstance(this).getPlaceInfo(placeId, new NetworkService.ApiPlaceInfoResult() {
            @Override
            public void success(Place res) {
                updatePlace(res);
                displayPlaceInfo();
            }

            @Override
            public void error(int code, String message) {

            }
        });
    }

    /**
     * apply place's informations on the listView and download place's image to apply in imageView
     */
    private void displayPlaceInfo() {
        mPlaceDetailList.add(String.format(getString(R.string.title_country), mPlace.getCountry()));
        mPlaceDetailList.add(String.format(getString(R.string.title_description), mPlace.getDescription()));
        mPlaceDetailList.add(String.format(getString(R.string.title_author), mPlace.getImageAuthor()));
        mPlaceDetailList.add(String.format(getString(R.string.title_link), mPlace.getImageCredit()));
        mPlaceDetailList.add(String.format(getString(R.string.title_coordinates), mPlace.getLatitiude(), mPlace.getLongitude()));
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter(this, android.R.layout.simple_list_item_1, mPlaceDetailList);
        mList.setAdapter(itemsAdapter);

        mTitle.setText(mPlace.getLabel());

        favorite = mPlace.isFavorite();
        mToggleFavoriteButton.setImageResource(favorite ? android.R.drawable.btn_star_big_on : android.R.drawable.btn_star_big_off);
        Glide.with(this).load(NetworkService.getInstance(this).getPlaceImage(mPlace.getImageId())).into(mImageView);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_showMap:
                boolean isVisible = mRevealMapView.getVisibility() == View.VISIBLE;
                mShowMapButton.setImageResource(isVisible ? android.R.drawable.ic_dialog_map : android.R.drawable.ic_menu_report_image);
                mRevealMapView.setVisibility(isVisible ? View.INVISIBLE : View.VISIBLE);
                break;
            case R.id.btn_toggleFavorite:
                favorite = !favorite;
                updatePlace(mPlace);
                break;
        }
    }

    /**
     * update the current displayed place on realm database
     * and refresh favorite status view
     *
     * @param place
     */
    private void updatePlace(Place place) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        //we set the favorite with the last status before the refresh
        place.setFavorite(favorite);
        realm.copyToRealmOrUpdate(place);
        realm.commitTransaction();
        realm.close();
        mPlace = place;
        mToggleFavoriteButton.setImageResource(mPlace.isFavorite() ? android.R.drawable.btn_star_big_on : android.R.drawable.btn_star_big_off);
    }

    /**
     * on back pressed show an animated transition from the {@link DetailActivity} View to @{@link MainActivity}
     */
    @Override
    public void onBackPressed() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(100);
        mShowMapButton.startAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mShowMapButton.setVisibility(View.GONE);
                finishAfterTransition();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
