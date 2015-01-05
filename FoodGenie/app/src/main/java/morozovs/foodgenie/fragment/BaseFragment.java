package morozovs.foodgenie.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import morozovs.foodgenie.R;
import morozovs.foodgenie.models.MyPlaceInfo;
import morozovs.foodgenie.utils.StringUtils;

public abstract class BaseFragment extends Fragment {

    ProgressDialog progressDialog;

    protected void startLoadAnimation(Context context){
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Genie working");
        progressDialog.setMessage("Your wish is my command!");
        progressDialog.show();
    }

    protected void stopLoadAnimation(){
        if(progressDialog != null){
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public Intent createShareIntent(ArrayList<MyPlaceInfo> selectedPlaces){
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, StringUtils.myPlacesLIstToString(selectedPlaces));
        shareIntent.setType("text/plain");
        return shareIntent;
    }
}
