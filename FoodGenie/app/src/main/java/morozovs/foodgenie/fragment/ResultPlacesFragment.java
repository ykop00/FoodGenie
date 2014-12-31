package morozovs.foodgenie.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import morozovs.foodgenie.R;
import morozovs.foodgenie.activities.BaseActivity;
import morozovs.foodgenie.adapters.ResultPlacesAdapter;
import morozovs.foodgenie.interfaces.IResponseHandler;
import morozovs.foodgenie.interfaces.IPlacesGetter;
import morozovs.foodgenie.interfaces.IResultSelectionRefresh;
import morozovs.foodgenie.models.MyPlaceInfo;
import morozovs.foodgenie.models.SearchResult;
import morozovs.foodgenie.utils.AppController;
import morozovs.foodgenie.utils.PlacesGetter;

public class ResultPlacesFragment extends BasePlacesFragment implements IResponseHandler, IResultSelectionRefresh {
    String searchParams;
    ListView listView;
    ResultPlacesAdapter resultPlaces;
    TextView noResults;

    public static ResultPlacesFragment newInstance(String searchParams){
        Bundle b = new Bundle();
        b.putString(BaseActivity.KEY_SEARCH_PARAMS, searchParams);
        ResultPlacesFragment p = new ResultPlacesFragment();
        p.setArguments(b);
        return p;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        if(getArguments() != null){
            searchParams = getArguments().getString(BaseActivity.KEY_SEARCH_PARAMS);
        }

        listView = (ListView)getView().findViewById(android.R.id.list);

        if(resultPlaces == null)
            loadPlaces();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_places_list, container, false);
        noResults = (TextView) view.findViewById(R.id.no_results);

        return view;
    }

    private void loadPlaces(){
        startLoadAnimation(getActivity());
        AppController.getPlacesGetter().searchForPlaces(searchParams, this);
    }

//    private Map<String, String> getParams(){
//        return new HashMap<String, String>();
//    }

//    @Override
//    public Loader<SearchResult> onCreateLoader(int id, Bundle args) {
//        startLoadAnimation(getActivity());
//        if(id == LOAD_PLACES)
//            return new GetSearchResultsLoader(getActivity(), searchParams);
//        else return null;
//    }
//
//    @Override
//    public void onLoadFinished(Loader<SearchResult> loader, SearchResult data) {
//        resultPlaces = new ResultPlacesAdapter(getActivity(), data.results, resultSelectionManagement, extendedResultsGetter);
//        if(data.results.isEmpty()){
//            listView.setVisibility(View.GONE);
//            noResults.setVisibility(View.VISIBLE);
//        } else {
//            listView.setVisibility(View.VISIBLE);
//            noResults.setVisibility(View.GONE);
//            listView.setAdapter(resultPlaces);
//            getLoaderManager().destroyLoader(loader.getId());
//        }
//        stopLoadAnimation();
//    }
//
//    @Override
//    public void onLoaderReset(Loader<SearchResult> loader) {
//
//    }

    @Override
    public void setSelection(ArrayList<MyPlaceInfo> selectedPlaces) {
        if(listView != null)
            listView.invalidateViews();
    }

    private void displayResults(SearchResult data){
        resultPlaces = new ResultPlacesAdapter(getActivity(), data.results, resultSelectionManagement, extendedResultsGetter);
        if(data.results.isEmpty()){
            listView.setVisibility(View.GONE);
            noResults.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.VISIBLE);
            noResults.setVisibility(View.GONE);
            listView.setAdapter(resultPlaces);
        }
        stopLoadAnimation();
    }

    @Override
    public void onResponse(Object requestedData) {
        displayResults((SearchResult)requestedData);
    }
}
