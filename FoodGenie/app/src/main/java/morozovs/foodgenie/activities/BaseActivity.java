package morozovs.foodgenie.activities;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import morozovs.foodgenie.R;

public abstract class BaseActivity extends FragmentActivity {

    public static final String KEY_SEARCH_PARAMS = "search_params";
    public static final String KEY_FINAL_FRAGMENT = "start_final_fragment";
    public static final String KEY_PLACE_INFO_FRAGMENT = "start_place_info_fragment";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        if(getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public String getLocation() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Location l = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        String formattedLat = l.getLatitude() + "," + l.getLongitude();
        return formattedLat;
    }

    protected void startFragmentWithBackstack(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commitAllowingStateLoss();
    }

    public void startFragmentFresh(Fragment f) {
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, f);
        transaction.commitAllowingStateLoss();
    }
}
