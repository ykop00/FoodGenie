package morozovs.foodgenie.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import morozovs.foodgenie.R;
import morozovs.foodgenie.adapters.ReviewAdapter;
import morozovs.foodgenie.loaders.GetExtendedInfoLoader;
import morozovs.foodgenie.models.ExtendedPlaceInfo;
import morozovs.foodgenie.models.ExtendedSearchResult;
import morozovs.foodgenie.models.MyPlaceInfo;
import morozovs.foodgenie.utils.StringUtils;

public class ExtendedPlaceInfoFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<ExtendedSearchResult> {

    public static final String KEY_PLACE = "extended_info_place";
    private static final int LOADER_EXTENDED_INFO = 0;

    private static ExtendedPlaceInfo extendedPlaceInfo;
    private static MyPlaceInfo placeToLookUp;

    ViewGroup reviewLayout;
    TextView name;
    TextView price;
    TextView rating;
    TextView url;
    TextView viewMaps;
    ListView reviews;

    public static ExtendedPlaceInfoFragment newInstance(MyPlaceInfo place){
        Bundle b = new Bundle();
        b.putString(KEY_PLACE, place.toJSONString());

        ExtendedPlaceInfoFragment f = new ExtendedPlaceInfoFragment();
        f.setArguments(b);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstatnceState){
        super.onCreate(savedInstatnceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_extended_info, container, false);

        name = (TextView) view.findViewById(R.id.my_place_name);

        price = (TextView) view.findViewById(R.id.my_place_price);

        rating = (TextView) view.findViewById(R.id.my_place_distance);

        url = (TextView) view.findViewById(R.id.my_place_url);
        url.setPaintFlags(url.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = String.format(Locale.ENGLISH, extendedPlaceInfo.website);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });

        viewMaps = (TextView) view.findViewById(R.id.my_place_show_in_maps);
        viewMaps.setPaintFlags(url.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        viewMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = String.format(Locale.ENGLISH, extendedPlaceInfo.url);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });

        reviewLayout = (ViewGroup) view.findViewById(R.id.my_place_review_layout);
        reviews = (ListView)view.findViewById(android.R.id.list);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        startLoadAnimation(getActivity());

        Bundle args = getArguments();
        if(args != null){
            placeToLookUp = MyPlaceInfo.fromJsonString(args.getString(KEY_PLACE));
        }

        if(!StringUtils.isNullOrEmpty(placeToLookUp.getPlace_id()))
            getLoaderManager().restartLoader(LOADER_EXTENDED_INFO, null, this);

    }

    @Override
    public Loader<ExtendedSearchResult> onCreateLoader(int id, Bundle args) {
        return new GetExtendedInfoLoader(getActivity(), placeToLookUp.place_id);
    }

    @Override
    public void onLoadFinished(Loader<ExtendedSearchResult> loader, ExtendedSearchResult data) {
        displayInfo(data.result);
    }

    @Override
    public void onLoaderReset(Loader<ExtendedSearchResult> loader) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_share, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_share){
            ArrayList<MyPlaceInfo> a = new ArrayList<MyPlaceInfo>();
            a.add(placeToLookUp);
            startActivity(createShareIntent(a));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayInfo(ExtendedPlaceInfo result){
        extendedPlaceInfo = result;

        name.setText(result.getName());
        price.setText(result.getFormattedPrice());
        rating.setText(result.getFormattedRating());
        reviews = (ListView)getView().findViewById(android.R.id.list);
        reviews.setAdapter(new ReviewAdapter(getActivity(), result.reviews));

        stopLoadAnimation();
    }
}
