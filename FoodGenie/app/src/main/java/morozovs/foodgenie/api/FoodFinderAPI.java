package morozovs.foodgenie.api;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import morozovs.foodgenie.models.ExtendedSearchResult;
import morozovs.foodgenie.models.MyPlaceInfo;
import morozovs.foodgenie.models.SearchParameters;
import morozovs.foodgenie.models.SearchResult;
import morozovs.foodgenie.utils.DataGetter;
import morozovs.foodgenie.utils.FoodGenieDataSource;

public class FoodFinderAPI {
    private static DataGetter dataGetter = new DataGetter();
    private static FoodGenieDataSource dataSource = null;
    private static final String placeSearchUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    private static final String placeDetailsUrl = "https://maps.googleapis.com/maps/api/place/details/json?";

    private static Context context;
    public static ArrayList<MyPlaceInfo> myPlaces = null;

    public static void toLog(String tag, String message){

        Log.i("", "\n**************  " + tag.toUpperCase() + "  **************");
        Log.i("", message);
        Log.i("", "\n**************  END  **************");
    }

    public static SearchResult searchForPlaces(String params, Context context) {
        if(myPlaces == null)
            myPlaces = getMyVisitedPlaces(context);

        SearchResult searchResult = (SearchResult)dataGetter.getData(params, placeSearchUrl, SearchResult.class, context);

        if(!searchResult.validResults())
            return new SearchResult();

        ArrayList<MyPlaceInfo> temp = (ArrayList<MyPlaceInfo>)searchResult.results.clone();
        for(MyPlaceInfo p : temp){
            int indexOf = myPlaces.indexOf(p);
            if(indexOf >= 0) {
                MyPlaceInfo m = myPlaces.get(indexOf);
                if(m.isBlacklisted)
                    searchResult.results.remove(p);
                else {
                    p.setMyReview(m.getMyReview());
                    p.setVisited(m.isVisited());
                }
            }
        }

        return searchResult;
    }

    public static SearchResult getVisitedPlaces(Context context) {
        return new SearchResult(getMyVisitedPlaces(context));
    }

    public static void saveVisitedPlace(Context context, MyPlaceInfo visitedPlace) {

        //checking if we already visited a place
        for(MyPlaceInfo p : myPlaces){
            if(p.equals(visitedPlace))
                return;
        }

        dataSource.saveMyPlaceInfo(visitedPlace);
        myPlaces.add(visitedPlace);
    }

    public static void removeVisitedPlace(Context context, String placeId) {

        dataSource.deleteMyPlaceInfo(placeId);
        myPlaces = getMyVisitedPlaces(context);
    }

    public static void updateVisitedPlace(Context context, MyPlaceInfo visitedPlace) {
        if(dataSource == null || getContext() != context)
            dataSource = getDataSource(context);

        dataSource.updateMyPlaceInfo(visitedPlace);
        myPlaces = getMyVisitedPlaces(context);
    }

    public static ExtendedSearchResult getPlaceInfo(String placeId){
        String params = "placeid="+placeId+"&key="+ SearchParameters.APIKey;
        ExtendedSearchResult result = (ExtendedSearchResult)dataGetter.getData(params, placeDetailsUrl, ExtendedSearchResult.class, context);
        return result;
    }

    private static ArrayList<MyPlaceInfo> getMyVisitedPlaces(Context context){
        if(dataSource == null || getContext() != context)
            dataSource = getDataSource(context);

        ArrayList<MyPlaceInfo> myPlaceInfos = dataSource.getAllPlaces();
        if(myPlaceInfos == null)
            return new ArrayList<MyPlaceInfo>();
        return myPlaceInfos;
    }

    private static FoodGenieDataSource getDataSource(Context context){
        setContext(context);
        return new FoodGenieDataSource(getContext());
    }

    private static void setContext(Context newContext){
        context = newContext;
    }

    private static Context getContext(){
        return context;
    }
}
