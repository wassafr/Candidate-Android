package com.wassa.candidate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.wassa.candidate.model.NetworkService;
import com.wassa.candidate.model.Place;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

/**
 * @author khadir
 * @since 10/06/2017
 */
public class MainActivity extends AppCompatActivity implements OnRefreshListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private TravelListAdapter mAdapter;
    private boolean isListDisplay, isFavoriteDisplay;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAdapter = new TravelListAdapter(this, new ArrayList<Place>());
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setLayoutManager(mStaggeredLayoutManager);

        mRecyclerView.setHasFixedSize(true); //Data size is fixed - improves performance
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(onItemClickListener);

        isListDisplay = true;
        isFavoriteDisplay = false;
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_swipe);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        fetchplaces();
    }

    /**
     * refresh the place list content and apply the refresh on the listView
     */
    @Override
    public void onRefresh() {
        updateFavoriteIcon();
        fetchplaces();
    }


    /**
     * on item click launch {@link DetailActivity} and apply animation transition between
     * the two activites and pass the place id as extra String
     */
    private TravelListAdapter.OnItemClickListener onItemClickListener = new TravelListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, int position) {
            Intent transitionIntent = new Intent(MainActivity.this, DetailActivity.class);
            transitionIntent.putExtra(DetailActivity.EXTRA_PARAM_ID, mAdapter.getItemList().get(position).getId());
            ImageView placeImage = (ImageView) v.findViewById(R.id.placeImage);
            LinearLayout placeNameHolder = (LinearLayout) v.findViewById(R.id.placeNameHolder);
            View navigationBar = findViewById(android.R.id.navigationBarBackground);
            View statusBar = findViewById(android.R.id.statusBarBackground);
            Pair<View, String> imagePair = Pair.create((View) placeImage, "tImage");
            Pair<View, String> holderPair = Pair.create((View) placeNameHolder, "tNameHolder");
            Pair<View, String> statusPair = Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, imagePair, holderPair, statusPair);
            ActivityCompat.startActivity(MainActivity.this, transitionIntent, options.toBundle());
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_show_favorite:
                updateFavoriteIcon();
                displayPlaceList(isFavoriteDisplay);
                break;
            case R.id.action_toggle:
                toggleDisplay();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * change the display of list item grid size
     */
    private void toggleDisplay() {
        MenuItem item = menu.findItem(R.id.action_toggle);
        mStaggeredLayoutManager.setSpanCount(isListDisplay ? 2 : 1);
        item.setIcon(isListDisplay ? R.drawable.ic_action_list : R.drawable.ic_action_grid);
        item.setTitle(isListDisplay ? R.string.title_grid_off : R.string.title_grid_on);
        isListDisplay = !isListDisplay;
    }

    /**
     * update the favorite places filter menu icon
     */
    private void updateFavoriteIcon(){
        MenuItem item = menu.findItem(R.id.action_show_favorite);
        item.setIcon(isFavoriteDisplay ? android.R.drawable.btn_star_big_on : android.R.drawable.btn_star_big_off);
        item.setTitle(isFavoriteDisplay ? R.string.title_favorite_on : R.string.title_favorite_off);
    }

    /**
     * update the listView adapter
     * @param showFavorite filter the list to show only favorite places
     */
    private void displayPlaceList(boolean showFavorite) {
        Realm realm = Realm.getDefaultInstance();
        List places = showFavorite ? realm.where(Place.class).equalTo("favorite", true).findAll() : realm.where(Place.class).findAll();
        isFavoriteDisplay = !showFavorite;
        mAdapter.setItemList(places);
        mAdapter.notifyDataSetChanged();
        realm.close();
    }

    /**
     * This method is called to request from server the list of places
     */
    public void fetchplaces() {
        mSwipeRefreshLayout.setRefreshing(true);
        NetworkService apiService = NetworkService.getInstance(this);
        apiService.getPlacesList(new NetworkService.ApiPlacesResult<List<Place>>() {

            @Override
            public void success(List<Place> places) {
                Realm realm = Realm.getDefaultInstance();
                for (Place place : places) {
                    try {
                        realm.beginTransaction();
                        realm.copyToRealm(place);
                        realm.commitTransaction();
                    } catch (RealmPrimaryKeyConstraintException e) {
                        realm.cancelTransaction();
                    }
                }
                displayPlaceList(false);
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void error(int code, String message) {
                Log.e(TAG, "fetchRepoList error code " + code + ": " + message);
                Toast.makeText(getApplicationContext(), "error " + code + ": " + message, Toast.LENGTH_SHORT).show();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
