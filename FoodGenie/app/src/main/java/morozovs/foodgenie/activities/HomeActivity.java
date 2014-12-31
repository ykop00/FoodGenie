package morozovs.foodgenie.activities;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import morozovs.foodgenie.R;
import morozovs.foodgenie.api.FoodFinderAPI;
import morozovs.foodgenie.fragment.ExtendedPlaceInfoFragment;
import morozovs.foodgenie.fragment.FinalPlaceFragment;
import morozovs.foodgenie.fragment.SearchBuilderFragment;
import morozovs.foodgenie.fragment.SelectedPlacesFragment;
import morozovs.foodgenie.fragment.WelcomeFragment;
import morozovs.foodgenie.interfaces.IExtendedResultsGetter;
import morozovs.foodgenie.interfaces.IHomeNavigationManager;
import morozovs.foodgenie.interfaces.ILocationReceiver;
import morozovs.foodgenie.interfaces.IResultSelectionManagement;
import morozovs.foodgenie.models.ExtendedPlaceInfo;
import morozovs.foodgenie.models.MyPlaceInfo;
import morozovs.foodgenie.models.SearchParameters;
import morozovs.foodgenie.models.SearchResult;
import morozovs.foodgenie.utils.AppController;
import morozovs.foodgenie.utils.LocationHelper;
import morozovs.foodgenie.utils.PlacesGetter;
import morozovs.foodgenie.utils.StringUtils;

public class HomeActivity extends BaseActivity implements IHomeNavigationManager, IResultSelectionManagement, IExtendedResultsGetter, ILocationReceiver {

    private static ArrayList<MyPlaceInfo> visitedPlaces;
    private static LocationHelper locationHelper;
    private String params;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationHelper = new LocationHelper(this);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            if(extras.getString(KEY_FINAL_FRAGMENT, "").equals("true")){
                loadFinalPlace(MyPlaceInfo.fromJsonString(extras.getString(FinalPlaceFragment.KEY_PLACE_PARAM, "")));
            } else if(extras.getString(KEY_PLACE_INFO_FRAGMENT, "").equals("true")){
                startFragmentFresh(ExtendedPlaceInfoFragment.newInstance((MyPlaceInfo.fromJsonString(extras.getString(ExtendedPlaceInfoFragment.KEY_PLACE, "")))));
            }
        }else loadWelcome();
    }

    @Override
    public void onStart(){
        super.onStart();
        new GetVisitedPlaces().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void loadInfo() {
        startFragmentWithBackstack(new SearchBuilderFragment());
    }

    @Override
    public void loadWelcome() {
        startFragmentFresh(new WelcomeFragment());
    }

    @Override
    public void loadVisitedPlaces() {
        startFragmentWithBackstack(new SelectedPlacesFragment());
    }

    @Override
    public void loadFinalPlace(MyPlaceInfo finalPlace) {
        startFragmentFresh(FinalPlaceFragment.newInstance(finalPlace));
    }

    @Override
    public void startExtendedPlaceInfo(MyPlaceInfo placeToLookUp){
        startFragmentWithBackstack(ExtendedPlaceInfoFragment.newInstance(placeToLookUp));
    }


    @Override
    public void initiateSearch(String searchParams) {
        params = searchParams;
        locationHelper.startAcquiringLocation();
    }

    @Override
    public void initiateSuggestionSearch() {
        locationHelper.startAcquiringLocation();
    }

    private void showResults(){
        Intent resultsActivity = new Intent(this, ResultsActivity.class)
                .putExtra(BaseActivity.KEY_SEARCH_PARAMS, params)
                .setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(resultsActivity);
        params = null;
    }

    @Override
    public void addSelectedPlace(MyPlaceInfo selectedPlace) {

    }

    @Override
    public void removeSelectedPlace(MyPlaceInfo selectedPlace) {

    }

    @Override
    public ArrayList<MyPlaceInfo> getSelectedPlaces() {
        return visitedPlaces;
    }

    @Override
    public void gotLocation(Location location) {
        String locationParam;
        if(location != null)
            locationParam = location.getLatitude() + "," + location.getLongitude();
        else {
            //display dialog saying location cant be retrieved
            return;
        }

        if(StringUtils.isNullOrEmpty(params)) {
            SearchParameters paramsBuilder = new SearchParameters();
            params = paramsBuilder.getSuggestionParams(locationParam);
        } else params += locationParam;

        showResults();
    }

    private class GetVisitedPlaces extends AsyncTask<Void, Void, SearchResult>{

        @Override
        protected SearchResult doInBackground(Void... params) {
            return AppController.getPlacesGetter().getVisitedPlaces();
        }

        @Override
        protected void onPostExecute(SearchResult myVisitedPlaces){
            visitedPlaces = myVisitedPlaces.results;
        }
    }

}
