package morozovs.foodgenie.api;

import android.util.Log;
import android.util.Pair;

import java.util.HashMap;
import java.util.Map;

import morozovs.foodgenie.interfaces.IPlacesGetterResponseHandler;
import morozovs.foodgenie.interfaces.IResponseHandler;
import morozovs.foodgenie.models.ExtendedSearchResult;
import morozovs.foodgenie.models.SearchParameters;
import morozovs.foodgenie.models.SearchResult;
import morozovs.foodgenie.utils.DataGetter;
import morozovs.foodgenie.utils.StringUtils;

public class FoodFinderAPI {
    private static DataGetter dataGetter = new DataGetter();
    private static final String placeSearchUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    private static final String placeDetailsUrl = "https://maps.googleapis.com/maps/api/place/details/json?";

    public static void toLog(String tag, String message){

        Log.i("", "\n**************  " + tag.toUpperCase() + "  **************");
        Log.i("", message);
        Log.i("", "\n**************  END  **************");
    }

    public static void searchForPlaces(String params, IResponseHandler callback, IPlacesGetterResponseHandler placesCallback) {
        dataGetter.getData(params, placeSearchUrl, SearchResult.class, callback, placesCallback);
    }

    public static void getPlaceInfo(String placeId, IResponseHandler callback){
        String params = "placeid="+placeId+"&key="+ SearchParameters.APIKey;
        dataGetter.getData(params, placeDetailsUrl, ExtendedSearchResult.class, callback, null);
    }

//    private static Pair<String, String> getParam(String key, String value){
//        if(StringUtils.isNullOrEmpty(key))
//            try {
//                throw new Exception("attempting to add a param with null key");
//            } catch (Exception e) {
//                toLog("NO GOOD", e.getMessage());
//            }
//        return new Pair<String, String>(key, value);
//    }
}
