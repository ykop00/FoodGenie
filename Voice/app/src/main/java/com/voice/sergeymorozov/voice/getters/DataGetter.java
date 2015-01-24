package com.voice.sergeymorozov.voice.getters;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.voice.sergeymorozov.voice.interfaces.IDataGetter;
import com.voice.sergeymorozov.voice.interfaces.IResponseHandler;
import com.voice.sergeymorozov.voice.utils.AppController;
import com.voice.sergeymorozov.voice.utils.ResponseHelper;

import java.lang.reflect.Type;

public class DataGetter implements IDataGetter, Response.ErrorListener{
    private static String tag_json_obj = "json_obj_req";

    @Override
    public void getData(String parameters, String baseUrl, Type returnObjectType, IResponseHandler callback){

        StringRequest strReq = new StringRequest(Request.Method.GET,
                baseUrl + parameters, new ResponseHelper(callback, returnObjectType), this){
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        //TODO: handle errors
    }

}


