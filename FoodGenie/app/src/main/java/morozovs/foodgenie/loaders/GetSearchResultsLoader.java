package morozovs.foodgenie.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import morozovs.foodgenie.api.FoodFinderAPI;
import morozovs.foodgenie.models.SearchResult;

public class GetSearchResultsLoader extends AsyncTaskLoader<SearchResult> {
    Context context;
    String parameters;

    public GetSearchResultsLoader(Context context, String parameters){
        super(context);
        this.context = context;
        this.parameters = parameters;
    }

    @Override
    public SearchResult loadInBackground() {
        return FoodFinderAPI.searchForPlaces(parameters, context);
    }

    @Override
    protected void onStartLoading(){
        forceLoad();
    }

    @Override
    public void deliverResult(SearchResult results) {
        super.deliverResult(results);
    }

}