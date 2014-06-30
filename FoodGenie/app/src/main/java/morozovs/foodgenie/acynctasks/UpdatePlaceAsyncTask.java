package morozovs.foodgenie.acynctasks;

import android.content.Context;

import morozovs.foodgenie.api.FoodFinderAPI;
import morozovs.foodgenie.models.MyPlaceInfo;

public class UpdatePlaceAsyncTask extends BasePlaceAsyncTask {

    public UpdatePlaceAsyncTask(Context context){
        super(context);
    }

    @Override
    protected Boolean doInBackground(MyPlaceInfo... params) {
        if(params.length == 0 || params.length > 1)
            return false;

        MyPlaceInfo p = params[0];
        FoodFinderAPI.updateVisitedPlace(context, p);
        return true;
    }
}
