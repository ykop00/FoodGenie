package morozovs.foodgenie.utils;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import morozovs.foodgenie.interfaces.IPlacesGetterResponseHandler;
import morozovs.foodgenie.interfaces.IResponseHandler;
import morozovs.foodgenie.models.FGResponse;

public class DataGetter implements Response.ErrorListener{
    private static String tag_json_obj = "json_obj_req";

    public void getData(String parameters, String baseUrl, Type returnObjectType, IResponseHandler callback, IPlacesGetterResponseHandler placesCallback){

        StringRequest strReq = new StringRequest(Request.Method.GET,
                baseUrl + parameters, new FGResponse(callback, returnObjectType, placesCallback), this){
//            @Override
//            protected Map<String, String> getParams() {
//                if(parameters == null)
//                    return new HashMap<String, String>();
//                return  parameters;
//            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
//        VolleyLog.d(TAG, "Error: " + error.getMessage());
//        pDialog.hide();
    }

}


