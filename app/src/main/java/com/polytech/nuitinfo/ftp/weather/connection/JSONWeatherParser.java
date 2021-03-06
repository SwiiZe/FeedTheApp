package com.polytech.nuitinfo.ftp.weather.connection;

import com.polytech.nuitinfo.ftp.weather.model.Location;
import com.polytech.nuitinfo.ftp.weather.model.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JSONWeatherParser {

    public static Weather getWeather(String data) throws JSONException  {
        Weather weather = new Weather();

        JSONObject jObj = new JSONObject(data);

        // We start extracting the info
        Location loc = new Location();

        JSONObject coordObj = getObject("coord", jObj);
        loc.setLatitude(getFloat("lat", coordObj));
        loc.setLongitude(getFloat("lon", coordObj));

        JSONObject sysObj = getObject("sys", jObj);
        loc.setCountry(getString("country", sysObj));
        loc.setSunrise(getInt("sunrise", sysObj));
        loc.setSunset(getInt("sunset", sysObj));
        loc.setCity(getString("name", jObj));
        weather.setLocation( loc );

        // We get weather info (This is an array)
        JSONArray jArr = jObj.getJSONArray("weather");

        // We use only the first value
        JSONObject JSONWeather = jArr.getJSONObject(0);
        weather.getCondition().setID( getInt( "id", JSONWeather ) );
        weather.getCondition().setDescription( getString( "description", JSONWeather ) );
        weather.getCondition().setType(getString( "main", JSONWeather ) );
        weather.getCondition().setIcon( getString( "icon", JSONWeather ) );

        JSONObject mainObj = getObject("main", jObj);
        weather.getCondition().setHumidity(getInt("humidity", mainObj));
        weather.getCondition().setPressure(getInt("pressure", mainObj));
        weather.getTemperature().setMax(getFloat("temp_max", mainObj));
        weather.getTemperature().setMin(getFloat("temp_min", mainObj));
        weather.getTemperature().setValue(getFloat("temp", mainObj));

        // Wind
        JSONObject wObj = getObject("wind", jObj);
        weather.getWind().setSpeed(getFloat("speed", wObj));
        weather.getWind().setDegree(getFloat("deg", wObj));

        // Clouds
        JSONObject cObj = getObject("clouds", jObj);
        weather.getClouds().setPercentage( getInt( "all", cObj ) );

        // We download the icon to show


        return weather;
    }


    private static JSONObject getObject(String tagName, JSONObject jObj)  throws JSONException {
        return jObj.getJSONObject( tagName );
    }

    private static String getString(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getString(tagName);
    }

    private static float  getFloat(String tagName, JSONObject jObj) throws JSONException {
        return (float) jObj.getDouble(tagName);
    }

    private static int  getInt(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getInt(tagName);
    }
}