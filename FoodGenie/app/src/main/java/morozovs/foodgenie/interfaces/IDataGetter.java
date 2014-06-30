package morozovs.foodgenie.interfaces;

import android.content.Context;

import java.lang.reflect.Type;

public interface IDataGetter {
    public abstract Object getData(String params, String urlString, Type returnObjectType, Context context);
}
