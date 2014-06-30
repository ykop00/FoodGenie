package morozovs.foodgenie.utils;

import android.content.Context;

import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Arrays;

import javax.net.ssl.HttpsURLConnection;

import morozovs.foodgenie.activities.HomeActivity;
import morozovs.foodgenie.api.FoodFinderAPI;
import morozovs.foodgenie.interfaces.IDataGetter;

public class DataGetter implements IDataGetter {

    public static final int TIMEOUT_MILLIES = 30000;

    @Override
    public Object getData(String parameters, String urlString, Type returnObjectType, Context context){
        FoodFinderAPI.toLog("urlString is", urlString);
        FoodFinderAPI.toLog("params", parameters);

        return _getData(urlString + parameters, returnObjectType);
    }

    private static Object _getData(String urlString, Type returnObjectType){
        try {
            HttpsURLConnection conn = setUpConnection(urlString);

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setInstanceFollowRedirects(true);
            conn.setConnectTimeout(TIMEOUT_MILLIES);
            conn.setReadTimeout(TIMEOUT_MILLIES);

            InputStreamReader is = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(is);
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            br.close();

            FoodFinderAPI.toLog("JSONObject returned:", sb.toString());

            if(returnObjectType != null)
                return JSONManager.getObject(sb.toString(), returnObjectType);
            return sb.toString();

        }catch (Exception e){
            if(e instanceof SocketTimeoutException)
                FoodFinderAPI.toLog("SocketTimeoutException occurred", Arrays.toString(e.getStackTrace()));
            if(e instanceof FileNotFoundException)
                FoodFinderAPI.toLog("FileNotFoundException occurred", Arrays.toString(e.getStackTrace()));
            if(e instanceof IOException)
                FoodFinderAPI.toLog("IOException occurred", Arrays.toString(e.getStackTrace()));
            if(e instanceof JsonSyntaxException) {
                FoodFinderAPI.toLog("JsonSyntaxException occurred", Arrays.toString(e.getStackTrace()));
            }
            FoodFinderAPI.toLog("Unknown exception occurred", Arrays.toString(e.getStackTrace()));
        }
        return null;
    }

    public static HttpsURLConnection setUpConnection(String urlString) throws IOException {

        HttpsURLConnection connection;
        connection = (HttpsURLConnection)new URL(urlString).openConnection();

        return connection;
    }

}


