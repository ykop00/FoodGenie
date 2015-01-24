package com.voice.sergeymorozov.voice.utils;

import com.android.volley.Response;
import com.voice.sergeymorozov.voice.interfaces.IResponseHandler;

import java.lang.reflect.Type;


public class ResponseHelper implements Response.Listener<String> {

    private Type objectReturnType;
    private IResponseHandler callback;

    public ResponseHelper(IResponseHandler callback, Type objectReturnType){
        this.callback = callback;
        this.objectReturnType = objectReturnType;
    }

    @Override
    public void onResponse(String response) {
        Object r = JSONManager.getObject(response, objectReturnType);
        callback.onResponse(r);
    }
}
