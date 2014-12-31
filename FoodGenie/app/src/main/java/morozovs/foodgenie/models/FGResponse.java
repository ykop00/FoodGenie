package morozovs.foodgenie.models;

import com.android.volley.Response;

import java.lang.reflect.Type;

import morozovs.foodgenie.interfaces.IPlacesGetterResponseHandler;
import morozovs.foodgenie.interfaces.IResponseHandler;
import morozovs.foodgenie.utils.JSONManager;

public class FGResponse implements Response.Listener<String> {

    private Type objectReturnType;
    private IResponseHandler callback;
    private IPlacesGetterResponseHandler placesHandlerCallback;

    public FGResponse(IResponseHandler callback, Type objectReturnType, IPlacesGetterResponseHandler placesHandlerCallback){
        this.callback = callback;
        this.objectReturnType = objectReturnType;
        this.placesHandlerCallback = placesHandlerCallback;
    }

    @Override
    public void onResponse(String response) {
        Object r = JSONManager.getObject(response, objectReturnType);
        if(placesHandlerCallback != null)
               placesHandlerCallback.onPlacesResponse(callback, r);
        else callback.onResponse(r);
    }
}
