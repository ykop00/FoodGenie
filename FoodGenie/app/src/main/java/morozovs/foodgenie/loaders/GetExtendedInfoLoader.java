package morozovs.foodgenie.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import morozovs.foodgenie.api.FoodFinderAPI;
import morozovs.foodgenie.models.ExtendedSearchResult;
import morozovs.foodgenie.models.SearchResult;

public class GetExtendedInfoLoader extends AsyncTaskLoader<ExtendedSearchResult> {

    String placeId;

    public GetExtendedInfoLoader(Context context, String placeId){
        super(context);
        this.placeId = placeId;
    }

    @Override
    public ExtendedSearchResult loadInBackground() {
        return FoodFinderAPI.getPlaceInfo(placeId);
    }

    @Override
    protected void onStartLoading(){
        forceLoad();
    }

    @Override
    public void deliverResult(ExtendedSearchResult results) {
        super.deliverResult(results);
    }
}
